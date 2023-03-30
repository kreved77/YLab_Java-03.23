package io.ylab.intensive.lesson04.movie;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
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

      List<Movie> listOfMovies = new ArrayList<>();
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
        listOfMovies.add(movie);
      }
      saveMovie(listOfMovies, dataSource);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }


  private static void saveMovie(List<Movie> listOfMovies, DataSource dataSource) throws SQLException {
    String insertQuery = "insert into movie (year, length, title, subject, actors, actress, director, popularity, awards) " +
            "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

      for (int i = 0; i < listOfMovies.size(); i++) {
        try {
          preparedStatement.setInt(1, listOfMovies.get(i).getYear());
        } catch (NullPointerException nullPointerException) {
          preparedStatement.setNull(1, Types.INTEGER);
        }
        try {
          preparedStatement.setInt(2, listOfMovies.get(i).getLength());
        } catch (NullPointerException nullPointerException) {
          preparedStatement.setNull(2, Types.INTEGER);
        }
        preparedStatement.setString(3, listOfMovies.get(i).getTitle());
        preparedStatement.setString(4, listOfMovies.get(i).getSubject());
        preparedStatement.setString(5, listOfMovies.get(i).getActors());
        preparedStatement.setString(6, listOfMovies.get(i).getActress());
        preparedStatement.setString(7, listOfMovies.get(i).getDirector());
        try {
          preparedStatement.setInt(8, listOfMovies.get(i).getPopularity());
        } catch (NullPointerException nullPointerException) {
          preparedStatement.setNull(8, Types.INTEGER);
        }
        try {
          preparedStatement.setBoolean(9, listOfMovies.get(i).getAwards());
        } catch (NullPointerException nullPointerException) {
          preparedStatement.setNull(9, Types.BOOLEAN);
        }
        preparedStatement.addBatch();
      }
      preparedStatement.executeBatch();
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
