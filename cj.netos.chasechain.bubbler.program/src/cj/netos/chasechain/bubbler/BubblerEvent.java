package cj.netos.chasechain.bubbler;

public class BubblerEvent {
    String key;
    Object args;
    public BubblerEvent(String key) {
        this.key = key;
    }
    public BubblerEvent(String key, Object args) {
        this.key = key;
        this.args=args;
    }

    public Object getArgs() {
        return args;
    }

    public void setArgs(Object args) {
        this.args = args;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
