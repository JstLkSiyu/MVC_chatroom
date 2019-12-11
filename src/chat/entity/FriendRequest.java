package chat.entity;

public class FriendRequest {
    public enum FriendQueryStatus {
        INCOMPLETE, AGREE, REJECT
    }

    private String from;
    private String to;
    private String status;
    private User user;

    /*
    Getter
     */
    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public String getStatus() {
        return status;
    }

    public User getUser() {
        return user;
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

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
