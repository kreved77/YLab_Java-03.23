package Lessson3.Task_2;

import java.util.HashMap;
import java.util.Map;

public class DatedMapTest {
    public static void main(String[] args) {
        DatedMap datedMap = new DatedMapImpl();
        System.out.println(datedMap.keySet() + " (keySet)");
        datedMap.remove("555");

        datedMap.put("1", "one");
        datedMap.put("2", "TWO");
        datedMap.put("3", "three");
        System.out.println(datedMap.keySet() + " (keySet)");
        System.out.println(datedMap.get("1") + "   -> " + datedMap.getKeyLastInsertionDate("1"));
        System.out.println(datedMap.get("2") + "   -> " + datedMap.getKeyLastInsertionDate("2"));
        System.out.println(datedMap.get("3") + " -> " + datedMap.getKeyLastInsertionDate("3"));
        System.out.println(datedMap.get("4") + "  -> " + datedMap.getKeyLastInsertionDate("4") + " (4=empty)\n");

        try {
            Thread.sleep(2023);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        datedMap.put("zero", "zero");
        datedMap.put("2", "two");
        datedMap.remove("3");
        datedMap.put("4", "four");
        System.out.println(datedMap.keySet() + " (keySet)");
        System.out.println(datedMap.get("zero") + " -> " + datedMap.getKeyLastInsertionDate("zero") + " (zero=added)");
        System.out.println(datedMap.get("1") + "  -> " + datedMap.getKeyLastInsertionDate("1"));
        System.out.println(datedMap.get("2") + "  -> " + datedMap.getKeyLastInsertionDate("2") + " (2=updated)");
        System.out.println(datedMap.get("3") + " -> " + datedMap.getKeyLastInsertionDate("3") + " (3=removed)");
        System.out.println(datedMap.get("4") + " -> " + datedMap.getKeyLastInsertionDate("4") + " (4=added)\n");

        System.out.println(datedMap.containsKey("3") + " (containsKey(\"3\") = no)");
        System.out.println(datedMap.containsKey("4") + "  (containsKey(\"4\") = yes)");
    }
}
