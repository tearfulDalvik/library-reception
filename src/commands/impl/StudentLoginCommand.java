package commands.impl;

import commands.Command;
import model.Student;
import model.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class StudentLoginCommand extends Command {

    private StudentLoginCommand(User u, Socket s, String command, String... args) {
        super(u, s, command, args);
    }

    @Override
    public User exec() throws Exception {
        getSocket().getOutputStream().write("201 USERNAME\r\n".getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(getSocket().getInputStream()));
        Student stu = new Student(reader.readLine());
        getSocket().getOutputStream().write("201 PASSWORD\r\n".getBytes());
        Student student = stu.login(reader.readLine());
        getSocket().getOutputStream().write(("Welcome: " + student.getUsername() + "\r\n").getBytes());
        return student;
    }
}
