package core;

import org.junit.*;
import redis.embedded.RedisServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.*;

import static org.junit.Assert.*;

/**
 * @author Sleiman
 *         <p/>
 *         Date: 12/10/15.
 */
public class ConnectorTest {

    @ClassRule
    public static RedisServerRule redisServerRule = new RedisServerRule();

    @Test
    public void test_connect() throws Exception {
        Connector connector = new Connector("localhost",RedisServerRule.PORT);
        assertTrue(connector.ping().get());
    }

    @Test(expected = Exception.class)
    public void test_connection_failure() throws Exception {

        Connector connector = new Connector("localhost", -1);
        connector.ping().get();
    }


}
