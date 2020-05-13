package commands.impl;

import commands.Command;
import model.Library;
import model.User;
import utils.ConnectionHandler;

import java.io.IOException;
import java.net.Socket;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;

@Command.Meta(description = "查看可用书籍")
@Command.RequiresLogin
public class ViewBooksCommand extends Command {
    private ViewBooksCommand(User u, ConnectionHandler h, SocketChannel s, String command, String... args) {
        super(u, h, s, command, args);
    }
    
    @Override
    public Object exec() throws Exception {
        assumeLoggedIn();
        getSocket().write(getHandler().encoder.encode(CharBuffer.wrap("ID\t\t\tBook\t\t\tAvailability\r\n")));
        Library.availableBooks().forEach((id, book) -> {
            try {
                getSocket().write(getHandler().encoder.encode(CharBuffer.wrap(String.format("%s\t\t\t%s\t\t\t%s\r\n", id, book.getTitle(), book.getTotalCount() - book.getTotalLent() > 0 ? "✔" : "✖"))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return null;
    }
}