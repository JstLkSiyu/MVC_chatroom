package chat.dao;

import chat.entity.FriendRequest;
import chat.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface RelationDao {
    int addRelation(@Param("u1id") String u1id, @Param("u2id") String u2id, @Param("day") Timestamp day);

    int delRelation(@Param("u1id") String u1id, @Param("u2id") String u2id);

    List<User> queryRelationOfUid(@Param("uid") String uid);

    int pushFriendRequest(@Param("from") String from, @Param("to") String to);

    int popFriendRequest(@Param("from") String from, @Param("to") String to);

    void popBothFriendRequest(@Param("u1id") String u1id, @Param("u2id") String u2id);

    List<FriendRequest> getFriendRequestByFromUid(@Param("uid") String uid);

    List<FriendRequest> getFriendRequestByToUid(@Param("uid") String uid);

    int changeStatus(@Param("from") String from, @Param("to") String to, @Param("status") String status);
}
