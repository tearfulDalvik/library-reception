package sh.dalvik.forum.commands.impl;

import sh.dalvik.forum.commands.Command;
import sh.dalvik.forum.model.User;
import sh.dalvik.forum.utils.IOBuffer;

@Command.RequiresLogin
public class LogoutCommand extends Command {

    private LogoutCommand(User u, IOBuffer h, String command, String... args) {
        super(u, h, command, args);
    }

    @Override
    public User exec() throws Exception {
        getUser().logout();
        return null;
    }
}
