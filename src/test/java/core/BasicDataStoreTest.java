package core;

import org.junit.ClassRule;
import org.junit.Test;
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




}
