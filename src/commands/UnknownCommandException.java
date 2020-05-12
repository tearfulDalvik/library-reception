package commands;

public class UnknownCommandException extends Exception {
    public UnknownCommandException(String reason) {
        super(reason);
    }
}