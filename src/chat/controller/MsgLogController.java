package chat.controller;

import chat.entity.MsgLog;
import chat.service.MsgLogService;
import chat.utils.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("chat")
public class MsgLogController {
    @Autowired
    private MsgLogService msgLogService;

    @PostMapping("send")
    @ResponseBody
    public Map<String, Object> sendMsg(@RequestParam("fid") String fid, @RequestParam("message") String message, HttpSession session) {
        Map<String, Object> json = new HashMap<>();
        try {
            String uid = (String) session.getAttribute("uid");

            Map<String, Object> resp = MiscUtils.packMessageJson(uid, message);

            LocalDateTime now = LocalDateTime.now();
            System.out.println(now);
            //Store message log into database
            if(msgLogService.addLog(uid, fid, now, message)) {
                try {
                    WebsocketController.getWebSocketMap().get(fid).sendObject(resp);
                } catch (NullPointerException npe) {
                    System.out.println("对方不在线");
                }
                json.put("success", true);
            } else {
                json.put("success", false);
            }
        } catch (NullPointerException npe) {
            json.put("success", false);
        }
        return json;
    }

    @PostMapping("pull_log")
    @ResponseBody
    public Map<String, Object> pullMsgLog(@RequestParam("fid") String fid, @RequestParam("logs") int limit, HttpSession session) {
        Map<String, Object> json = new HashMap<>();
        try {
            String uid = (String) session.getAttribute("uid");
            List<MsgLog> logs = msgLogService.pullLogs(uid, fid, limit);
            json.put("success", true);
            json.put("logs", logs);
        } catch (NullPointerException npe) {
            json.put("success", false);
        }
        return json;
    }
}
