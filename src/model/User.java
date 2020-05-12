package model;

public abstract class User {
    private String username;
    private boolean loggedIn;
    User(String username) {
        this.username = username;
    }
    public boolean isLoggedIn() {
        return loggedIn;
    }
    User login(String password) throws IllegalAccessException {
        this.loggedIn = true;
        return this;
    }
    abstract boolean isAdmin();

    public String getUsername() {
        return username;
    }
}