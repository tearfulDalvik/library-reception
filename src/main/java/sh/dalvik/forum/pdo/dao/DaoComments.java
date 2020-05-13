package sh.dalvik.forum.pdo.dao;

import sh.dalvik.forum.pdo.DBProxy;

import java.sql.ResultSet;

public interface DaoComments {

    String TABLE = "comments";

    static DaoComments getInstance() {
        return DBProxy.proxy(DaoComments.class);
    }

    @DBProxy.Query("SELECT * FROM `" + TABLE + "`;")
    ResultSet fetchAll();

    @DBProxy.Query("SELECT * FROM `" + TABLE + "` WHERE `cid`=? LIMIT 1;")
    ResultSet fetchByCid(int cid);

    @DBProxy.Query("DELETE FROM `" + TABLE + "` WHERE `cid`=? LIMIT 1;")
    void deleteByCid(int cid);

    @DBProxy.Query("INSERT INTO `" + TABLE + "` (`uid`, `content`, `flags`) VALUES (?,?,?);")
    void newComment(int uid, String content, int flag);
}
