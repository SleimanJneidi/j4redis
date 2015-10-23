package core;

import org.junit.rules.ExternalResource;
import redis.embedded.RedisServer;

/**
 * @author Sleiman
 *         <p/>
 *         Date: 22/10/15.
 */
public class RedisServerRule extends ExternalResource{

    public static final int PORT = 9999;

    private RedisServer redisServer;

    @Override
    protected void before() throws Throwable {
        super.before();
        redisServer = new RedisServer(PORT);
        redisServer.start();
    }

    @Override
    protected void after() {
        super.after();
        try {
            redisServer.stop();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
