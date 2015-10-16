package core;

/**
 * @author Sleiman
 *         <p/>
 *         Date: 16/10/15.
 */
public class DataStores {
    public static DataStore simpleDataStore(Connector connector){
        DataStore dataStore = new BasicDataStore(connector);
        return dataStore;
    }
}
