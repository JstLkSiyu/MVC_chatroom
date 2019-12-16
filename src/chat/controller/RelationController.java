package chat.controller;

import chat.entity.FriendRequest;
import chat.entity.JsonUser;
import chat.entity.User;
import chat.service.RelationService;
import chat.service.UserService;
import chat.utils.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/friend")
public class RelationController {
    @Autowired
    private RelationService relationService;
    @Autowired
    private UserService userService;

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
                Map<String, Object> makeFriendRequest = new HashMap<>();
                makeFriendRequest.put("type", WebsocketController.msgType.SYSTEM);
                makeFriendRequest.put("msg", "您收到一条添加好友请求");
                try {
                    WebsocketController.getWebSocketMap().get(fid).sendObject(makeFriendRequest);
                } catch (NullPointerException npe) {
                    //Ignore
                }
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

    @GetMapping("get_got_request")
    @ResponseBody
    public Map<String, Object> getGotRequest(HttpSession session) {
        Map<String, Object> json = new HashMap<>();
        try {
            String uid = (String) session.getAttribute("uid");
            List<FriendRequest> result = relationService.getFriendRequestByToUid(uid);
            List<Object> requests = new ArrayList<>();
            for(FriendRequest friendRequest : result) {
                Map<String, Object> request = new HashMap<>();
                request.put("uid", friendRequest.getFrom());
                request.put("uname", friendRequest.getUser().getUname());
                request.put("age", MiscUtils.convertBirthdayToAge(friendRequest.getUser().getBirthday()));
                request.put("gender", friendRequest.getUser().getGender());
                request.put("status", friendRequest.getStatus());
                requests.add(request);
            }
            json.put("success", true);
            json.put("requests", requests);
        } catch (NullPointerException npe) {
            json.put("success", false);
            json.put("error", "bad authorized");
        }
        return json;
    }

    @GetMapping("get_sent_request")
    @ResponseBody
    public Map<String, Object> getSentRequest(HttpSession session) {
        Map<String, Object> json = new HashMap<>();
        try {
            String uid = (String) session.getAttribute("uid");
            List<FriendRequest> result = relationService.getFriendRequestByFromUid(uid);
            List<Object> requests = new ArrayList<>();
            for(FriendRequest friendRequest : result) {
                Map<String, Object> request = new HashMap<>();
                request.put("uid", friendRequest.getTo());
                request.put("uname", friendRequest.getUser().getUname());
                request.put("age", MiscUtils.convertBirthdayToAge(friendRequest.getUser().getBirthday()));
                request.put("gender", friendRequest.getUser().getGender());
                request.put("status", friendRequest.getStatus());
                requests.add(request);
            }
            json.put("success", true);
            json.put("requests", requests);
        } catch (NullPointerException npe) {
            json.put("success", false);
            json.put("error", "bad authorized");
        }
        return json;
    }

    @PostMapping("reject_friend_request")
    @ResponseBody
    public Map<String, Object> rejectFriendRequest(@RequestParam("fid") String fid, HttpSession session) {
        Map<String, Object> json = new HashMap<>();
        try {
            String uid = (String) session.getAttribute("uid");
            String uname = (String) session.getAttribute("uname");
            json.put("success", relationService.changeStatus(fid, uid, "reject"));
            Map<String, Object> requestResponse = new HashMap<>();
            requestResponse.put("type", WebsocketController.msgType.SYSTEM);
            requestResponse.put("msg", uname + "拒绝了您的好友请求");
            try {
                WebsocketController.getWebSocketMap().get(fid).sendObject(requestResponse);
            } catch (NullPointerException npe) {
                //Ignore
            }
        } catch (NullPointerException npe) {
            json.put("success", false);
            json.put("error", "bad authorized");
        }
        return json;
    }

    @PostMapping("agree_friend_request")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> agreeFriendRequest(@RequestParam("fid") String fid, HttpSession session) throws Exception {
        Map<String, Object> json = new HashMap<>();
        try {
            String uid = (String) session.getAttribute("uid");
            String uname = (String) session.getAttribute("uname");
            if(relationService.changeStatus(fid, uid, "agree") && addRelation(uid, fid)) {
                json.put("success", true);
                Map<String, Object> requestResponse = new HashMap<>();
                requestResponse.put("type", WebsocketController.msgType.AGREE_REQ);
                requestResponse.put("friend", MiscUtils.packageJsonUser(userService.getUserInfoByUid(uid))[0]);
                try {
                    WebsocketController.getWebSocketMap().get(fid).sendObject(requestResponse);
                } catch (NullPointerException npe) {
                    //Ignore
                }
            } else {
                throw new Exception();
            }
        } catch (NullPointerException npe) {
            json.put("success", false);
            json.put("error", "bad authorized");
        }
        return json;
    }

    @PostMapping("delete_got_request")
    @ResponseBody
    public Map<String, Object> deleteGotRequest(@RequestParam("fid") String fid, HttpSession session) {
        Map<String, Object> json = new HashMap<>();
        try {
            String uid = (String) session.getAttribute("uid");
            json.put("success", relationService.popFriendRequest(fid, uid));
        } catch (NullPointerException npe) {
            json.put("success", false);
            json.put("error", "bad authorized");
        }
        return json;
    }

    @PostMapping("delete_sent_request")
    @ResponseBody
    public Map<String, Object> deleteSentRequest(@RequestParam("fid") String fid, HttpSession session) {
        Map<String, Object> json = new HashMap<>();
        try {
            String uid = (String) session.getAttribute("uid");
            json.put("success", relationService.popFriendRequest(uid, fid));
        } catch (NullPointerException npe) {
            json.put("success", false);
            json.put("error", "bad authorized");
        }
        return json;
    }

    private boolean addRelation(String uid, String fid) {
        return relationService.addRelation(uid, fid);
    }
}
