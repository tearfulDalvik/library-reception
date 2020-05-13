package sh.dalvik.forum.model;

public class Student extends User {
    public Student(String username) {
        super(username);
    }

    @Override
    public Student login(String password) throws IllegalAccessException {
        if (!password.equals("TEST")) {
            throw new IllegalAccessException("403 FORBIDDEN");
        }
        super.login(password);
        return this;
    }

    boolean isAdmin() {
        return false;
    }
}
