package commands;

import commands.impl.*;
import model.User;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.HashMap;

public abstract class Command {

    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequiresLogin {

    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Meta
    {
        String description() default "";
    }

    public final static HashMap<String, Class> table = new HashMap<>();
    private String command;
    private String[] args;
    private Socket socket;
    private User user;

    static {
        table.put("LOGIN", StudentLoginCommand.class);
        table.put("BOOKS", ViewBooksCommand.class);
        table.put("DETAIL", BookDetailsCommand.class);
        table.put("LEND", LendBookCommand.class);
        table.put("RETURN", ReturnBookCommand.class);
        table.put("HELP", HelpCommand.class);
        table.put("QUIT", QuitCommand.class);
    }
    public static Command getCommand(User u, Socket s, String command, String... args) throws UnknownCommandException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class cmd = table.get(command.toUpperCase());
        if (cmd == null) {
            throw new UnknownCommandException("Unknown commands.Command");
        }
        Constructor a = cmd.getDeclaredConstructor(User.class, Socket.class, String.class, String[].class);
        a.setAccessible(true);
        return (Command) a.newInstance(u, s, command, args);
    }

    /**
     * Mandatory constructor, without any signature changes please!
     * @param s socket that is open with input and output stream
     */
    protected Command(User u, Socket s, String command, String... args) {
        if (s.isClosed()) {
            throw new IllegalArgumentException();
        }
        this.command = command;
        this.args = args;
        this.socket = s;
        this.user = u;
    }

    public abstract Object exec() throws Exception;

    public Socket getSocket() {
        return socket;
    }

    public User getUser() {
        return user;
    }

    protected void assumeLoggedIn() throws IllegalAccessException {
        if (getUser() == null || !getUser().isLoggedIn()) {
            throw new IllegalAccessException("403 Forbidden");
        }
    }

    public String[] getArgs() {
        return args;
    }
}