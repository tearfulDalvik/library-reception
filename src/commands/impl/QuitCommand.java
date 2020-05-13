package commands.impl;

import commands.Command;
import model.User;
import utils.IOBuffer;

import java.nio.channels.SocketChannel;

@Command.Meta(description = "登出并退出")
public class QuitCommand extends Command {
    private QuitCommand(User u, IOBuffer h, String command, String... args) {
        super(u, h, command, args);
    }

    @Override
    public Object exec() throws Exception {
        throw new Exception("Bye");
    }
}
