package chat.controller;

import chat.entity.FriendRequest;
import chat.entity.JsonUser;
import chat.entity.User;
import chat.service.RelationService;
import chat.utils.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("friend")
public class RelationController {
    @Autowired
    private RelationService relationService;

    @RequestMapping("get_friend_list")
    @ResponseBody
    public Map<String, Object> queryFriendsById(HttpSession session) {
        Map<String, Object> json = new HashMap<>();
        try {
            String uid = (String) session.getAttribute("uid");
            if(MiscUtils.isEmpty(uid)) {
                throw new NullPointerException();
            }
            List<User> result = relationService.queryRelationOfUid(uid);
            JsonUser[] friends = MiscUtils.packageJsonUser(result);
            json.put("success", true);
            json.put("friend_list", friends);
        } catch (NullPointerException npe) {
            json.put("success", false);
            json.put("error", "bad authorized");
        }
        return json;
    }

    @PostMapping("add_relation")
    @ResponseBody
    public Map<String, Object> addRelation(@RequestParam("fid") String fid, HttpSession session) {
        Map<String, Object> json = new HashMap<>();
        try {
            String uid = (String) session.getAttribute("uid");
            if (relationService.addRelation(uid, fid)) {
                json.put("success", true);
            } else {
                json.put("success", false);
                json.put("error", "something wrong occurred");
            }
        } catch (NullPointerException npe) {
            json.put("success", false);
            json.put("error", "bad authorized");
        }
        return json;
    }

    @PostMapping("del_friend")
    @ResponseBody
    public Map<String, Object> delRelation(@RequestParam("fid") String fid, HttpSession session) {
        Map<String, Object> json = new HashMap<>();
        try {
            String uid = (String) session.getAttribute("uid");
            if(relationService.delRelation(uid, fid)) {
                json.put("success", true);
                relationService.popBothFriendRequest(uid, fid);
            } else {
                json.put("success", false);
                json.put("error", "删除好友失败");
            }
        } catch (NullPointerException npe) {
            json.put("success", false);
            json.put("error", "bad authorized");
        }
        return json;
    }

    @PostMapping("send_friend_request")
    @ResponseBody
    public Map<String, Object> addFriendRequest(@RequestParam("fid") String fid, HttpSession session) {
        Map<String, Object> json = new HashMap<>();
        try {
            String uid = (String) session.getAttribute("uid");
            if(relationService.addFriendRequest(uid, fid)) {
                json.put("success", true);
            } else {
                json.put("success", false);
            }
        } catch (NullPointerException npe) {
            json.put("success", false);
            json.put("error", "bad authorized");
        } catch (DuplicateKeyException dke) {
            json.put("success", false);
            json.put("error", "请勿重复发送请求");
        }
        return json;
    }

    @GetMapping("fetch_friend_request")
    @ResponseBody
    public Map<String, Object> fetchFriendRequest(HttpSession session) {
        Map<String, Object> json = new HashMap<>();
        try {
            String uid = (String) session.getAttribute("uid");
            List<FriendRequest> requests = relationService.getFriendRequestByToUid(uid);
            json.put("success", true);
            json.put("requests", requests);
        } catch (NullPointerException npe) {
            json.put("success", false);
            json.put("error", "bad authorized");
        }
        return json;
    }
}
