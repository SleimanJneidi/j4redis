package core;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static core.RESPUtils.*;
import static org.junit.Assert.*;

/**
 * @author Sleiman
 *         <p/>
 *         Date: 11/10/15.
 */
public class RESPUtilsTest {


    @Test
    public void test_can_read_simple_string(){
        ByteBuffer buffer = ByteBuffer.wrap("+simpleString\r\n".getBytes());
        buffer.get(); // reads '+'
        String str = readSimpleString(buffer, StandardCharsets.UTF_8);
        assertEquals("simpleString",str);
    }

    @Test
    public void test_read_complete_bulk_string(){
        ByteBuffer buffer = ByteBuffer.wrap("$11\r\nbulk_String\r\n".getBytes());
        buffer.get(); // reads '$'
        String str = readBulkString(buffer, StandardCharsets.UTF_8);
        assertEquals("bulk_String",str);
    }
    @Test
    public void test_read_null_bulk_string(){
        ByteBuffer buffer = ByteBuffer.wrap("$-1\r\n".getBytes());
        buffer.get(); // reads '$'
        String str = readBulkString(buffer, StandardCharsets.UTF_8);
        assertNull(str);
    }

    @Test
    public void test_read_empty_bulk_string(){
        ByteBuffer buffer = ByteBuffer.wrap("$0\r\n\r\n".getBytes());
        buffer.get(); // reads '$'
        String str = readBulkString(buffer, StandardCharsets.UTF_8);
        assertTrue(str.isEmpty());
    }

    @Test
    public void test_read_array_string(){
        ByteBuffer buffer = ByteBuffer.wrap("*2\r\n$3\r\nfoo\r\n$3\r\nbar\r\n".getBytes());
        buffer.get(); // reads '*'
        List<String> array = readArray(buffer, StandardCharsets.UTF_8);
        assertEquals(2,array.size());
        assertEquals("foo", array.get(0));
        assertEquals("bar", array.get(1));

    }

    @Test
    public void test_read_int(){
        ByteBuffer buffer = ByteBuffer.wrap(":666\r\n".getBytes());
        buffer.get(); // reads ':'
        int val = readInt(buffer);
        assertEquals(666,val);
    }

    @Test
    public void test_read_long(){
        long longVal = 0xFFFFFFFFFFFFFFFL;
        String longAsString = String.valueOf(longVal);
        ByteBuffer buffer = ByteBuffer.wrap((":"+longAsString+"\r\n").getBytes());
        buffer.get(); // reads ':'

        long actualLong = readLong(buffer);

        assertEquals(longVal,actualLong);
    }

    @Test
    public void test_read_negative_int(){
        ByteBuffer buffer = ByteBuffer.wrap(":-666\r\n".getBytes());
        buffer.get(); // reads ':'
        int val = readInt(buffer);
        assertEquals(-666,val);
    }

    @Test
    public void test_add_CtrlF(){
        ByteBuffer buffer = ByteBuffer.allocate(2);
        addCtrlF(buffer);
        buffer.flip();

        assertEquals((byte) '\r', buffer.get());
        assertEquals((byte)'\n',buffer.get());

    }

    @Test
    public void test_read_CtrlF(){
        ByteBuffer buffer = ByteBuffer.allocate(2);
        addCtrlF(buffer);
        buffer.flip();
        readCtrlF(buffer);

        assertTrue(buffer.position()==buffer.limit());
    }





}
