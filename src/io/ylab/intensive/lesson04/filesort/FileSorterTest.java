package io.ylab.intensive.lesson04.filesort;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import javax.sql.DataSource;
import javax.xml.crypto.Data;

import io.ylab.intensive.lesson04.DbUtil;

public class FileSorterTest {
  public static void main(String[] args) throws SQLException, IOException {
    DataSource dataSource = initDb();
    File data = new File("data.txt");
//    File data = new Generator().generate("src/io/ylab/intensive/lesson04/filesort/data/source.txt", 1_000_000); // test generator
    System.out.println(new ValidatorDesc(data).isSorted()); // false

    System.out.println("startTime = " + new Time(System.currentTimeMillis()));
    FileSorter fileSorter = new FileSortImpl(dataSource);
    File res = fileSorter.sort(data);
    System.out.println("endTime   = " + new Time(System.currentTimeMillis()));

    System.out.print("Waiting for check:");
    System.out.println(new ValidatorDesc(res).isSorted()); // true
  }
  
  public static DataSource initDb() throws SQLException {
    String createSortTable = "" 
                                 + "drop table if exists numbers;" 
                                 + "CREATE TABLE if not exists numbers (\n"
                                 + "\tval bigint\n"
                                 + ");";
    DataSource dataSource = DbUtil.buildDataSource();
    DbUtil.applyDdl(createSortTable, dataSource);
    return dataSource;
  }
}
