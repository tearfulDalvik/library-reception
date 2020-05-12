package commands.impl;

import commands.Command;
import model.Book;
import model.Library;
import model.User;

import java.net.Socket;

@Command.Meta(description = "获取书籍详细信息")
@Command.RequiresLogin
public class BookDetailsCommand extends Command {
    private BookDetailsCommand(User u, Socket s, String command, String... args) {
        super(u, s, command, args);
    }

    @Override
    public Object exec() throws Exception {
        assumeLoggedIn();
        if (getArgs().length < 2) {
            throw new IllegalArgumentException("400 ARG 1 SHOULD BE BOOK ID");
        }
        Book bk = Library.availableBooks().get(getArgs()[1]);
        if (bk == null) {
            throw new IllegalArgumentException("404 NOT FOUND");
        }
        getSocket().getOutputStream().write("FEATURE\t\t\tVALUE\r\n".getBytes());
        getSocket().getOutputStream().write(String.format("%s\t\t\t%s\r\n", "TITLE", bk.getTitle()).getBytes());
        getSocket().getOutputStream().write(String.format("%s\t\t%s\r\n", "TOTAL LENT", bk.getTotalLent()).getBytes());
        getSocket().getOutputStream().write(String.format("%s\t\t\t%s\r\n", "REMAINS", bk.getTotalCount()).getBytes());
        getSocket().getOutputStream().write(String.format("%s\t\t%s\r\n", "CAPACITY", bk.getTotalCount() + bk.getLent().size()).getBytes());
        getSocket().getOutputStream().write(String.format("%s\t\t\t%s\r\n", "HOLDERS", String.join(", ", bk.getLent())).getBytes());
        return bk;
    }
}