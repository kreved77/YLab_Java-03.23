package io.ylab.intensive.lesson04.persistentmap;

import java.sql.SQLException;
import javax.sql.DataSource;

import io.ylab.intensive.lesson04.DbUtil;

public class PersistenceMapTest {
  public static void main(String[] args) throws SQLException {
    DataSource dataSource = initDb();
    PersistentMap persistentMap = new PersistentMapImpl(dataSource);

    mapGenerate(persistentMap);   // optional generator: Map_1, Map_2, Map_3
                                  // with "key_9"-"value_9", "key_10"-"value_10", "key_11"-"value_11"

    persistentMap.init("Map_1");
    System.out.println(persistentMap.containsKey("key_10"));  // true
    persistentMap.remove("key_10");                       // after remove
    System.out.println(persistentMap.containsKey("key_10"));  // false

    for (String s : persistentMap.getKeys()) {
      System.out.println("RETURN getKeys = " + s);            // key_9, key_11
    }

    System.out.println("==========");
    persistentMap.init("Map_3");
    System.out.println("Map_3 initial value : key_11 -> " + persistentMap.get("key_11"));
    persistentMap.put("key_11", "value_5555");                // updated value
    System.out.println("Map_3 updated value : key_11 -> " + persistentMap.get("key_11"));

    System.out.println("==========");
    persistentMap.init("Map_2");                        // key_9, key_10, key_11
    persistentMap.clear();
    for (String s : persistentMap.getKeys()) {
      System.out.println("RETURN getKeys = " + s);            // none
    }
  }

  private static void mapGenerate(PersistentMap persistentMap) throws SQLException {
    int mapCount = 0;
    while (mapCount++ < 3) {
      persistentMap.init("Map_" + mapCount);
      for (int i = 9; i < 12; i++) {
        persistentMap.put("key_" + i, "value_" + i);
      }
    }
  }

  public static DataSource initDb() throws SQLException {
    String createMapTable = "" 
                                + "drop table if exists persistent_map; "
                                + "CREATE TABLE if not exists persistent_map (\n"
                                + "   map_name varchar,\n"
                                + "   KEY varchar,\n"
                                + "   value varchar\n"
                                + ");";
    DataSource dataSource = DbUtil.buildDataSource();
    DbUtil.applyDdl(createMapTable, dataSource);
    return dataSource;
  }
}
