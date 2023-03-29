package io.ylab.intensive.lesson04.movie;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import javax.sql.DataSource;

public class MovieLoaderImpl implements MovieLoader {
  private DataSource dataSource;

  public MovieLoaderImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void loadData(File file) {
    try (FileInputStream fileInputStream = new FileInputStream(file);
         BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream))) {
      reader.readLine();              // need to skip 2 header lines
      reader.readLine();
      String line;

      while ((line = reader.readLine()) != null) {
        Movie movie = new Movie();
        try {
          String[] data = line.split(";");

          movie.setYear(parseInt(data[0]));
          movie.setLength(parseInt(data[1]));
          movie.setLength(parseInt(data[1]));
          movie.setTitle(data[2]);
          movie.setSubject(data[3]);
          movie.setActors(data[4]);
          movie.setActress(data[5]);
          movie.setDirector(data[6]);
          movie.setPopularity(parseInt(data[7]));
          movie.setAwards(parseBoolean(data[8]));

//          System.out.println(movie.toString());
        } catch (Exception e) {
          throw new RuntimeException("CSV-file error: empty data or wrong format!");
        }

        saveMovie(movie, dataSource);
      }
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }


  private static void saveMovie(Movie movie, DataSource dataSource) throws SQLException {
    String insertQuery = "insert into movie (year, length, title, subject, actors, actress, director, popularity, awards) " +
            "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
      try {
        preparedStatement.setInt(1, movie.getYear());
      } catch (NullPointerException nullPointerException) {
        preparedStatement.setNull(1, Types.INTEGER);
      }
      try {
        preparedStatement.setInt(2, movie.getLength());
      } catch (NullPointerException nullPointerException) {
        preparedStatement.setNull(2, Types.INTEGER);
      }
      try {
        preparedStatement.setString(3, movie.getTitle());
      } catch (NullPointerException nullPointerException) {
        preparedStatement.setNull(3, Types.VARCHAR);
      }
      try {
        preparedStatement.setString(4, movie.getSubject());
      } catch (NullPointerException nullPointerException) {
        preparedStatement.setNull(4, Types.VARCHAR);
      }
      try {
        preparedStatement.setString(5, movie.getActors());
      } catch (NullPointerException nullPointerException) {
        preparedStatement.setNull(5, Types.VARCHAR);
      }
      try {
        preparedStatement.setString(6, movie.getActress());
      } catch (NullPointerException nullPointerException) {
        preparedStatement.setNull(6, Types.VARCHAR);
      }
      try {
        preparedStatement.setString(7, movie.getDirector());
      } catch (NullPointerException nullPointerException) {
        preparedStatement.setNull(7, Types.VARCHAR);
      }
      try {
        preparedStatement.setInt(8, movie.getPopularity());
      } catch (NullPointerException nullPointerException) {
        preparedStatement.setNull(8, Types.INTEGER);
      }
      try {
        preparedStatement.setBoolean(9, movie.getAwards());
      } catch (NullPointerException nullPointerException) {
        preparedStatement.setNull(9, Types.BOOLEAN);
      }

      preparedStatement.executeUpdate();
//      connection.close();
    }
  }


  private Integer parseInt(String number) {
    try {
      return Integer.valueOf(number);
    } catch (Exception e) {
      return null;
    }
  }

  private Boolean parseBoolean(String award) {
    try {
      return award.equals("Yes");
    } catch (Exception e) {
      return null;
    }
  }

}
