package sh.dalvik.forum.model;

public class User {
    private String username;
    private boolean loggedIn, admin;

    User(String username) throws Exception {
        this.username = username;
    }
    public boolean isLoggedIn() {
        return loggedIn;
    }
    User login(String password) throws IllegalAccessException {
        this.loggedIn = true;
        return this;
    }

    boolean isAdmin() {
        return admin;
    }

    public String getUsername() {
        return username;
    }
}