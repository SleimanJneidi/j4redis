package core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static core.RESPUtils.addCtrlF;

/**
 * @author Sleiman
 *         <p/>
 *         Date: 11/10/15.
 */
public class Connector {

    private final String host;
    private final int port;

    public Connector(String host, int port) {
        this.host = host;
        this.port = port;
    }

    protected  <T> CompletableFuture<T> execute(byte[] cmdBytes, Function<ByteBuffer,? extends T> converter){
        AsynchronousSocketChannel asynchronousSocketChannel = null;
        CompletableFuture<ByteBuffer> future = new CompletableFuture<>();
        try {
            ByteBuffer cmdBuffer = ByteBuffer.allocate(cmdBytes.length + 4);

            addCtrlF(cmdBuffer);
            cmdBuffer.put(cmdBytes);
            addCtrlF(cmdBuffer);
            cmdBuffer.flip();

            asynchronousSocketChannel = AsynchronousSocketChannel.open();
            asynchronousSocketChannel.connect(new InetSocketAddress(this.host, this.port), cmdBuffer, new ConnectionCompletionHandler(asynchronousSocketChannel,future));
        }catch (Throwable t){
            future.completeExceptionally(t);
            if(asynchronousSocketChannel!=null){
                try{
                    asynchronousSocketChannel.close();
                }catch (IOException e){
                    throw new RuntimeException(e);
                }
            }
        }
        CompletableFuture<T> completableFuture = future.thenApply(converter);
        return completableFuture;
    }

    public CompletableFuture<Boolean> ping(){
        CompletableFuture<Boolean> future = execute(Commands.PING.getBytesPrefix(), ByteBuffer::array)
                .thenApply(b -> Arrays.equals(b,RESPUtils.PONG));
        return future;
    }
}
