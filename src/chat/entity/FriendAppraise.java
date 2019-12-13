package chat.entity;

public class FriendAppraise {
    private String appraise;
    private User from;

    public String getAppraise() {
        return appraise;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public void setAppraise(String appraise) {
        this.appraise = appraise;
    }
}
