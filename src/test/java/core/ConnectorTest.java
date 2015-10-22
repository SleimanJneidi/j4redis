package core;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
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

    private static RedisServer redisServer;
    private static final int PORT = 9999;

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
    public void test_connect() throws Exception {
        Connector connector = new Connector("localhost", PORT);
        assertTrue(connector.ping().get());
    }

    @Test(expected = Exception.class)
    public void test_connection_failure() throws Exception {

        Connector connector = new Connector("localhost", -1);
        connector.ping().get();
    }


}
