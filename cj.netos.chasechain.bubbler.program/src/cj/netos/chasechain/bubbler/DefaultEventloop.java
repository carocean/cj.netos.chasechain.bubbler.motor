package cj.netos.chasechain.bubbler;

import cj.studio.ecm.CJSystem;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultEventloop implements Runnable {
    IBubblerCommandSelector commandSelector;
    long period;
    List<BubblerEvent> loopEvents;
    AtomicBoolean isRunning;

    public DefaultEventloop(List<BubblerEvent> loopEvents, IBubblerCommandSelector commandSelector, AtomicBoolean isRunning, long period) {
        this.commandSelector = commandSelector;
        this.period = period;
        this.loopEvents = loopEvents;
        this.isRunning = isRunning;
    }

    @Override
    public void run() {
        try {
            doEventloop();
        } catch (InterruptedException e) {
            CJSystem.logging().error(getClass(), e);
        }
    }

    private void doEventloop() throws InterruptedException {
        while (isRunning.get()) {
            for (BubblerEvent etlEvent : loopEvents) {
                IBubblerCommand command = commandSelector.select(etlEvent.key);
                if (command == null) {
                    CJSystem.logging().warn(getClass(), String.format("不支持的事件:%s", etlEvent));
                    continue;
                }
                try {
                    command.doCommand(etlEvent);
                } catch (Throwable e) {
                    CJSystem.logging().error(getClass(), e);
                }
            }
            Thread.sleep(period);
        }
    }
}
