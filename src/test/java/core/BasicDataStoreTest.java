package core;

import org.junit.ClassRule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

/**
 * @author Sleiman
 *         <p/>
 *         Date: 20/10/15.
 */
public class BasicDataStoreTest {

    @ClassRule
    public static RedisServerRule redisServerRule = new RedisServerRule();

    private final Connector connector = new Connector("localhost",RedisServerRule.PORT);
    private final DataStore ds = DataStores.simpleDataStore(connector);


    @Test
    public void test_get_existing_key() throws ExecutionException, InterruptedException {
          String expected = "hi";
          ds.set("x",expected).get();
          String actual = ds.get("x").get();
          assertEquals(expected, actual);

    }

    @Test
    public void test_get_non_existing_key() throws Exception{
        CompletableFuture<String> future = ds.get("some_non_existing_key");
        assertNull(future.get());

    }

    @Test
    public void test_get_existing_key_with_converter() throws ExecutionException, InterruptedException {

        ds.set("x","1234").get();
        CompletableFuture<Integer> future = ds.get("x", Integer::parseInt);
        int actual = future.get();
        int expected = 1234;
        assertEquals(expected, actual);

    }

    @Test
    public void test_increment() throws Exception{
        ds.set("x","12").get();
        long actual = ds.increment("x").get();
        assertEquals(13L,actual);
    }

    @Test
    public void test_set_key() throws Exception{

        CompletableFuture<Boolean> future = ds.set("x", "2");
        assertTrue(future.get());

    }
    @Test
    public void test_decrement() throws Exception{
        ds.set("x","12").get();
        long actual = ds.decrement("x").get();
        assertEquals(11L, actual);
    }

    @Test
    public void test_decrement_negative() throws Exception{
        ds.set("x","0").get();
        long actual = ds.decrement("x").get();
        assertEquals(-1L, actual);
    }

    @Test
    public void test_check_if_existing_key_exists()throws Exception{
        ds.set("x","0").get();
        boolean exists = ds.exists("x").get();
        assertTrue(exists);
    }

    @Test
    public void test_check_if_key_exists()throws Exception{
        boolean exists = ds.exists("some_non_existing_key").get();
        assertFalse(exists);
    }


    @Test
    public void test_delete() throws Exception{
        ds.set("key1","x").get();
        ds.set("key2","x").get();
        int count = ds.delete("key1", "key2").get();
        assertEquals(2, count);
    }

    @Test
    public void test_incr_by() throws Exception{
        ds.set("x", "1").get();
        long actual = ds.incrementBy("x", -15).get();
        assertEquals(-14,actual);
    }

    @Test
    public void test_decr_by() throws Exception{
        ds.set("x", "4").get();
        long actual = ds.decrementBy("x", 1).get();
        assertEquals(3,actual);
    }

    @Test
    public void test_lpush() throws Exception{
        ds.delete("listname").get();
        int count = ds.listPush("listname", Arrays.asList("world", "hello")).get();
        assertEquals(2,count);
    }

    @Test
    public void test_lrange() throws Exception{
        ds.delete("listname").get();
        ds.listPush("listname", Arrays.asList("world", "hello")).get();
        String[] actual =ds.listRange("listname", 0, -1).get()
                .stream()
                .toArray(s -> new String[2]);

        assertArrayEquals(new String[]{"hello", "world"}, actual);
    }

    @Test
    public void test_list_length() throws Exception{
        ds.delete("listname").get();
        ds.listPush("listname", Arrays.asList("world", "hello")).get();
        int actual =  ds.listLength("listname").get();
        assertEquals(2, actual);
    }

    @Test
    public void test_list_pop() throws Exception{
        ds.delete("listname").get();
        ds.listPush("listname", Arrays.asList("world", "hello")).get();
        String actual =  ds.listPop("listname").get();
        assertEquals("hello",actual);
    }

    @Test
    public void test_list_pop_null_list() throws Exception{
        ds.delete("listname").get();
        String actual =  ds.listPop("listname").get();
        assertNull(actual);
    }


}
