package sh.dalvik.forum.commands.impl;

import sh.dalvik.forum.commands.Command;
import sh.dalvik.forum.model.User;
import sh.dalvik.forum.utils.IOBuffer;

public class LoginCommand extends Command {

    private LoginCommand(User u, IOBuffer h, String command, String... args) {
        super(u, h, command, args);
    }

    @Override
    public User exec() throws Exception {
        if(getArgs().length != 3) {
            throw new Exception("400 /LOGIN USERNAME PASSWORD");
        }
        if (getUser() != null && getUser().isLoggedIn()) {
            throw new Exception("400 ALREADY LOGGED IN");
        }
        User student = new User(getArgs()[1]).login(getArgs()[2]);
        getHandler().write("Welcome: " + student.getUsername() + "\r\n");
        return student;
    }
}
