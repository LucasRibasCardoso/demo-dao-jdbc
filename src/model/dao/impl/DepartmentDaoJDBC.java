package model.dao.impl;


import java.sql.Statement;
import model.dao.DepartmentDao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.entities.Department;
import DB.DbException;
import model.entities.EntityFactory;
import model.utils.ValidationUtils;

public class DepartmentDaoJDBC implements DepartmentDao {

  private Connection connection;

  public DepartmentDaoJDBC(Connection connection) {
    this.connection = connection;
  }


  private List<Department> extractDepartmentList(ResultSet resultSet) throws SQLException {
    List<Department> departments = new ArrayList<>();
    while (resultSet.next()) {
      Department department = EntityFactory.instantiateDepartment(resultSet);
      departments.add(department);
    }
    return departments;
  }

  private Department extractDepartment(ResultSet resultSet) throws SQLException {
    if (resultSet.next()) {
      Department department = EntityFactory.instantiateDepartment(resultSet);
      return department;
    }
    return null;
  }

  private void setGeneratedIdIfPresent(PreparedStatement preparedStatement, Department department)
      throws SQLException {
    try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
      if (resultSet.next()) {
        department.setId(resultSet.getInt(1));
      }
    }
  }


  @Override
  public void deleteById(Integer id) {
    ValidationUtils.validateId(id);

    String query = "DELETE FROM department WHERE Id = ?";

    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, id);

      int rowsAffected = preparedStatement.executeUpdate();
      ValidationUtils.validateUpdateOperation(rowsAffected);
    }
    catch (SQLException e) {
      throw new DbException(e.getMessage());
    }
  }

  @Override
  public List<Department> findAll() {
    String query = "SELECT * FROM department";

    try (PreparedStatement preparedStatement = connection.prepareStatement(
        query); ResultSet resultSet = preparedStatement.executeQuery()) {
      return extractDepartmentList(resultSet);
    }
    catch (SQLException e) {
      throw new DbException(e.getMessage());
    }
  }

  @Override
  public Department findById(Integer id) {
    ValidationUtils.validateId(id);

    String query = "SELECT * FROM department WHERE Id = ?";

    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, id);

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        return extractDepartment(resultSet);
      }
    }
    catch (SQLException e) {
      throw new DbException(e.getMessage());
    }
  }

  @Override
  public void insert(Department department) {
    ValidationUtils.validateDepartmentForInsert(department);

    String query = "INSERT INTO department (Name) VALUES (?)";

    try (PreparedStatement preparedStatement = connection.prepareStatement(query,
        Statement.RETURN_GENERATED_KEYS
    )) {
      preparedStatement.setString(1, department.getName());

      int rowsAffected = preparedStatement.executeUpdate();

      if (rowsAffected > 0){
        setGeneratedIdIfPresent(preparedStatement, department);
      }
      else {
        throw new DbException("Error while inserting department");
      }
    }
    catch (SQLException e) {
      throw new DbException(e.getMessage());
    }
  }

  @Override
  public void update(Department department) {
    ValidationUtils.validateDepartment(department);

    String query = "UPDATE department SET Name = ? WHERE Id = ?";

    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, department.getName());
      preparedStatement.setInt(2, department.getId());

      int rowsAffected = preparedStatement.executeUpdate();
      ValidationUtils.validateUpdateOperation(rowsAffected);
    }
    catch (SQLException | IllegalArgumentException e) {
      throw new DbException(e.getMessage());
    }
  }

}
