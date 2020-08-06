package cj.netos.chasechain.bubbler.ports;

import cj.netos.chasechain.bubbler.IBubblerEngine;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.ISecuritySession;

@CjService(name = "/motor.ports")
public class DefaultMotorPorts implements IMotorPorts {
    @CjServiceRef(refByName = "defaultBubblerEngine")
    IBubblerEngine bubblerEngine;

    //按源数据表的ctime作为增量抽取的依据
    @Override
    public boolean isRunning(ISecuritySession securitySession) throws CircuitException {
        return bubblerEngine.isRunning();
    }

    @Override
    public void start(ISecuritySession securitySession, int workThreadCount, long delay, long period) throws CircuitException {
        if (!securitySession.roleIn("platform:administrators")) {
            throw new CircuitException("801", "拒绝访问");
        }
        bubblerEngine.start(securitySession.principal(), workThreadCount, delay, period);
    }

    @Override
    public void stop(ISecuritySession securitySession) throws CircuitException {
        if (!securitySession.roleIn("platform:administrators")) {
            throw new CircuitException("801", "拒绝访问");
        }
        bubblerEngine.stop();
    }
}
