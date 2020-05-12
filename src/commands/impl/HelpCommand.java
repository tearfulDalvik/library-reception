package commands.impl;

import commands.Command;
import model.User;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.Socket;
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
    protected HelpCommand(User u, Socket s, String command, String... args) {
        super(u, s, command, args);
    }

    @Override
    public Object exec() throws Exception {
        OutputStream os = getSocket().getOutputStream();
        os.write("=============\r\n".getBytes());
        os.write(String.format("%s\t\t\t%s\t\t\t\t%s\r\n", "Command", "Description", "etc.").getBytes());
        Command.table.forEach((i, c) -> {
            try {
                os.write(String.format("%s", i).getBytes());
                String metaS = "";
                Meta meta = (Meta) c.getDeclaredAnnotation(Meta.class);
                if (meta != null) {
                    metaS = meta.description();
                }
                os.write(String.format("\t\t\t%s", metaS).getBytes());

                Stream.of(c.getDeclaredAnnotations()).forEach(annotation -> {
                    if (annotation.annotationType() == Meta.class) {
                        return;
                    }
                    try {
                        os.write(String.format("\t\t\t\t(%s)", annotation.annotationType().getSimpleName()).getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                os.write("\r\n".getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        os.write("=============\r\n".getBytes());
        return null;
    }
}
