package sh.dalvik.forum.commands.impl;

import sh.dalvik.forum.commands.Command;
import sh.dalvik.forum.model.Book;
import sh.dalvik.forum.model.Library;
import sh.dalvik.forum.model.User;
import sh.dalvik.forum.utils.IOBuffer;

@Command.Meta(description = "获取书籍详细信息")
@Command.RequiresLogin
public class BookDetailsCommand extends Command {
    private BookDetailsCommand(User u, IOBuffer h, String command, String... args) {
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
        getHandler().write("Feature\t\t\tValue\r\n");
        getHandler().write(String.format("%s\t\t\t%s\r\n", "TITLE", bk.getTitle()));
        getHandler().write(String.format("%s\t\t%s\r\n", "TOTAL LENT", bk.getTotalLent()));
        getHandler().write(String.format("%s\t\t\t%s\r\n", "REMAINS", bk.getTotalCount()));
        getHandler().write(String.format("%s\t\t%s\r\n", "CAPACITY", bk.getTotalCount() + bk.getLent().size()));
        getHandler().write(String.format("%s\t\t\t%s\r\n", "HOLDERS", String.join(", ", bk.getLent())));
        return bk;
    }
}