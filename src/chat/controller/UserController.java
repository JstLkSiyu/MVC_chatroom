package chat.controller;

import chat.entity.JsonUser;
import chat.entity.User;
import chat.service.UserService;
import chat.utils.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "login")
    @ResponseBody
    public Map<String, Object> login(@RequestParam("username") String uname, @RequestParam("password") String password, HttpSession session) {
        List<User> result = userService.login(uname, password);
        Map<String, Object> json = new HashMap<>();
        if(result.size() > 0) {
            User user = result.get(0);
            /*
            Store uid of user into session as signal of login
             */
            session.setAttribute("uid", user.getUid());
            /*
            Package the JSON response body
             */
            json.put("success", true);

            Map<String, Object> userInfo = new HashMap<>();
            //String token = generateToken(user.getUid(), session);
            userInfo.put("username", uname);
            userInfo.put("age", MiscUtils.convertBirthdayToAge(user.getBirthday()));
            userInfo.put("gender", user.getGender());
            //userInfo.put("token", token);
            userInfo.put("uid", user.getUid());

            json.put("userInfo", userInfo);
        } else {
            json.put("success", false);
        }
        return json;
    }

    @PostMapping(value = "register")
    @ResponseBody
    public Map<String, Object> register(@RequestParam("username") String uname, @RequestParam("password") String password, @RequestParam("gender") String gender, @RequestParam("birthday") String birthday) {
        Map<String, Object> json = new HashMap<>();
        try {
            if(userService.register(uname, password, gender, birthday)) {
                json.put("success", true);
            } else {
                json.put("success", false);
            }
        } catch (DuplicateKeyException dke) {
            json.put("success", false);
            json.put("error", "用户已存在");
        }
        return json;
    }

    @RequestMapping(value = "logout")
    @ResponseBody
    public Map<String, Object> logout(HttpSession session) {
        Map<String, Object> json = new HashMap<>();
        try {
            session.removeAttribute("uid");
            json.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("success", false);
        }
        return json;
    }

    @RequestMapping(value = "user_info")
    @ResponseBody
    public Map<String, Object> userInfo(HttpSession session) {
        Map<String, Object> json = new HashMap<>();
        try {
            String uid = (String) session.getAttribute("uid");
            if(MiscUtils.isEmpty(uid)) {
                throw new NullPointerException();
            }
            List<User> result = userService.getUserInfoByUid(uid);
            if(result.size() > 0) {
                User user = result.get(0);
                Map<String, Object> userInfo = new HashMap<>();

                //String token = generateToken(user.getUid(), session);
                userInfo.put("username", user.getUname());
                userInfo.put("age", MiscUtils.convertBirthdayToAge(user.getBirthday()));
                userInfo.put("gender", user.getGender());
                userInfo.put("isLogin", true);
                //userInfo.put("token", token);
                userInfo.put("uid", user.getUid());

                json.put("success", true);
                json.put("userInfo", userInfo);
            }
        } catch (NullPointerException npe) {
            json.put("success", true);
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("isLogin", false);
            json.put("userInfo", userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("success", false);
        }
        return json;
    }

    @PostMapping(value = "search_user")
    @ResponseBody
    public Map<String, Object> search(@RequestParam("username") String uname, HttpSession session) {
        Map<String, Object> json = new HashMap<>();
        try {
            String uid = (String) session.getAttribute("uid");
            if(MiscUtils.isEmpty(uid)) {
                throw new NullPointerException();
            }
            List<User> result = userService.searchUsersByUname(uname);
            JsonUser[] users = MiscUtils.packageJsonUser(result);
            json.put("success", true);
            json.put("result", users);
        } catch (NullPointerException npe) {
            /*
            when uid in session is empty, the search action is illegal
             */
            json.put("success", false);
            json.put("error", "bad authorized");
        }
        return json;
    }


    private String generateToken(String uid, HttpSession session) {
        /*
        Generate a salt value if the session not exists salt,
        encode the salt with uid to generate a token key,
        when the token transmitted from fore side was faked,
        encode(uid, salt) != token,
        then the real websocket connection could not be occupied
         */
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
