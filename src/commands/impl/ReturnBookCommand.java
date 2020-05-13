package commands.impl;

import commands.Command;
import model.Book;
import model.Library;
import model.User;
import utils.IOBuffer;

import java.nio.channels.SocketChannel;

@Command.Meta(description = "还书")
@Command.RequiresLogin
public class ReturnBookCommand extends Command {

    private ReturnBookCommand(User u, IOBuffer h, String command, String... args) {
        super(u, h, command, args);
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
        if (!bk.lent.contains(getUser().getUsername())) {
            throw new IllegalArgumentException("400 NOT YOURS");
        }
        bk.ret(getUser().getUsername());
        return null;
    }
}
