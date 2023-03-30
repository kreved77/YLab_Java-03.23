package io.ylab.intensive.lesson04.movie;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import javax.sql.DataSource;

import io.ylab.intensive.lesson04.DbUtil;

public class MovieTest {
  public static void main(String[] args) throws SQLException {
    DataSource dataSource = initDb();
    MovieLoader movieLoader = new MovieLoaderImpl(dataSource);

    System.out.println("startTime = " + new Time(System.currentTimeMillis()));
//    File dataFile = new File("movies.csv");
    File dataFile = new File("src/io/ylab/intensive/lesson04/movie/data/film.csv");
    movieLoader.loadData(dataFile);
    System.out.println("endTime   = " + new Time(System.currentTimeMillis()));

    /**
     * Тут написать в комментариях запрос получения всех
     *
     * Запрос - вывод подсчета количества записей с группировкой по жанрам (subject):
     * SELECT subject,COALESCE(sum(1),0) AS count FROM movie GROUP BY subject;
     */
  }

  private static DataSource initDb() throws SQLException {
    String createMovieTable = "drop table if exists movie;"
                                  + "CREATE TABLE IF NOT EXISTS movie (\n"
                                  + "\tid bigserial NOT NULL,\n"
                                  + "\t\"year\" int4,\n"
                                  + "\tlength int4,\n"
                                  + "\ttitle varchar,\n"
                                  + "\tsubject varchar,\n"
                                  + "\tactors varchar,\n"
                                  + "\tactress varchar,\n"
                                  + "\tdirector varchar,\n"
                                  + "\tpopularity int4,\n"
                                  + "\tawards bool,\n"
                                  + "\tCONSTRAINT movie_pkey PRIMARY KEY (id)\n"
                                  + ");";
    DataSource dataSource = DbUtil.buildDataSource();
    DbUtil.applyDdl(createMovieTable, dataSource);
    return dataSource;
  }
}
