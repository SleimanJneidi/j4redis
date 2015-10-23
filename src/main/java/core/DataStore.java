package core;

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

    CompletableFuture<Long> decrement(String key);

    CompletableFuture<Boolean> exists(String key);

}
