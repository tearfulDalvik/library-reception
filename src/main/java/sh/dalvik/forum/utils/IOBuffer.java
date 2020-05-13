package sh.dalvik.forum.utils;

import sh.dalvik.forum.commands.Command;
import sh.dalvik.forum.commands.impl.LoginCommand;
import sh.dalvik.forum.model.User;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.StandardCharsets;

public class IOBuffer {
    private static final ByteBuffer REUSABLE_BYTE_BUFFER = ByteBuffer.allocate(1024);
    private static final CharBuffer REUSABLE_CHAR_BUFFER = CharBuffer.allocate(1024);

    private final CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
    private final CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
    private final SegmentedBuffer segmentedBuffer = new SegmentedBuffer();

    private SocketChannel client;

    User servedUser = null;

    public void handle(SocketChannel client) throws Exception {
        this.client = client;

        REUSABLE_BYTE_BUFFER.clear();
        boolean eof = client.read(REUSABLE_BYTE_BUFFER) == -1;
        REUSABLE_BYTE_BUFFER.flip();
        System.out.println(String.format("read %d bytes to buffer", REUSABLE_BYTE_BUFFER.limit()));

        CoderResult decodeResult;
        do {
            REUSABLE_CHAR_BUFFER.clear();
            decodeResult = decoder.decode(REUSABLE_BYTE_BUFFER, REUSABLE_CHAR_BUFFER, false);
            REUSABLE_CHAR_BUFFER.flip();
            System.out.println(String.format("decoded %d chars from buffer", REUSABLE_CHAR_BUFFER.length()));

            segmentedBuffer.put(REUSABLE_CHAR_BUFFER);
        } while (decodeResult == CoderResult.OVERFLOW);

        if (eof) {
            segmentedBuffer.flush();
        }

        while (segmentedBuffer.hasNext()) {
            String[] input = segmentedBuffer.next().split(" ");
            try {
                Command cmd = Command.getCommand(servedUser, this, input[0], input);
                if(cmd.getClass().getDeclaredAnnotation(Command.RequiresLogin.class) != null) {
                    cmd.assumeLoggedIn();
                }
                Object returnValue = cmd.exec();
                if (returnValue instanceof User) {
                    servedUser = (User) returnValue;
                }
                client.write(encoder.encode(CharBuffer.wrap(String.format("200 OK\r\n%s> ", (servedUser != null && servedUser.isLoggedIn()) ? "(" + servedUser.getUsername() + ")" : ""))));
            } catch (Exception e) {
                e.printStackTrace();
                client.write(encoder.encode(CharBuffer.wrap(String.format(e.getMessage() + "\r\n%s> ", (servedUser != null && servedUser.isLoggedIn()) ? "(" + servedUser.getUsername() + ")" : ""))));
            }
        }

        if (eof) {
            throw new ClosedChannelException();
        }
    }

    public void write(CharSequence thing) throws IOException {
        getClient().write(encoder.encode(CharBuffer.wrap(thing)));
    }

    public SocketChannel getClient() {
        return client;
    }
}