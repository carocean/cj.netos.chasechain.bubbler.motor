package cj.netos.chasechain.bubbler.etlcmd;

import cj.netos.chasechain.bubbler.BubblerEvent;
import cj.netos.chasechain.bubbler.IBubblerCommand;
import cj.netos.chasechain.bubbler.IBubblerEngine;
import cj.netos.chasechain.bubbler.ITrafficPoolService;
import cj.netos.rabbitmq.IRabbitMQProducer;
import cj.studio.ecm.CJSystem;
import cj.studio.ecm.IServiceSite;
import cj.studio.ecm.net.CircuitException;
import com.rabbitmq.client.AMQP;

import java.util.HashMap;
import java.util.List;

public class TrafficPoolCommand implements IBubblerCommand {

    ITrafficPoolService trafficPoolService;
    IBubblerEngine bubblerEngine;
    IRabbitMQProducer rabbitMQProducer;

    public TrafficPoolCommand(IServiceSite site) {
        trafficPoolService = (ITrafficPoolService) site.getService("defaultTrafficPoolService");
        bubblerEngine = (IBubblerEngine) site.getService("defaultBubblerEngine");
        rabbitMQProducer = (IRabbitMQProducer) site.getService("@.rabbitmq.producer.bubbler");
    }

    @Override
    public void doCommand(BubblerEvent bubblerEvent) throws CircuitException {
        int limit = 100;
        long offset = 0;
        while (bubblerEngine.isRunning()) {
            List<String> pools = trafficPoolService.pageLowerPool(limit, offset);
            if (pools.isEmpty()) {
                break;
            }
            offset += pools.size();
            for (String pool : pools) {
                AMQP.BasicProperties props = new AMQP.BasicProperties().builder()
                        .type("/bubbler.mq")
                        .headers(new HashMap<String, Object>() {{
                            put("command", "doBubbler");
                            put("traffic-pool", pool);
                        }})
                        .build();

                rabbitMQProducer.publish("bubbler", props, new byte[0]);
                CJSystem.logging().info(getClass(), String.format("发现流量池:%s", pool));
            }
        }
    }
}
