package sh.dalvik.forum.model;

import sh.dalvik.forum.pdo.dao.DaoComments;

import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Comment {
    public static int FLAG_PUBLIC = 0b01;

    private String title;
    private Date created;
    private User user;
    private int flags = FLAG_PUBLIC, cid = -1;

    public Comment(User u, String content) {
        this(u, content, 0);
    }

    public Comment(User u, String content, int flags) {
        this.user = u;
        this.title = content;
        this.flags = flags;
    }

    public Comment(int cid) throws Exception {
        ResultSet set = DaoComments.getInstance().fetchByCid(cid);
        if (set.first()) {
            this.user = new User(set.getInt("uid"));
            this.title = set.getString("content");
            this.flags = set.getInt("flags");
            this.created = set.getDate("created");
            this.cid = cid;
        } else {
            throw new IllegalArgumentException("404 NOT FOUND");
        }
    }

    public Comment(ResultSet set) throws Exception {
        this.user = new User(set.getInt("uid"));
        this.title = set.getString("content");
        this.flags = set.getInt("flags");
        this.cid = set.getInt("cid");
        this.created = set.getDate("created");
    }

    public void delete() {
        DaoComments.getInstance().deleteByCid(getCid());
    }

    public void save() {
        if (user.isLoggedIn()) {
            DaoComments.getInstance().newComment(getUser().getUid(), getTitle(), getFlags());
        }
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public String getTitle() {
        return title;
    }

    public User getUser() {
        return user;
    }

    public int getCid() {
        return cid;
    }

    public Date getCreated() {
        return created;
    }
}