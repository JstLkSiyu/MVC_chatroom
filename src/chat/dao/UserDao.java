package chat.dao;

import chat.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface UserDao {

    List<User> login(@Param("uname") String uname, @Param("password") String password);

    int register(@Param("uid") String uid, @Param("uname") String uname, @Param("password") String password, @Param("gender") String gender, @Param("birthday") Timestamp birthday);

    List<User> getUserInfoByUid(@Param("uid") String uid);

    List<User> searchUsersByUname(@Param("uname") String uname);

}
