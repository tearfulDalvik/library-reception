package sh.dalvik.forum.commands.impl;

import sh.dalvik.forum.commands.Command;
import sh.dalvik.forum.model.Student;
import sh.dalvik.forum.model.User;
import sh.dalvik.forum.utils.IOBuffer;

public class StudentLoginCommand extends Command {

    private StudentLoginCommand(User u, IOBuffer h, String command, String... args) {
        super(u, h, command, args);
    }

    @Override
    public User exec() throws Exception {
        if(getArgs().length != 3) {
            throw new Exception("400 /LOGIN username password");
        }
        Student student = new Student(getArgs()[1]).login(getArgs()[2]);
        getHandler().write("Welcome: " + student.getUsername() + "\r\n");
        return student;
    }
}
