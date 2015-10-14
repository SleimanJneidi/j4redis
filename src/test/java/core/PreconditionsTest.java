package core;


import org.junit.Test;

/**
 * @author Sleiman
 *         <p/>
 *         Date: 12/10/15.
 */
public class PreconditionsTest{

    @Test(expected = RuntimeException.class)
    public void test_unstatisfied_precondition(){
        Preconditions.checkArg(1==0,RuntimeException::new);
    }

    @Test
    public void test_statisfied_precondition(){
        Preconditions.checkArg(1==1,RuntimeException::new);
    }
}