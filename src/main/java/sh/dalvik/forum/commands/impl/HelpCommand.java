package sh.dalvik.forum.commands.impl;


import sh.dalvik.forum.commands.Command;
import sh.dalvik.forum.model.User;
import sh.dalvik.forum.utils.IOBuffer;

import java.io.IOException;
import java.util.stream.Stream;

public class HelpCommand extends Command {
    /**
     * Mandatory constructor, without any signature changes please!
     *
     * @param u
     * @param command
     * @param args
     */
    protected HelpCommand(User u, IOBuffer h, String command, String... args) {
        super(u, h, command, args);
    }

    @Override
    public Object exec() throws Exception {
        getHandler().write("=============\r\n");
        getHandler().write((String.format("%s\t\t\t%s\t\t\t\t%s\r\n", "Command", "Description", "etc.")));
        Command.table.forEach((i, c) -> {
            try {
                getHandler().write((String.format("%s", i)));
                String metaS = "";
                Meta meta = (Meta) c.getDeclaredAnnotation(Meta.class);
                if (meta != null) {
                    metaS = meta.description();
                }
                getHandler().write((String.format("\t\t\t%s", metaS)));

                Stream.of(c.getDeclaredAnnotations()).forEach(annotation -> {
                    if (annotation.annotationType() == Meta.class) {
                        return;
                    }
                    try {
                        getHandler().write((String.format("\t\t\t\t(%s)", annotation.annotationType().getSimpleName())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                getHandler().write(("\r\n"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        getHandler().write(("=============\r\n"));
        return null;
    }
}
