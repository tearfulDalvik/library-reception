package sh.dalvik.forum.commands.impl;

import sh.dalvik.forum.commands.Command;
import sh.dalvik.forum.model.Comment;
import sh.dalvik.forum.model.User;
import sh.dalvik.forum.pdo.dao.DaoComments;
import sh.dalvik.forum.utils.IOBuffer;

import java.sql.ResultSet;

import static sh.dalvik.forum.model.Comment.FLAG_PUBLIC;

@Command.RequiresLogin
@Command.Meta(description = "查看所有评论")
public class QueryCommentCommand extends Command {
    private QueryCommentCommand(User u, IOBuffer h, String command, String... args) {
        super(u, h, command, args);
    }

    @Override
    public Object exec() throws Exception {
        ResultSet resultSet = DaoComments.getInstance().fetchAll();
        getHandler().write("======");
        while (resultSet.next()) {
            Comment comment = new Comment(resultSet);
            User user = comment.getUser();
            if (comment.getFlags() != FLAG_PUBLIC && user.getUid() != getUser().getUid()) {
                continue;
            }
            getHandler().write("\r\n" + comment.getTitle());
            getHandler().write("\r\n\r\nComment ID: " + comment.getCid() + "\r\nCreated: " + comment.getCreated().toLocalDate().toString() + "\r\nBy: " + user.getUsername() + "\r\n======\r\n");
        }
        return null;
    }
}
