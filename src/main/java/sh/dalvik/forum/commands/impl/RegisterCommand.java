package sh.dalvik.forum.commands.impl;

import sh.dalvik.forum.commands.Command;
import sh.dalvik.forum.model.User;
import sh.dalvik.forum.utils.IOBuffer;

@Command.Meta(description = "注册新用户")
public class RegisterCommand extends Command {

    private RegisterCommand(User u, IOBuffer h, String command, String... args) {
        super(u, h, command, args);
    }

    @Override
    public Object exec() throws Exception {
        if(getArgs().length != 3) {
            throw new Exception("400 /REG USERNAME PASSWORD");
        }
        new User(getArgs()[1]).register(getArgs()[2]);
        return null;
    }
}
