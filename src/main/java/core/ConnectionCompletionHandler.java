package core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CompletableFuture;

/**
 * @author Sleiman
 *         <p>
 *         Date: 08/10/15.
 */
class ConnectionCompletionHandler implements CompletionHandler<Void,ByteBuffer> {

    private final AsynchronousSocketChannel socketChannel;
    private final CompletableFuture<? super ByteBuffer> future;

    public ConnectionCompletionHandler(AsynchronousSocketChannel socketChannel, CompletableFuture<? super ByteBuffer> future) {
        this.socketChannel = socketChannel;
        this.future = future;
    }

    @Override
    public void completed(Void result, ByteBuffer buffer) {
        this.socketChannel.write(buffer,null,new WriteCompletionHandler(this.socketChannel, future));
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        this.future.completeExceptionally(exc);
        try {
            socketChannel.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
