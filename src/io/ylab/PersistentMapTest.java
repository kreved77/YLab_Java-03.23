package io.ylab;

import io.ylab.intensive.lesson04.persistentmap.PersistenceMapTest;
import io.ylab.intensive.lesson04.persistentmap.PersistentMap;
import io.ylab.intensive.lesson04.persistentmap.PersistentMapImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

@Nested
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersistentMapTest {

    static final String MAP_1 = "Map_1";
    static final String MAP_2 = "Map_2";
    static final String MAP_3 = "Map_3";
    static final String KEY_1 = "Key_1";
    static final String KEY_2 = "Key_2";
    static final String KEY_3 = "Key_3";
    static final String KEY_4 = "Key_4";
    static final String VALUE_1 = "Value_1";
    static final String VALUE_2 = "Value_2";
    static final String VALUE_3 = "Value_3";
    static final String VALUE_4 = "Value_4";

    DataSource dataSource;
    PersistentMap persistentMap;

    @BeforeAll
    void setupData() throws SQLException {
        dataSource = PersistenceMapTest.initDb();
        persistentMap = new PersistentMapImpl(dataSource);

        persistentMap.init(MAP_1);
        persistentMap.put(KEY_1, VALUE_2);
        persistentMap.put(KEY_2, VALUE_2);
        persistentMap.put(KEY_2, VALUE_1); // rewrite

        persistentMap.init(MAP_2);
        persistentMap.put(KEY_2, VALUE_3);
        persistentMap.put(KEY_3, VALUE_3);
        persistentMap.put(KEY_3, VALUE_2); // rewrite

        persistentMap.init(MAP_3);
        persistentMap.put(KEY_3, VALUE_4);
        persistentMap.put(KEY_4, VALUE_4);
        persistentMap.put(KEY_4, VALUE_3); // rewrite
    }

    @Test
    @Order(1)
    void put_test() throws SQLException {

        List<String> resultList = selectAllFromPersistentMap();

        assertThat(resultList).containsExactlyInAnyOrder(
                MAP_1 + KEY_1 + VALUE_2,
                MAP_1 + KEY_2 + VALUE_1,
                MAP_2 + KEY_2 + VALUE_3,
                MAP_2 + KEY_3 + VALUE_2,
                MAP_3 + KEY_3 + VALUE_4,
                MAP_3 + KEY_4 + VALUE_3
        );
    }

    @ParameterizedTest
    @CsvSource({
            "Map_1, Key_1, true",
            "Map_1, Key_2, true",
            "Map_1, Key_3, false",
            "Map_1, Key_4, false",
            "Map_2, Key_1, false",
            "Map_2, Key_2, true",
            "Map_2, Key_3, true",
            "Map_2, Key_4, false",
            "Map_3, Key_1, false",
            "Map_3, Key_2, false",
            "Map_3, Key_3, true",
            "Map_3, Key_4, true"
    })
    @Order(2)
    void contains_test(String map, String key, boolean expectedIsContain) throws SQLException {
        persistentMap.init(map);

        boolean actualIsContain = persistentMap.containsKey(key);

        assertThat(actualIsContain).isEqualTo(expectedIsContain);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "Map_1, Key_1, Value_2",
            "Map_1, Key_2, Value_1",
            "Map_1, Key_3, null",
            "Map_1, Key_4, null",
            "Map_2, Key_1, null",
            "Map_2, Key_2, Value_3",
            "Map_2, Key_3, Value_2",
            "Map_2, Key_4, null",
            "Map_3, Key_1, null",
            "Map_3, Key_2, null",
            "Map_3, Key_3, Value_4",
            "Map_3, Key_4, Value_3"
    }, nullValues = "null")
    @Order(3)
    void get_test(String map, String key, String expectedValue) throws SQLException {
        persistentMap.init(map);

        String actualValue = persistentMap.get(key);

        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @ParameterizedTest
    @CsvSource({
            "Map_1, Key_1 Key_2",
            "Map_2, Key_2 Key_3",
            "Map_3, Key_3 Key_4",
    })
    @Order(4)
    void getKeys_test(String map, String expectedKeys) throws SQLException {
        persistentMap.init(map);

        String actualKeys = persistentMap.getKeys()
                .stream()
                .sorted()
                .reduce((key1, key2) -> key1 + " " + key2)
                .orElseThrow();

        assertThat(actualKeys).isEqualTo(expectedKeys);
    }

    @Test
    @Order(5)
    void remove_test() throws SQLException {
        persistentMap.init(MAP_1);
        persistentMap.remove(KEY_2);
        persistentMap.remove(KEY_3);

        persistentMap.init(MAP_2);
        persistentMap.remove(KEY_3);
        persistentMap.remove(KEY_4);

        persistentMap.init(MAP_3);
        persistentMap.remove(KEY_4);
        persistentMap.remove(KEY_1);

        List<String> resultList = selectAllFromPersistentMap();

        assertThat(resultList).containsExactlyInAnyOrder(
                MAP_1 + KEY_1 + VALUE_2,
                MAP_2 + KEY_2 + VALUE_3,
                MAP_3 + KEY_3 + VALUE_4
        );
    }

    @Test
    @Order(6)
    void clear_test() throws SQLException {
        persistentMap.init(MAP_1);
        persistentMap.clear();

        persistentMap.init(MAP_2);
        persistentMap.clear();

        List<String> resultList = selectAllFromPersistentMap();

        assertThat(resultList).containsExactlyInAnyOrder(MAP_3 + KEY_3 + VALUE_4);
    }

    private List<String> selectAllFromPersistentMap() throws SQLException {
        String sql = "SELECT * FROM persistent_map";

        List<String> resultList = new ArrayList<>(16);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                resultList.add(resultSet.getString("map_name") +
                        resultSet.getString("key") +
                        resultSet.getString("value"));
            }
        }
        return resultList;
    }
}
