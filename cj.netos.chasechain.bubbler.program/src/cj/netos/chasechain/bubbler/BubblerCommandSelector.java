package cj.netos.chasechain.bubbler;

import cj.studio.ecm.IServiceSite;

import java.util.HashMap;
import java.util.Map;

public class BubblerCommandSelector implements IBubblerCommandSelector {
    IServiceSite site;
    Map<String, IBubblerCommand> commandMap;
    IBubblerCommandBuilder commandBuilder;

    public BubblerCommandSelector(IServiceSite site, IBubblerCommandBuilder commandBuilder) {
        this.site = site;
        commandMap = new HashMap<>();
        this.commandBuilder = commandBuilder;
    }

    @Override
    public synchronized IBubblerCommand select(String key) {
        if (commandMap.containsKey(key)) {
            return commandMap.get(key);
        }
        IBubblerCommand command=commandBuilder.create(key);
        commandMap.put(key, command);
        return command;
    }
}
