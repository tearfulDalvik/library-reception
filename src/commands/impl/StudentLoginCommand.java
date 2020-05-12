package commands.impl;

import commands.Command;
import model.Student;
import model.User;
import utils.ConnectionHandler;
import utils.SegmentedBuffer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.CoderResult;

import static utils.ConnectionHandler.REUSABLE_BYTE_BUFFER;
import static utils.ConnectionHandler.REUSABLE_CHAR_BUFFER;

public class StudentLoginCommand extends Command {

    private StudentLoginCommand(User u, ConnectionHandler handler, SocketChannel s, String command, String... args) {
        super(u, handler, s, command, args);
    }

    @Override
    public User exec() throws Exception {
        if(getArgs().length != 3) {
            throw new Exception("400 /LOGIN username password");
        }
        Student student = new Student(getArgs()[1]).login(getArgs()[2]);
        getSocket().write(getHandler().encoder.encode(CharBuffer.wrap("Welcome: " + student.getUsername() + "\r\n")));
        return student;
    }
}
