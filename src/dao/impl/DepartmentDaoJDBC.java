package dao.impl;

import DB.DB;
import dao.DepartmentDao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.entities.Department;
import DB.DbException;

public class DepartmentDaoJDBC implements DepartmentDao {

  private static Connection connection;

  public DepartmentDaoJDBC(Connection connection) {
    this.connection = connection;
  }

  public static Department instantiateDepartment(ResultSet resultSet) throws SQLException {
    Department department = new Department();
    department.setId(resultSet.getInt("Id"));
    department.setName(resultSet.getString("Name"));
    return department;
  }

  @Override
  public void deleteByID(Integer id) {
    PreparedStatement preparedStatement = null;

    try {
      preparedStatement = connection.prepareStatement("DELETE FROM department WHERE Id = ?");
      preparedStatement.setInt(1, id);

      int rowsAffected = preparedStatement.executeUpdate();

      if (rowsAffected == 0) {
        throw new DbException("Unexpected error! No department deleted!");
      }
    }
    catch (SQLException e) {
      throw new DbException(e.getMessage());

    }
    finally {
      DB.closeStatement(preparedStatement);
    }
  }

  @Override
  public List<Department> findAll() {
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    try {
      List<Department> departments = new ArrayList<>();

      preparedStatement = connection.prepareStatement("SELECT * FROM department");
      resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        Department department = instantiateDepartment(resultSet);
        departments.add(department);
      }
      return departments;
    }
    catch (SQLException e) {
      throw new DbException(e.getMessage());
    }
    finally {
      DB.closeStatement(preparedStatement);
    }
  }

  @Override
  public Department findById(Integer id) {
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    try {
      preparedStatement = connection.prepareStatement("SELECT * FROM department WHERE Id = ?");
      preparedStatement.setInt(1, id);

      resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        Department department = instantiateDepartment(resultSet);
        return department;
      }
      return null;
    }
    catch (SQLException e) {
      throw new DbException(e.getMessage());
    }
    finally {
      DB.closeStatement(preparedStatement);
      DB.closeResultSet(resultSet);
    }
  }

  @Override
  public void insert(Department department) {
    PreparedStatement preparedStatement = null;

    try {
      preparedStatement = connection.prepareStatement(
          "INSERT INTO department " + "(Name) " + "VALUES " + "(?)", Statement.RETURN_GENERATED_KEYS);

      preparedStatement.setString(1, department.getName());

      int rowsAffected = preparedStatement.executeUpdate();

      if (rowsAffected > 0) {
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        if (resultSet.next()) {
          int id = resultSet.getInt(1);
          department.setId(id);
        }
      }
      else {
        throw new DbException("Unexpected error! No rows affected!");
      }
    }
    catch (SQLException e) {
      throw new DbException(e.getMessage());
    }
    finally {
      DB.closeStatement(preparedStatement);
    }
  }

  @Override
  public void update(Department department) {
    PreparedStatement preparedStatement = null;

    try {
      preparedStatement = connection.prepareStatement(
          "UPDATE department " + "SET Name = ? " + "WHERE Id = ?");

      preparedStatement.setString(1, department.getName());
      preparedStatement.setInt(2, department.getId());

      preparedStatement.executeUpdate();
    }
    catch (SQLException e) {
      throw new DbException(e.getMessage());
    }
    finally {
      DB.closeStatement(preparedStatement);
    }
  }

}
