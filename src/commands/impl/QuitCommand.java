package commands.impl;

import commands.Command;
import model.User;

import java.net.Socket;

@Command.Meta(description = "登出并推出")
public class QuitCommand extends Command {
    private QuitCommand(User u, Socket s, String command, String... args) {
        super(u, s, command, args);
    }

    @Override
    public Object exec() throws Exception {
        throw new Exception("Bye");
    }
}
