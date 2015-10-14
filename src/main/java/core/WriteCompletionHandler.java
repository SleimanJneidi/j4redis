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
class WriteCompletionHandler implements CompletionHandler<Integer,ByteBuffer> {

    private final AsynchronousSocketChannel socketChannel;
    private final CompletableFuture<? super ByteBuffer> future;

    public WriteCompletionHandler(AsynchronousSocketChannel socketChannel, CompletableFuture<? super ByteBuffer> future) {
        this.socketChannel = socketChannel;
        this.future = future;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        if(result != -1){
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            this.socketChannel.read(buffer,null, new ReadCompletionHandler(this.socketChannel,buffer, future));
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        this.future.completeExceptionally(exc);
        try{
            this.socketChannel.close();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
