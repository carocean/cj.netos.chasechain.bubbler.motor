package cj.netos.chasechain.bubbler;

public interface IBubblerEngine {
    boolean isRunning();

    void stop();

    void start(String operator, int workThreadCount, long delay, long period);

}
