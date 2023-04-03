package io.ylab.intensive.lesson05.messagefilter;

import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.SQLException;

//@Service
public interface StopWordValidator {
    void loadData(File file);
    String validateText(String word) throws SQLException;
}
