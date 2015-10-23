package core;

import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author Sleiman
 *         <p/>
 *         Date: 22/10/15.
 */
public class PooledConnector extends Connector {

    private final int maxConnection;
    private final Semaphore semaphore;
    private final long timeout;
    private final TimeUnit timeoutUnit;

    public PooledConnector(String host, int port, int maxConnection) {
        this(host,port,maxConnection,1L,TimeUnit.DAYS);
    }

    public PooledConnector(String host,int port,int maxConnection,long timeout,TimeUnit timeoutUnit){
        super(host,port);
        this.maxConnection = maxConnection;
        this.semaphore = new Semaphore(this.maxConnection);
        this.timeout = timeout;
        this.timeoutUnit = timeoutUnit;

    }
    @Override
    protected <T> CompletableFuture<T> execute(byte[] cmdBytes, Function<ByteBuffer, ? extends T> converter) {
        try {
            this.semaphore.tryAcquire(timeout,timeoutUnit);
            CompletableFuture<T> execute = super.execute(cmdBytes, converter);
            this.semaphore.release();
            return execute;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public int currentConnection(){
        int numberOfConnection = Math.abs(maxConnection - availableConnections());
        return numberOfConnection;
    }
    public int getMaxConnection(){
        return maxConnection;
    }

    public int availableConnections(){
        return semaphore.availablePermits();
    }


}
