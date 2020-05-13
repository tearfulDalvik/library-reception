package sh.dalvik.forum.commands.impl;

import sh.dalvik.forum.commands.Command;
import sh.dalvik.forum.model.Comment;
import sh.dalvik.forum.model.User;
import sh.dalvik.forum.utils.IOBuffer;

import static sh.dalvik.forum.model.Comment.FLAG_PUBLIC;

@Command.Meta(description = "删除评论")
@Command.RequiresLogin
public class DeleteCommentCommand extends Command {
    private DeleteCommentCommand(User u, IOBuffer h, String command, String... args) {
        super(u, h, command, args);
    }

    @Override
    public Object exec() throws Exception {
        if (getArgs().length != 2) {
            throw new IllegalArgumentException("400 /DEL CID");
        }
        Comment comment = new Comment(Integer.parseInt(getArgs()[1]));
        if (comment.getUser().getUid() != getUser().getUid()) {
            throw new IllegalAccessException("403 Forbidden");
        }
        comment.delete();
        return null;
    }
}