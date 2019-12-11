package chat.service;

import chat.dao.RelationDao;
import chat.entity.FriendRequest;
import chat.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class RelationService {
    @Autowired
    private RelationDao relationDao;

    public boolean addRelation(String u1id, String u2id) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if(u1id.compareTo(u2id) > 0) {
            /*
            swap when u1id > u2id (dictionary order)
             */
            String tmp = u1id;
            u1id = u2id;
            u2id = tmp;
        }
        return relationDao.addRelation(u1id, u2id, now) > 0;
    }

    public boolean delRelation(String u1id, String u2id) {
        if(u1id.compareTo(u2id) > 0) {
            /*
            swap when u1id > u2id (dictionary order)
             */
            String tmp = u1id;
            u1id = u2id;
            u2id = tmp;
        }
        return relationDao.delRelation(u1id, u2id) > 0;
    }

    public List<User> queryRelationOfUid(String uid) {
        return relationDao.queryRelationOfUid(uid);
    }

    public boolean addFriendRequest(String uid, String fid) {
        return relationDao.pushFriendRequest(uid, fid) > 0;
    }

    public boolean popFriendRequest(String from, String to) {
        return relationDao.popFriendRequest(from, to) > 0;
    }

    public void popBothFriendRequest(String u1id, String u2id) {
        relationDao.popBothFriendRequest(u1id, u2id);
    }

    public List<FriendRequest> getFriendRequestByFromUid(String from) {
        return relationDao.getFriendRequestByFromUid(from);
    }

    public List<FriendRequest> getFriendRequestByToUid(String to) {
        return relationDao.getFriendRequestByToUid(to);
    }

    public boolean changeStatus(String from, String to, String status) {
        return relationDao.changeStatus(from, to, status) > 0;
    }
}
