package commands.impl;

import commands.Command;
import model.Library;
import model.User;
import utils.IOBuffer;

import java.io.IOException;

@Command.Meta(description = "查看可用书籍")
@Command.RequiresLogin
public class ViewBooksCommand extends Command {
    private ViewBooksCommand(User u, IOBuffer h, String command, String... args) {
        super(u, h, command, args);
    }

    @Override
    public Object exec() throws Exception {
        assumeLoggedIn();
        getHandler().write("ID\t\t\tBook\t\t\tAvailability\r\n");
        Library.availableBooks().forEach((id, book) -> {
            try {
                getHandler().write(String.format("%s\t\t\t%s\t\t\t%s\r\n", id, book.getTitle(), book.getTotalCount() > 0 ? "✔" : "✖"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return null;
    }
}
