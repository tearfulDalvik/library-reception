package commands.impl;

import commands.Command;
import model.User;
import utils.ConnectionHandler;

import java.net.Socket;
import java.nio.channels.SocketChannel;

@Command.Meta(description = "登出并退出")
public class QuitCommand extends Command {
    private QuitCommand(User u, ConnectionHandler h, SocketChannel s, String command, String... args) {
        super(u, h, s, command, args);
    }

    @Override
    public Object exec() throws Exception {
        throw new Exception("Bye");
    }
}
