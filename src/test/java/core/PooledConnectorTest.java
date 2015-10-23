package core;

import org.junit.ClassRule;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

/**
 * @author Sleiman
 *         <p/>
 *         Date: 23/10/15.
 */
public class PooledConnectorTest {

    @ClassRule
    public static RedisServerRule redisServerRule = new RedisServerRule();

    @Test
    public void test_connection_pooling(){
        PooledConnector pooledConnector = new PooledConnector("localhost",RedisServerRule.PORT,3);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
            Runnable runnable =()->{
                try {
                    pooledConnector.ping().get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };

            executorService.submit(runnable);
            assertTrue(pooledConnector.currentConnection() <= pooledConnector.getMaxConnection());
        }
        executorService.shutdown();
    }
}
