package core;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

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

    private static EchoServer echoServer;

    @BeforeClass
    public static void setup() throws IOException {
        echoServer = new EchoServer();
        echoServer.start();
    }

    @AfterClass
    public static void teardown() throws Exception {
        echoServer.stop();
    }

    @Test
    public void test_connect() throws Exception {

        Connector connector = new Connector("localhost", 9999);
        CountDownLatch latch = new CountDownLatch(1);

        CompletableFuture<ByteBuffer> execute = connector.execute("cmd".getBytes(), buffer -> buffer);

        execute.thenAccept(buffer -> {

            byte[] replyBytes = new byte[buffer.limit()];
            buffer.get(replyBytes);
            String strValue = new String(replyBytes);

            assertEquals("\r\ncmd\r\n", strValue);
            latch.countDown();
        });
        latch.await(10, TimeUnit.SECONDS);

    }

    @Test
    public void test_connection_failure() throws Exception {

        Connector connector = new Connector("localhost", -1);
        CountDownLatch latch = new CountDownLatch(1);

        CompletableFuture<ByteBuffer> execute = connector.execute("cmd".getBytes(), buffer -> buffer);
        execute.exceptionally(th -> {
            latch.countDown();
            assertNotNull(th);
            return null;
        });

        latch.await(10, TimeUnit.SECONDS);
    }

    static class EchoServer {

        public ServerSocket serverSocket;
        private Thread workerThread;
        private volatile boolean running = true;

        public EchoServer() throws IOException {
            this.serverSocket = new ServerSocket(9999);

        }

        public void start() throws IOException {
            Runnable serverTask = () -> {
                while (running) {
                    try {
                        Socket socket = this.serverSocket.accept();
                        int read;
                        InputStream in = socket.getInputStream();
                        OutputStream out = socket.getOutputStream();

                        while ((read = in.read()) !=-1) {
                            out.write(read);
                        }
                   } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            };

            workerThread = new Thread(serverTask);
            workerThread.start();

        }

        public void stop() throws Exception {
            running = false;
            workerThread.stop();
            this.serverSocket.close();

        }
    }
}
