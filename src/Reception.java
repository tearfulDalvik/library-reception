import commands.Command;
import commands.impl.StudentLoginCommand;
import model.Student;
import model.User;
import sun.reflect.Reflection;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Reception {
    static ArrayList<User> onlineUser = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket();
        ss.bind(new InetSocketAddress("0.0.0.0", 2333));
        System.out.println("Ready.");
        while(true) {
            Socket s = ss.accept();
            User servedUser = null;
            System.out.println("got connection from " + s.getRemoteSocketAddress());
            BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            OutputStream os = s.getOutputStream();
            os.write("Welcome to Dalvik's Library.\r\nType HELP to view help\r\n200 OK\r\n> ".getBytes());
            while (true) {
                try {
                    String[] input = reader.readLine().split(" ");
                    Command cmd = Command.getCommand(servedUser, s, input[0], input);
                    Object returnValue = cmd.exec();
                    if (cmd instanceof StudentLoginCommand && returnValue instanceof Student) {
                        onlineUser.add((User) returnValue);
                        servedUser = (User) returnValue;
                    }
                    os.write("200 OK\r\n> ".getBytes());
                } catch (SocketException ignored) {
                    ignored.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                    os.write((e.getMessage() + "\r\n").getBytes());
                    os.close();
                    break;
                }
            }
        }
    }
}
