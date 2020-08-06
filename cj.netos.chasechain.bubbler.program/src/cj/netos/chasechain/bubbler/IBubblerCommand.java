package cj.netos.chasechain.bubbler;

import cj.studio.ecm.net.CircuitException;

public interface IBubblerCommand {
    void doCommand(BubblerEvent bubblerEvent) throws CircuitException;
}
