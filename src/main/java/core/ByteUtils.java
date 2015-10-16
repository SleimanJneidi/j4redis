package core;

import java.nio.ByteBuffer;

/**
 * @author Sleiman
 *         <p/>
 *         Date: 16/10/15.
 */
class ByteUtils {
    public static byte[] concat(byte[]first,byte[] second){
        byte[]concat = new byte[first.length+second.length];

        System.arraycopy(first,0,concat,0,first.length);
        System.arraycopy(second,0,concat,first.length,second.length);
        return concat;
    }

    public static byte[] readToLimit(ByteBuffer buffer){
        byte []bytes = new byte[buffer.limit()];
        buffer.get(bytes);
        return bytes;
    }


}
