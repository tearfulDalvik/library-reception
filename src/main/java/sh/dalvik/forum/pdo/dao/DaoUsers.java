package sh.dalvik.forum.pdo.dao;

import sh.dalvik.forum.pdo.DBProxy;

import java.sql.ResultSet;

public interface DaoUsers {

    String TABLE = "users";

    static DaoUsers getInstance() {
        return DBProxy.proxy(DaoUsers.class);
    }

    @DBProxy.Query("SELECT * FROM `" + TABLE + "` WHERE `uid`=? LIMIT 1;")
    ResultSet getUserByUid(int uid);

    @DBProxy.Query("SELECT * FROM `" + TABLE + "` WHERE `username`=? LIMIT 1;")
    ResultSet getUserByName(String userName);

    @DBProxy.Query("INSERT INTO `" + TABLE + "` (`username`, `password`) VALUES (?,?);")
    void newUser(String userName, String password);
}
