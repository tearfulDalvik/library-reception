package sh.dalvik.forum.commands;

public class UnknownCommandException extends Exception {
    public UnknownCommandException(String reason) {
        super(reason);
    }
}