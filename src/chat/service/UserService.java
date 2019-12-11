package chat.service;

import chat.dao.UserDao;
import chat.entity.User;
import chat.utils.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserDao userDao;

    public List<User> login(String uname, String password) {
        password = "*" + MiscUtils.encode(password);
        return userDao.login(uname, password);
    }

    public boolean register(String uname, String password, String gender, String birthday) {
        String uid = "u_" + MiscUtils.encode(uname, password).substring(7, 23);
        password = "*" + MiscUtils.encode(password);
        return userDao.register(uid, uname, password, gender, MiscUtils.convertStringToTimestamp(birthday)) > 0;
    }

    public List<User> getUserInfoByUid(String uid) {
        return userDao.getUserInfoByUid(uid);
    }

    public List<User> searchUsersByUname(String uname) {
        return userDao.searchUsersByUname(uname);
    }

}
