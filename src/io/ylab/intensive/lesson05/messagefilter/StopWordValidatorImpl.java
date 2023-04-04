package io.ylab.intensive.lesson05.messagefilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.regex.*;

@Component
public class StopWordValidatorImpl implements StopWordValidator {
    static final String tableName = "words_filter";
    static DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String validateText(String sentence) throws SQLException {
        if (sentence == null || sentence.equals("")) {
            return sentence;
        }

        // **********************************
        // Validates (BULK) all words from input string

        Map<String, Boolean> wordsMap = new HashMap<>();
        for (String word : sentence.split("[ .,:;?!\\n]+")) {
            if (word.length() > 2) {
                wordsMap.put(word, false);
            }
        }

        // check all words: if find -> set value "true"
        findWordsFromMap(wordsMap);

        StringBuilder stringBuilder = new StringBuilder(sentence);

        // iterating the key value pair using for each loop
        for (Map.Entry<String, Boolean> entry : wordsMap.entrySet()) {
            if (entry.getValue()) {
                String badWord = entry.getKey();
                Pattern p = Pattern.compile("\\b" + badWord + "\\b");     // set pattern to find exact separate word
                Matcher m = p.matcher(stringBuilder);
                int countReplace = badWord.length() - 2;                        // count of replace **** (word -> w**d)
                String replacer = badWord.charAt(0) + "*".repeat(countReplace) + badWord.charAt(badWord.length()-1);
                stringBuilder = new StringBuilder(m.replaceAll(replacer));
            }
        }
        return stringBuilder.toString();


        // **********************************
        // Validates single word one-by-one

        /*
        Set<String> setOfWords = new HashSet<>();
        for (String s : sentence.split("[ .,:;?!\\n]+")) {
            setOfWords.add(s);
        }

        StringBuilder stringBuilder = new StringBuilder(sentence);
        Iterator<String> iterator = setOfWords.iterator();
        while (iterator.hasNext()) {
            String word = iterator.next();
            if (word.length() > 2 && findWord(word.toLowerCase())) {
                int countReplace = word.length() - 2;                   // count of replace **** (word -> w**d)

                // v_1 - don't work for repeated same words but longer
//                int lowIndex = stringBuilder.indexOf(word);             // meet at
//                while (lowIndex >= 0) {
//                    stringBuilder.replace(lowIndex+1, lowIndex+1 + countReplace, "*".repeat(countReplace));
//                    lowIndex = stringBuilder.indexOf(word);
//                }

                // v_2 - works fine
                Pattern p = Pattern.compile("\\b" + word + "\\b");
                Matcher m = p.matcher(stringBuilder);
                String replacer = word.charAt(0) + "*".repeat(countReplace) + word.charAt(word.length()-1);
                stringBuilder = new StringBuilder(m.replaceAll(replacer));
            }
        }
        return stringBuilder.toString();
         */
    }

    private boolean findWord(String word) throws SQLException {
        String containsKeyQuery = "SELECT word FROM " + tableName + " WHERE word=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(containsKeyQuery)) {
            preparedStatement.setString(1, word);
            return preparedStatement.executeQuery().next();
        }
    }

    private Map<String, Boolean> findWordsFromMap(Map<String, Boolean> wordsMap) throws SQLException {
        String containsKeyQuery = "SELECT word FROM " + tableName + " WHERE word=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(containsKeyQuery)) {

            for (String s : wordsMap.keySet()) {
                preparedStatement.setString(1, s.toLowerCase());

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    wordsMap.replace(s, true);
                }
                resultSet.close();
            }
        }
        return wordsMap;
    }

    @Override
    public void loadData(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream))) {
            String line;

            List<String> listOfWords = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                listOfWords.add(line);
            }
            createOrClearTableInDB();
            saveWordsToDB(listOfWords);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createOrClearTableInDB() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            java.sql.DatabaseMetaData databaseMetaData = connection.getMetaData();
            try (ResultSet table = databaseMetaData.getTables(null, null, tableName, new String[]{"TABLE"})){

                String querySQL = "";
                if (table.next()) {
                    querySQL = "TRUNCATE TABLE " + tableName + ";";
                    System.out.println("(ServiceMessage: Table \"" + tableName + "\" is cleared.)");
                } else {
                    querySQL = "CREATE TABLE " + tableName + " (\n"
                            + "\tword varchar\n"
                            + ");";
                    System.out.println("(ServiceMessage: Table \"" + tableName + "\" is created.)");
                }

                PreparedStatement preparedStatement = connection.prepareStatement(querySQL);
                preparedStatement.executeUpdate();
                preparedStatement.close();

                table.close();
                connection.close();
            }
        }
    }


    private void saveWordsToDB(List<String> listOfWords) throws SQLException {
        String insertQuery = "insert into " + tableName + " (word) values (?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            for (int i = 0; i < listOfWords.size(); i++) {
                preparedStatement.setString(1, listOfWords.get(i));
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            System.out.println("(ServiceMessage: Table \"" + tableName + "\" is updated.)");
            connection.close();
        }
    }

//    public void executeQuery(DataSource datasource, String query) throws SQLException {
//        try (Connection connection = dataSource.getConnection();
//        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//            preparedStatement.execute();
//        }
//    }
}
