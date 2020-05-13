package sh.dalvik.forum.commands.impl;

import sh.dalvik.forum.commands.Command;
import sh.dalvik.forum.model.Comment;
import sh.dalvik.forum.model.User;
import sh.dalvik.forum.utils.IOBuffer;

import static sh.dalvik.forum.model.Comment.FLAG_PUBLIC;

@Command.Meta(description = "发表评论")
@Command.RequiresLogin
public class CommentCommand extends Command {
    private CommentCommand(User u, IOBuffer h, String command, String... args) {
        super(u, h, command, args);
    }

    @Override
    public Object exec() throws Exception {
        if (getArgs().length != 2 && getArgs().length != 3) {
            throw new IllegalArgumentException("400 /NEW COMMENT [ISPUBLIC=TRUE|FALSE] = TRUE");
        }
        Comment comment = new Comment(getUser(), getArgs()[1], (getArgs().length == 2 || getArgs()[2].equals("TRUE")) ? FLAG_PUBLIC : 0);
        comment.save();
        return null;
    }
}