import model.User;
import utils.ConnectionHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


public class Reception {
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
                    SocketChannel client = serverSocketChannel.accept();
                    System.out.println("got connection from " + client.getRemoteAddress());
                    client.write(ByteBuffer.wrap("Welcome to Dalvik's Library.\r\nType HELP to view help\r\n200 OK\r\n> ".getBytes()));
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    SocketChannel client = (SocketChannel) key.channel();
                    try {
                        key.attach(new ConnectionHandler().handle(client, (User) key.attachment()));
                    } catch (Exception e) {
                        e.printStackTrace();
                        client.write(ByteBuffer.wrap((e.getMessage() + "\r\n").getBytes()));
                        client.close();
                    }
                }
                keyIterator.remove();
            }
        }

    }
}
