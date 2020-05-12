import commands.Command;
import commands.impl.StudentLoginCommand;
import model.Student;
import model.User;
import sun.reflect.Reflection;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.stream.Collectors;


public class Reception {
    static HashMap<Socket, User> onlineUser = new HashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress("0.0.0.0", 2333));
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("Ready.");
        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                if (key.isAcceptable()) {
                    SocketChannel channel = serverSocketChannel.accept();
                    Socket s = channel.socket();
                    System.out.println("got connection from " + s.getRemoteSocketAddress());
                    onlineUser.put(s, null);
                    OutputStream os = s.getOutputStream();
                    os.write("Welcome to Dalvik's Library.\r\nType HELP to view help\r\n200 OK\r\n> ".getBytes());
                    channel.configureBlocking(false);
                    channel.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    Socket s = channel.socket();

                    User servedUser = onlineUser.get(s);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    OutputStream os = s.getOutputStream();
                    while (true) {
                        try {
                            String[] input = reader.readLine().split(" ");
                            Command cmd = Command.getCommand(servedUser, s, input[0], input);
                            Object returnValue = cmd.exec();
                            if (cmd instanceof StudentLoginCommand && returnValue instanceof Student) {
                                servedUser = (User) returnValue;
                                onlineUser.replace(s, servedUser);
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
                keyIterator.remove();
            }
        }

    }
}
