package chat.utils;

import chat.controller.WebsocketController;
import chat.entity.JsonUser;
import chat.entity.User;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

public class MiscUtils {

    public static int convertBirthdayToAge(Timestamp birthday) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        int now_year = calendar.get(Calendar.YEAR);
        calendar.setTime(birthday);
        int birth_year = calendar.get(Calendar.YEAR);
        return now_year - birth_year;
    }

    public static boolean isEmpty(String arg) {
        return arg == null || arg.trim().equals("");
    }

    public static JsonUser[] packageJsonUser(List<User> raw) {
        JsonUser[] users = new JsonUser[raw.size()];
        int idx = 0;
        for(User user : raw) {
            users[idx] = new JsonUser();
            users[idx].setUid(user.getUid());
            users[idx].setUname(user.getUname());
            users[idx].setAge(MiscUtils.convertBirthdayToAge(user.getBirthday()));
            users[idx].setGender(user.getGender());
            idx ++;
        }
        return users;
    }

    public static String encode(String... params) {
        StringBuilder builder = new StringBuilder();
        for(String p : params) {
            builder.append(p);
        }
        return DigestUtils.md5DigestAsHex(builder.toString().getBytes());
    }

    public static Timestamp convertStringToTimestamp(String raw) {
        return Timestamp.valueOf(raw);
    }

    public static Timestamp convertLocalDateTimeToTimestamp(LocalDateTime time) {
        return Timestamp.valueOf(time);
    }

    public static String convertTimestampToString(Timestamp time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
    }

    public static Map<String, Object> packMessageJson(String uid, String msg) {
        Map<String, Object> message = new HashMap<>(), resp = new HashMap<>();
        resp.put("type", WebsocketController.msgType.MESSAGE);
        message.put("fid", uid);
        message.put("msg", msg);
        resp.put("msg", message);
        return resp;
    }

    public static String generateToken(String uid, HttpSession session) {
        String salt;
        if(session.getAttribute("token_salt") == null) {
            salt = Double.toString(Math.random());
            session.setAttribute("token_salt", salt);
        } else {
            salt = (String) session.getAttribute("token_salt");
        }
        return MiscUtils.encode(uid, salt);
    }
}
