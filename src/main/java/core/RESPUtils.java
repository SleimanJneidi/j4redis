package core;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sleiman
 *         <p>
 *         Date: 10/10/15.
 */
class RESPUtils {

    public static void addCtrlF(ByteBuffer buffer){
        buffer.put((byte) '\r');
        buffer.put((byte) '\n');
    }
    public static void readCtrlF(ByteBuffer buffer){
        buffer.get();
        buffer.get();
    }
    // + is read
    public static String readSimpleString(ByteBuffer buffer,Charset charset){
        byte[] strBytes = new byte[buffer.limit()-3];
        buffer.get(strBytes);
        String strValue = new String(strBytes,charset);
        readCtrlF(buffer);
        return strValue;
    }
    // $ is read
    public static String readBulkString(ByteBuffer buffer, Charset charset){
        byte firstByte = buffer.get();
        if (firstByte == '-') { // null
            buffer.get(); // -1
            readCtrlF(buffer);
            return null;
        }
        buffer.position(buffer.position() - 1);
        int len = readInt(buffer);

        readCtrlF(buffer);

        byte[] strBytes = new byte[len];
        buffer.get(strBytes);
        readCtrlF(buffer);

        String str = new String(strBytes,charset);

        return str;
    }
    // reads int from buffer, read till '\r' and leaves position at '\r'
    public static int readInt(ByteBuffer buffer){
        int value = 0;
        byte currentByte;
        while ((currentByte = buffer.get()) != '\r') {
            value = value*10 + (currentByte - '0');
        }
        buffer.position(buffer.position() - 1);
        return value;
    }
    // * is read
    public static List<String> readArray(ByteBuffer buffer, Charset charset){
        int arraySize = readInt(buffer);
        readCtrlF(buffer);
        List<String> array = new ArrayList<>(arraySize);
        for (int i = 0; i < arraySize; i++) {
            buffer.get(); // read $
            String str = readBulkString(buffer, charset);
            array.add(str);
        }
        return array;
    }

}
