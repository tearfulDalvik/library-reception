package commands.impl;

import commands.Command;
import model.Book;
import model.Library;
import model.User;

import java.net.Socket;

@Command.Meta(description = "借书")
@Command.RequiresLogin
public class LendBookCommand extends Command {

    private LendBookCommand(User u, Socket s, String command, String... args) {
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
        if (bk.lent.contains(getUser().getUsername())) {
            throw new IllegalArgumentException("400 YOU CAN ONLY LEND ONCE");
        }
        if (bk.totalCount <= 0) {
            throw new IllegalArgumentException("503 LENT OUT");
        }
        bk.lend(getUser().getUsername());
        return null;
    }
}

