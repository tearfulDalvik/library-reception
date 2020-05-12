package utils;

import commands.Command;
import commands.impl.StudentLoginCommand;
import model.Student;
import model.User;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.StandardCharsets;

public class ConnectionHandler {
    public static final ByteBuffer REUSABLE_BYTE_BUFFER = ByteBuffer.allocate(1024);
    public static final CharBuffer REUSABLE_CHAR_BUFFER = CharBuffer.allocate(1024);

    public final CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
    public final CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
    public final SegmentedBuffer segmentedBuffer = new SegmentedBuffer();

    public User handle(SocketChannel client, User user) throws Exception {
        REUSABLE_BYTE_BUFFER.clear();
        boolean eof = client.read(REUSABLE_BYTE_BUFFER) == -1;
        REUSABLE_BYTE_BUFFER.flip();
        System.out.println(String.format("read %d bytes to byte-buffer", REUSABLE_BYTE_BUFFER.limit()));

        CoderResult decodeResult;
        do {
            REUSABLE_CHAR_BUFFER.clear();
            decodeResult = decoder.decode(REUSABLE_BYTE_BUFFER, REUSABLE_CHAR_BUFFER, false);
            REUSABLE_CHAR_BUFFER.flip();
            System.out.println(String.format("decoded %d chars from byte-buffer", REUSABLE_CHAR_BUFFER.length()));

            segmentedBuffer.put(REUSABLE_CHAR_BUFFER);
        } while (decodeResult == CoderResult.OVERFLOW);

        if (eof) {
            segmentedBuffer.flush();
        }

        User servedUser = user;
        while (segmentedBuffer.hasNext()) {
            String[] input = segmentedBuffer.next().split(" ");
            try {
                Command cmd = Command.getCommand(servedUser, this, client, input[0], input);
                Object returnValue = cmd.exec();
                if (cmd instanceof StudentLoginCommand && returnValue instanceof Student) {
                    servedUser = (User) returnValue;
                }
                client.write(encoder.encode(CharBuffer.wrap(String.format("200 OK\r\n%s> ", servedUser != null ? "(" + servedUser.getUsername() + ")" : ""))));
            } catch (Exception e) {
                client.write(encoder.encode(CharBuffer.wrap(String.format(e.getMessage() + "\r\n%s> ", servedUser != null ? "(" + servedUser.getUsername() + ")" : ""))));
            }
        }

        if (eof) {
            throw new ClosedChannelException();
        }
        return servedUser;
    }
}