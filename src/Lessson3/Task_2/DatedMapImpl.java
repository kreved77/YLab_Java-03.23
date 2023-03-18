package Lessson3.Task_2;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;

public class DatedMapImpl implements DatedMap {

    HashMap<String,String> mapValue = new HashMap<>();
    HashMap<String,Date> mapDate = new HashMap<>();

    public DatedMapImpl() {
    }

    @Override
    public void put(String key, String value) {
        this.mapValue.put(key, value);
        this.mapDate.put(key, new Date());
    }

    @Override
    public String get(String key) {
//        return (this.mapValue.containsKey(key) ? this.mapValue.get(key) : null);
        return (this.mapValue.getOrDefault(key, null));
    }

    @Override
    public boolean containsKey(String key) {
        return this.mapValue.containsKey(key);
    }

    @Override
    public void remove(String key) {
//        if (this.mapValue.containsKey(key)) {
            this.mapValue.remove(key);
            this.mapDate.remove(key);
//        }
    }

    @Override
    public Set<String> keySet() {
        return this.mapValue.keySet();
    }

    @Override
    public Date getKeyLastInsertionDate(String key) {
//        return this.mapDate.get(key);
        return (this.mapDate.getOrDefault(key, null));
    }
}
