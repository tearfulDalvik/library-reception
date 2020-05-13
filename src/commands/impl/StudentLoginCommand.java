package commands.impl;

import commands.Command;
import model.Student;
import model.User;
import utils.IOBuffer;

import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;

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
