package core;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.*;
/**
 * @author Sleiman
 *         <p/>
 *         Date: 16/10/15.
 */
public class ByteUtilsTest {

    @Test
    public void test_byte_concat(){
        byte[] first = {'P','I','N','G'};
        byte[] second = {0xA,0xB,0xC,0xF};

        byte[] concat = ByteUtils.concat(first,second);
        byte[] expected = {'P','I','N','G',0xA,0xB,0xC,0xF};

        assertArrayEquals(expected,concat);
    }

    @Test
    public void test_read_all_bytes(){
        ByteBuffer buffer = ByteBuffer.allocate(10);
        byte[] actual = {0xF,0xA,23};
        buffer.put(actual);
        buffer.flip();

        byte[]expected = ByteUtils.readToLimit(buffer);
        assertArrayEquals(expected,actual);

    }
}