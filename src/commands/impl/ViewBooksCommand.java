package commands.impl;

import commands.Command;
import model.Library;
import model.User;

import java.io.IOException;
import java.net.Socket;

@Command.Meta(description = "查看可用书籍")
@Command.RequiresLogin
public class ViewBooksCommand extends Command {
    private ViewBooksCommand(User u, Socket s, String command, String... args) {
        super(u, s, command, args);
    }

    @Override
    public Object exec() throws Exception {
        assumeLoggedIn();
        getSocket().getOutputStream().write("BOOK ID\t\t\tBOOK NAME\r\n".getBytes());
        Library.availableBooks().forEach((id, book) -> {
            try {
                getSocket().getOutputStream().write(String.format("%s\t\t\t%s\r\n", id, book.getTitle()).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return null;
    }
}