package sh.dalvik.forum.commands.impl;

import sh.dalvik.forum.commands.Command;
import sh.dalvik.forum.model.User;
import sh.dalvik.forum.utils.IOBuffer;

@Command.Meta(description = "登出并退出")
public class QuitCommand extends Command {
    private QuitCommand(User u, IOBuffer h, String command, String... args) {
        super(u, h, command, args);
    }

    @Override
    public Object exec() throws Exception {
        getHandler().write("Bye\r\n");
        getHandler().getClient().close();
        return null;
    }
}
