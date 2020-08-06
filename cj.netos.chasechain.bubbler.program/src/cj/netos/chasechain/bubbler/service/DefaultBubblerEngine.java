package cj.netos.chasechain.bubbler.service;

import cj.netos.chasechain.bubbler.*;
import cj.netos.chasechain.bubbler.etlcmd.TrafficPoolCommand;
import cj.studio.ecm.CJSystem;
import cj.studio.ecm.IServiceSite;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceSite;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@CjService(name = "defaultBubblerEngine")
public class DefaultBubblerEngine implements IBubblerEngine {
    AtomicBoolean isRunning;
    ExecutorService executorService;
    @CjServiceSite
    IServiceSite site;

    @Override
    public boolean isRunning() {
        return isRunning.get();
    }

    @Override
    public void stop() {
        if (!isRunning.get()) {
            return;
        }
        isRunning.set(false);
        executorService.shutdown();
    }

    @Override
    public void start(String operator, int workThreadCount, long delay, long period) {
        if (isRunning == null) {
            isRunning = new AtomicBoolean(false);
        }
        if (isRunning.get()) {
            return;
        }

        if (workThreadCount == 0) {
            int count = Runtime.getRuntime().availableProcessors();
            workThreadCount = count;
        }
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            CJSystem.logging().info(getClass(), String.format("线程中断"));
            return;
        }

        executorService = Executors.newFixedThreadPool(workThreadCount);
        IBubblerCommandSelector selector = new BubblerCommandSelector(site, new IBubblerCommandBuilder() {
            @Override
            public IBubblerCommand create(String key) {
                IBubblerCommand command = null;
                switch (key) {
                    case "bubbler.command.pageTrafficPool":
                        command = new TrafficPoolCommand(site);
                        break;
                }
                return command;
            }
        });
        List<BubblerEvent> loopEvents = new ArrayList<>();
        loopEvents.add(new BubblerEvent("bubbler.command.pageTrafficPool"));
        isRunning.set(true);
        for (int i = 0; i < workThreadCount; i++) {
            executorService.submit(new DefaultEventloop(loopEvents, selector, isRunning, period));
        }

        CJSystem.logging().info(getClass(), String.format("Etl引擎已启动，参数:\r\n"));
        CJSystem.logging().info(getClass(), String.format("\tworkThreadCount=%s", workThreadCount));
        CJSystem.logging().info(getClass(), String.format("\tdelay=%s", delay));
        CJSystem.logging().info(getClass(), String.format("\tperiod=%s", period));
    }

}
