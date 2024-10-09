package model.entities;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EntityFactory {

  public static Seller instantiateSeller(ResultSet resultSet, Department department) throws SQLException {
    return new Seller(
        resultSet.getInt("Id"),
        resultSet.getString("Name"),
        resultSet.getString("Email"),
        resultSet.getDate("BirthDate"),
        resultSet.getDouble("BaseSalary"),
        department
    );
  }

  public static Department instantiateDepartment(ResultSet resultSet) throws SQLException {
    return new Department(
        resultSet.getInt("Id"),
        resultSet.getString("Name")
    );
  }
}
