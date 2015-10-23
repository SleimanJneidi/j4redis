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
 class ReadCompletionHandler implements CompletionHandler<Integer,ByteBuffer> {

    private final AsynchronousSocketChannel socketChannel;
    private final ByteBuffer buffer;
    private final CompletableFuture<? super ByteBuffer> future;

    public ReadCompletionHandler(AsynchronousSocketChannel socketChannel, ByteBuffer buffer, CompletableFuture<? super ByteBuffer> future) {
        this.socketChannel = socketChannel;
        this.buffer = buffer;
        this.future = future;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        buffer.flip();
        future.complete(buffer);
        try {
            this.socketChannel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
