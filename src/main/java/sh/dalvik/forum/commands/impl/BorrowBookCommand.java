package sh.dalvik.forum.commands.impl;

import sh.dalvik.forum.commands.Command;
import sh.dalvik.forum.model.Book;
import sh.dalvik.forum.model.Library;
import sh.dalvik.forum.model.User;
import sh.dalvik.forum.utils.IOBuffer;

@Command.Meta(description = "借书")
@Command.RequiresLogin
public class BorrowBookCommand extends Command {

    private BorrowBookCommand(User u, IOBuffer h, String command, String... args) {
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
        if (bk.lent.contains(getUser().getUsername())) {
            throw new IllegalArgumentException("400 POSSESSED");
        }
        if (bk.totalCount <= 0) {
            throw new IllegalArgumentException("503 LENT OUT");
        }
        bk.lend(getUser().getUsername());
        return null;
    }
}
