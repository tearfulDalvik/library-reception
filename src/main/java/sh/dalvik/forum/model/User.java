package sh.dalvik.forum.model;

import org.mindrot.jbcrypt.BCrypt;
import sh.dalvik.forum.pdo.dao.DaoUsers;

import java.sql.ResultSet;

public class User {
    private String username;
    private int uid = -1;
    private boolean loggedIn, admin;

    public User(String username) throws Exception {
        this.username = username;
    }

    public User(int uid) throws Exception {
        ResultSet resultSet = DaoUsers.getInstance().getUserByUid(uid);
        if (resultSet.first()) {
            this.username = resultSet.getString("username");
            this.uid = uid;
        } else {
            throw new IllegalArgumentException("404 NO SUCH USER");
        }
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void logout() {
        loggedIn = false;
    }

    public User login(String password) throws Exception {
        ResultSet resultSet = DaoUsers.getInstance().getUserByName(getUsername());
        if (!resultSet.first() || !BCrypt.checkpw(password, resultSet.getString("password"))) {
            throw new IllegalAccessException("403 FORBIDDEN");
        }
        this.loggedIn = true;
        this.uid = resultSet.getInt("uid");
        return this;
    }

    public void register(String password) throws Exception {
        DaoUsers user = DaoUsers.getInstance();
        ResultSet resultSet = user.getUserByName(getUsername());
        if (isLoggedIn() || resultSet.first()) {
            throw new IllegalStateException("400 User Exists");
        }
        user.newUser(getUsername(), BCrypt.hashpw(password, BCrypt.gensalt()));
    }

    boolean isAdmin() {
        return admin;
    }

    public String getUsername() {
        return username;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}