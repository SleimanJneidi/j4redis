package core;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static core.ByteUtils.*;
/**
 * @author Sleiman
 *         <p/>
 *         Date: 16/10/15.
 */
public class BasicDataStore implements DataStore {
    private final Connector connector;

    public BasicDataStore(Connector connector){
        this.connector = connector;
    }

    @Override
    public CompletableFuture<String> get(String key) {
        Objects.requireNonNull(key);
        byte[] keyBytes = getBytes(key);
        byte[] getCmd = concat(Commands.GET.getBytesPrefix(), keyBytes);
        CompletableFuture<ByteBuffer> future = this.connector.execute(getCmd, Function.identity());
        // remove '$'
        CompletableFuture<String> finalFuture = future.thenApply(buffer -> {
            buffer.get();
            return RESPUtils.readBulkString(buffer, StandardCharsets.UTF_8);
        });
        return finalFuture;
    }

    @Override
    public CompletableFuture<Boolean> set(String key, String value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);

        byte[] keyBytes = getBytes(key+" "+value);
        byte[] getCmd = concat(Commands.SET.getBytesPrefix(), keyBytes);

        CompletableFuture<Boolean> future = this.connector
                .execute(getCmd, ByteUtils::readToLimit)
                .thenApply(array -> Arrays.equals(RESPUtils.OK, array));

        return future;
    }

    @Override
    public CompletableFuture<Long> increment(String key) {
        Objects.requireNonNull(key);
        byte[] keyBytes = getBytes(key);
        byte[] incrCmd = concat(Commands.INCR.getBytesPrefix(), keyBytes);
        CompletableFuture<ByteBuffer> future = this.connector.execute(incrCmd, Function.identity());
        // remove ':'
        CompletableFuture<Long> finalFuture = future.thenApply(buffer -> {
            buffer.get();
            return RESPUtils.readLong(buffer);
        });
        return finalFuture;
    }

    @Override
    public CompletableFuture<Long> decrement(String key) {
        Objects.requireNonNull(key);
        byte[] keyBytes = getBytes(key);
        byte[] decrCmd = concat(Commands.DECR.getBytesPrefix(), keyBytes);
        CompletableFuture<ByteBuffer> future = this.connector.execute(decrCmd, Function.identity());
        // remove ':'
        CompletableFuture<Long> finalFuture = future.thenApply(buffer -> {
            buffer.get();
            return RESPUtils.readLong(buffer);
        });
        return finalFuture;
    }


    private byte[] getBytes(String str){
        return str.getBytes(StandardCharsets.UTF_8);
    }


}
