package chat.utils;

import chat.entity.JsonUser;
import chat.entity.User;
import org.springframework.util.DigestUtils;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

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
}
