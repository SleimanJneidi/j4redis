package core;

import java.util.function.Supplier;

/**
 * @author Sleiman
 *         <p>
 *         Date: 10/10/15.
 */
public class Preconditions {

    public static <T extends Throwable> void  checkArg(boolean expression,Supplier<T> exception) throws T{
        if(!expression){
            throw exception.get();
        }
    }
}
