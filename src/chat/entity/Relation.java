package chat.entity;

public class Relation {
    private String u1id;
    private String u2id;

    public String getU2id() {
        return u2id;
    }

    public String getU1id() {
        return u1id;
    }

    public void setU2id(String u2id) {
        this.u2id = u2id;
    }

    public void setU1id(String u1id) {
        this.u1id = u1id;
    }
}
