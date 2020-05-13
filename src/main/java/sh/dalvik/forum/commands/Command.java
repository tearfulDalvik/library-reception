package sh.dalvik.forum.commands;

import sh.dalvik.forum.commands.impl.*;
import sh.dalvik.forum.model.User;
import sh.dalvik.forum.utils.IOBuffer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

public abstract class Command {

    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequiresLogin {

    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Meta {
        String description() default "";
    }

    public final static HashMap<String, Class> table = new HashMap<>();
    private String command;
    private String[] args;
    private SocketChannel socket;
    private User user;
    private IOBuffer handler;

    static {
        table.put("Q", QueryCommentCommand.class);
        table.put("LOGIN", LoginCommand.class);
        table.put("LOGOUT", LogoutCommand.class);
        table.put("COM", CommentCommand.class);
        table.put("DEL", DeleteCommentCommand.class);
        table.put("REG", RegisterCommand.class);
        table.put("HELP", HelpCommand.class);
        table.put("QUIT", QuitCommand.class);
    }

    public static Command getCommand(User u, IOBuffer h, String command, String... args) throws UnknownCommandException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class cmd = table.get(command.toUpperCase());
        if (cmd == null) {
            throw new UnknownCommandException("400 Unknown Command");
        }
        Constructor a = cmd.getDeclaredConstructor(User.class, IOBuffer.class, String.class, String[].class);
        a.setAccessible(true);
        return (Command) a.newInstance(u, h, command, args);
    }

    /**
     * Mandatory constructor, without any signature changes please!
     */
    protected Command(User u, IOBuffer handler, String command, String... args) {
        if (!handler.getClient().isConnected()) {
            throw new IllegalArgumentException();
        }
        this.command = command;
        this.args = args;
        this.user = u;
        this.handler = handler;
    }

    public abstract Object exec() throws Exception;

    public IOBuffer getHandler() {
        return handler;
    }

    public User getUser() {
        return user;
    }

    public void assumeLoggedIn() throws IllegalAccessException {
        if (getUser() == null || !getUser().isLoggedIn()) {
            throw new IllegalAccessException("403 Forbidden");
        }
    }

    public String[] getArgs() {
        return args;
    }
}