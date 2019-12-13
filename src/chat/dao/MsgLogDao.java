package chat.dao;

import chat.entity.MsgLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface MsgLogDao {
    int addLog(@Param("from") String from, @Param("to") String to, @Param("log_time")Timestamp log_time, @Param("msg") String msg);

    List<MsgLog> searchRecentLogByUidAndFid(@Param("uid") String uid, @Param("fid") String fid, @Param("limit") int limit);

    void setStatusRead(@Param("from") String from, @Param("to") String to);

    List<MsgLog> downloadLog(@Param("uid") String uid, @Param("fid") String fid);
}
