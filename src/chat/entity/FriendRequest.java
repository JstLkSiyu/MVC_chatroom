package chat.entity;

public class FriendRequest {
    public enum FriendQueryStatus {
        INCOMPLETE, AGREE, REJECT
    }

    private String from;
    private String to;
    private FriendQueryStatus status;

    /*
    Getter
     */
    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public FriendQueryStatus getStatus() {
        return status;
    }

    /*
    Setter
     */
    public void setTo(String to) {
        this.to = to;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setStatus(FriendQueryStatus status) {
        this.status = status;
    }
}
