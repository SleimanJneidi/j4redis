package core;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * @author Sleiman
 *         <p/>
 *         Date: 16/10/15.
 */
public interface DataStore {

    CompletableFuture<String> get(String key);

    <T> CompletableFuture<T> get(String key,Function<String,? extends T> converter);

    CompletableFuture<Boolean> set(String key,String value);

    CompletableFuture<Long> increment(String key);

    CompletableFuture<Long> incrementBy(String key,long value);

    CompletableFuture<Long> decrement(String key);

    CompletableFuture<Long> decrementBy(String key,long value);

    CompletableFuture<Boolean> exists(String key);

    CompletableFuture<Integer> delete(String key,String... keys);

    CompletableFuture<Integer> listPush(String key,Collection<String> values);

    CompletableFuture<List<String>> listRange(String key,int from,int to);

}
