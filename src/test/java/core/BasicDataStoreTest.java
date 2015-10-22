package core;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Sleiman
 *         <p/>
 *         Date: 20/10/15.
 */
public class BasicDataStoreTest {

    private static RedisServer redisServer;
    private static final int PORT = 9999;
    private final Connector connector = new Connector("localhost",PORT);
    private final DataStore ds = DataStores.simpleDataStore(connector);

    @BeforeClass
    public static void setup() throws Exception {
        redisServer = new RedisServer("2.8.9",PORT);
        redisServer.start();

    }

    @AfterClass
    public static void teardown() throws Exception {
        redisServer.stop();
    }

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



}
