package chat.entity;

import java.sql.Timestamp;

public class MsgLog {
    private String from;
    private String to;
    private String msg;
    private Timestamp log_time;
    private String status;

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

    public String getStatus() {
        return status;
    }

    public Timestamp getLog_time() {
        return log_time;
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

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLog_time(Timestamp log_time) {
        this.log_time = log_time;
    }
}
