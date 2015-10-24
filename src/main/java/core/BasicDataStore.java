package core;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;

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
    public <T> CompletableFuture<T> get(String key, Function<String,? extends T> converter) {
        CompletableFuture<String> future = this.get(key);
        CompletableFuture<T> convertedFuture = future.thenApply(s -> s == null ? null : converter.apply(s));
        return convertedFuture;
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
    public CompletableFuture<Long> incrementBy(String key, long value) {
        Objects.requireNonNull(key);
        byte[] keyBytes = getBytes(key+" ");
        byte[]valBytes = getBytes(String.valueOf(value));
        byte[]incrCmd = concat(Commands.INCR_BY.getBytesPrefix(), concat(keyBytes, valBytes));
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

    @Override
    public CompletableFuture<Boolean> exists(String key) {
        Objects.requireNonNull(key);
        byte[] keyBytes = getBytes(key);
        byte[] existsCmd = concat(Commands.EXISTS.getBytesPrefix(),keyBytes);

        CompletableFuture<ByteBuffer> future = this.connector.execute(existsCmd, Function.identity());
        // remove ':'
        CompletableFuture<Boolean> finalFuture = future.thenApply(buffer -> {
            buffer.get();
            return RESPUtils.readInt(buffer);
        }).thenApply(i-> i==1);
        return finalFuture;

    }

    @Override
    public CompletableFuture<Integer> delete(String key,String... keys) {

        byte[] keysBytes =  Stream.concat(Stream.of(key),Stream.of(keys))
                            .map(c -> c + " ")
                            .map(this::getBytes)
                            .reduce(ByteUtils::concat).get();

        byte[] deleteCmd = concat(Commands.DEL.getBytesPrefix(), keysBytes);
        CompletableFuture<ByteBuffer> future = this.connector.execute(deleteCmd, Function.identity());
        // reads ':'
        CompletableFuture<Integer> finalFuture = future.thenApply(buffer -> {
            buffer.get();
            return RESPUtils.readInt(buffer);
        });
        return finalFuture;
    }

    @Override
    public CompletableFuture<Integer> listPush(String key, Collection<String> values) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(values);

        byte[] valBytes = Stream.concat(Stream.of(key),values.stream())
                .map(c -> c + " ")
                .map(this::getBytes)
                .reduce(ByteUtils::concat).get();

        byte[] lpushCmd = concat(Commands.LPUSH.getBytesPrefix(), valBytes);
        CompletableFuture<ByteBuffer> future = this.connector.execute(lpushCmd, Function.identity());
        // reads ':'
        CompletableFuture<Integer> finalFuture = future.thenApply(buffer -> {
            buffer.get();
            return RESPUtils.readInt(buffer);
        });
        return finalFuture;
    }

    @Override
    public CompletableFuture<List<String>> listRange(String key, int from, int to) {
        Objects.requireNonNull(key);
        byte[] keyBytes = getBytes(key+" "+String.valueOf(from)+" "+String.valueOf(to));
        byte[]lrangeCmd = concat(Commands.LRANGE.getBytesPrefix(), keyBytes);

        CompletableFuture<ByteBuffer> future = this.connector.execute(lrangeCmd, Function.identity());
        CompletableFuture<List<String>> finalFuture = future.thenApply(buffer -> {
            buffer.get();
            return RESPUtils.readArray(buffer, StandardCharsets.UTF_8);
        });
        return finalFuture;
    }


    private byte[] getBytes(String str){
        return str.getBytes(StandardCharsets.UTF_8);
    }


}
