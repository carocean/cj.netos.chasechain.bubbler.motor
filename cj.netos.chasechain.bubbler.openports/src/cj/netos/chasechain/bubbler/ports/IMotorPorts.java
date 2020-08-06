package cj.netos.chasechain.bubbler.ports;

import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.IOpenportService;
import cj.studio.openport.ISecuritySession;
import cj.studio.openport.annotations.CjOpenport;
import cj.studio.openport.annotations.CjOpenportParameter;
import cj.studio.openport.annotations.CjOpenports;

@CjOpenports(usage = "Bubbler发动器开放服务")
public interface IMotorPorts extends IOpenportService {
    @CjOpenport(usage = "工作状态")
    boolean isRunning(ISecuritySession securitySession) throws CircuitException;

    @CjOpenport(usage = "启动工作")
    void start(ISecuritySession securitySession,
               @CjOpenportParameter(usage = "工作线程数，0表示默认", name = "workThreadCount", defaultValue = "0") int workThreadCount,
               @CjOpenportParameter(usage = "延迟多少毫秒执行，0表立即", name = "delay", defaultValue = "0") long delay,
               @CjOpenportParameter(usage = "间隔多少毫秒检查一次数据源", name = "period", defaultValue = "600000") long period) throws CircuitException;


    @CjOpenport(usage = "停止工作")
    void stop(ISecuritySession securitySession) throws CircuitException;


}
