package chat.controller;

import chat.entity.FriendAppraise;
import chat.entity.JsonUser;
import chat.entity.User;
import chat.service.RelationService;
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
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RelationService relationService;

    @PostMapping(value = "/login")
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
            session.setAttribute("uname", user.getUname());
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
            session.invalidate();
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

                userInfo.put("username", user.getUname());
                userInfo.put("age", MiscUtils.convertBirthdayToAge(user.getBirthday()));
                userInfo.put("gender", user.getGender());
                userInfo.put("isLogin", true);
                userInfo.put("uid", user.getUid());

                json.put("success", true);
                json.put("userInfo", userInfo);
            } else {
                json.put("success", false);
                json.put("error", "你号没了");
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

    @PostMapping(value = "add_appraise")
    @ResponseBody
    public Map<String, Object> addAppraise(@RequestParam("fid") String fid, @RequestParam("appraise") String appraise, HttpSession session) {
        Map<String, Object> json = new HashMap<>();
        try {
            String uid = (String) session.getAttribute("uid");
            json.put("success", userService.addFriendAppraise(uid, fid, appraise));
        } catch (NullPointerException npe) {
            json.put("success", false);
            json.put("error", "bad authorized");
        }
        return json;
    }

    @PostMapping(value = "appraises")
    @ResponseBody
    public Map<String, Object> getAppraises(@RequestParam("uid") String uid, HttpSession session) {
        Map<String, Object> json = new HashMap<>();
        try {
            String _uid = (String) session.getAttribute("uid");
            if(uid.equals(_uid) || relationService.isFriends(uid, _uid)) {
                List<FriendAppraise> appraises = userService.getFriendAppraiseByUid(uid);
                json.put("success", true);
                json.put("appraises", appraises);
            } else {
                json.put("success", false);
            }
        } catch (NullPointerException npe) {
            json.put("success", false);
            json.put("error", "bad authorize");
        }
        return json;
    }
}
