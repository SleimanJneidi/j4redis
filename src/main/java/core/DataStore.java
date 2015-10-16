package core;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Sleiman
 *         <p/>
 *         Date: 16/10/15.
 */
public interface DataStore {

    CompletableFuture<String> get(String key);

    CompletableFuture<Boolean> set(String key,String value);

}
