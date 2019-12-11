package chat.service;

import chat.dao.MsgLogDao;
import chat.entity.MsgLog;
import chat.utils.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class MsgLogService {
    @Autowired
    private MsgLogDao msgLogDao;

    public boolean addLog(String from, String to, LocalDateTime log_time, String msg) {
        return msgLogDao.addLog(from, to, MiscUtils.convertLocalDateTimeToTimestamp(log_time), msg) > 0;
    }

    public List<MsgLog> pullLogs(String uid, String fid, int limit) {
        //msgLogDao.setStatusRead(fid, uid);
        return msgLogDao.searchRecentLogByUidAndFid(uid, fid, limit);
    }

}
