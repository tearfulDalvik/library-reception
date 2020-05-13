package sh.dalvik.forum.pdo.dao;

import sh.dalvik.forum.pdo.DBProxy;

public interface DaoUsers {
    default String tableName() {
        return "user";
    }

    default DaoUsers getInstance() {
        return DBProxy.proxy(DaoUsers.class);
    }

    @DBProxy.Query("SELECT * FROM ${TABLE} WHERE `user` = '${userName}' LIMIT 1")
    String getUserByName(String userName);
}
