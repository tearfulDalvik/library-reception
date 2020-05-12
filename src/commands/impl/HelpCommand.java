package commands.impl;

import commands.Command;
import model.User;
import utils.ConnectionHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.Socket;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.util.stream.Stream;

public class HelpCommand extends Command {
    /**
     * Mandatory constructor, without any signature changes please!
     *
     * @param u
     * @param s       socket that is open with input and output stream
     * @param command
     * @param args
     */
    protected HelpCommand(User u, ConnectionHandler h, SocketChannel s, String command, String... args) {
        super(u, h, s, command, args);
    }

    @Override
    public Object exec() throws Exception {
        getSocket().write(getHandler().encoder.encode(CharBuffer.wrap("=============\r\n")));
        getSocket().write(getHandler().encoder.encode(CharBuffer.wrap((String.format("%s\t\t\t%s\t\t\t\t%s\r\n", "Command", "Description", "etc.")))));
        Command.table.forEach((i, c) -> {
            try {
                getSocket().write(getHandler().encoder.encode(CharBuffer.wrap((String.format("%s", i)))));
                String metaS = "";
                Meta meta = (Meta) c.getDeclaredAnnotation(Meta.class);
                if (meta != null) {
                    metaS = meta.description();
                }
                getSocket().write(getHandler().encoder.encode(CharBuffer.wrap((String.format("\t\t\t%s", metaS)))));

                Stream.of(c.getDeclaredAnnotations()).forEach(annotation -> {
                    if (annotation.annotationType() == Meta.class) {
                        return;
                    }
                    try {
                        getSocket().write(getHandler().encoder.encode(CharBuffer.wrap((String.format("\t\t\t\t(%s)", annotation.annotationType().getSimpleName())))));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                getSocket().write(getHandler().encoder.encode(CharBuffer.wrap(("\r\n"))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        getSocket().write(getHandler().encoder.encode(CharBuffer.wrap(("=============\r\n"))));
        return null;
    }
}
