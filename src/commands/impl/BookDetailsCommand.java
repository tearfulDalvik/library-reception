package commands.impl;

import commands.Command;
import model.Book;
import model.Library;
import model.User;
import utils.ConnectionHandler;

import java.net.Socket;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;

@Command.Meta(description = "获取书籍详细信息")
@Command.RequiresLogin
public class BookDetailsCommand extends Command {
    private BookDetailsCommand(User u, ConnectionHandler h, SocketChannel s, String command, String... args) {
        super(u, h, s, command, args);
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
        getSocket().write(getHandler().encoder.encode(CharBuffer.wrap("Feature\t\t\tValue\r\n")));
        getSocket().write(getHandler().encoder.encode(CharBuffer.wrap(String.format("%s\t\t\t%s\r\n", "TITLE", bk.getTitle()))));
        getSocket().write(getHandler().encoder.encode(CharBuffer.wrap(String.format("%s\t\t%s\r\n", "TOTAL LENT", bk.getTotalLent()))));
        getSocket().write(getHandler().encoder.encode(CharBuffer.wrap(String.format("%s\t\t\t%s\r\n", "REMAINS", bk.getTotalCount()))));
        getSocket().write(getHandler().encoder.encode(CharBuffer.wrap(String.format("%s\t\t%s\r\n", "CAPACITY", bk.getTotalCount() + bk.getLent().size()))));
        getSocket().write(getHandler().encoder.encode(CharBuffer.wrap(String.format("%s\t\t\t%s\r\n", "HOLDERS", String.join(", ", bk.getLent())))));
        return bk;
    }
}