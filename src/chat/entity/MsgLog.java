package chat.entity;

public class MsgLog {
    private String from;
    private String to;
    private String msg;

    /*
    Getter
     */
    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getMsg() {
        return msg;
    }

    /*
    Setter
     */
    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
