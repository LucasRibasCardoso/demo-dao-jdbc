package dao.impl;

import DB.DB;
import DB.DbException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import dao.SellerDao;
import java.util.Map;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

  private Connection connection;

  public SellerDaoJDBC(Connection connection) {
    this.connection = connection;
  }

  private Seller instantiateSeller(ResultSet resultSet, Department department) throws SQLException {
    Seller seller = new Seller();
    seller.setId(resultSet.getInt("Id"));
    seller.setName(resultSet.getString("Name"));
    seller.setEmail(resultSet.getString("Email"));
    seller.setSalary(resultSet.getDouble("BaseSalary"));
    seller.setBirthDate(resultSet.getDate("BirthDate"));
    seller.setDepartment(department);
    return seller;
  }

  @Override
  public void deleteById(int id) {
    PreparedStatement preparedStatement = null;

    try {
      preparedStatement = connection.prepareStatement("DELETE FROM seller WHERE seller.Id = ? ");
      preparedStatement.setInt(1, id);

      int rowsAffected = preparedStatement.executeUpdate();

      if (rowsAffected == 0){
        throw new DbException("Unexpected error! No seller deleted!");
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
  public List<Seller> findAll() {
    PreparedStatement preparedStatement;
    ResultSet resultSet;

    try {
      preparedStatement = connection.prepareStatement(
          "SELECT seller.*,department.Name as DepartmentName " + "FROM seller INNER JOIN department "
              + "ON seller.DepartmentId = department.Id ");
      resultSet = preparedStatement.executeQuery();

      List<Seller> sellers = new ArrayList<>();
      Map<Integer, Department> departmentMap = new HashMap<>();

      while (resultSet.next()) {
        Department dep = departmentMap.get(resultSet.getInt("DepartmentId"));

        if (dep == null) {
          dep =  DepartmentDaoJDBC.instantiateDepartment(resultSet);
          departmentMap.put(resultSet.getInt("DepartmentId"), dep);
        }
        Seller seller = instantiateSeller(resultSet, dep);
        sellers.add(seller);
      }
      return sellers;
    }
    catch (SQLException e) {
      throw new DbException(e.getMessage());
    }
  }

  @Override
  public Seller findById(int id) {
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    try {
      preparedStatement = connection.prepareStatement(
          "SELECT seller.*,department.Name as DepartmentName " + "FROM seller INNER JOIN department "
              + "ON seller.DepartmentId = department.Id " + "WHERE seller.Id = ?");
      preparedStatement.setInt(1, id);
      resultSet = preparedStatement.executeQuery();

      if (resultSet.next()) {
        Department department = DepartmentDaoJDBC.instantiateDepartment(resultSet);
        Seller seller = instantiateSeller(resultSet, department);
        return seller;
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
  public List<Seller> findByDepartment(Department department) {
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    try {
      preparedStatement = connection.prepareStatement(
          "SELECT seller.*, department.Name as DepartmentName " + "FROM seller INNER JOIN department "
              + "ON seller.DepartmentId = department.id " + "WHERE departmentId = ? " + "ORDER BY Name");
      preparedStatement.setInt(1, department.getId());
      resultSet = preparedStatement.executeQuery();

      List<Seller> sellers = new ArrayList<>();
      Map<Integer, Department> departmentMap = new HashMap<>();

      while (resultSet.next()) {
        Department dep = departmentMap.get(resultSet.getInt("DepartmentId"));

        if (dep == null) {
          dep = DepartmentDaoJDBC.instantiateDepartment(resultSet);
          departmentMap.put(resultSet.getInt("DepartmentId"), dep);
        }
        Seller seller = instantiateSeller(resultSet, dep);
        sellers.add(seller);
      }
      return sellers;

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
  public void insert(Seller seller) {
    PreparedStatement preparedStatement = null;

    try {
      preparedStatement = connection.prepareStatement(
          "INSERT INTO seller " + "(Name, Email, BirthDate, BaseSalary, DepartmentId)" + "VALUES "
              + "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setString(1, seller.getName());
      preparedStatement.setString(2, seller.getEmail());
      preparedStatement.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
      preparedStatement.setDouble(4, seller.getSalary());
      preparedStatement.setInt(5, seller.getDepartment()
          .getId());

      int rowsAffected = preparedStatement.executeUpdate();

      if (rowsAffected > 0) {
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        if (resultSet.next()) {
          int id = resultSet.getInt(1);
          seller.setId(id);
        }
        DB.closeResultSet(resultSet);
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
  public void update(Seller seller) {
    PreparedStatement preparedStatement = null;

    try {
      preparedStatement = connection.prepareStatement(
          "UPDATE seller "
          + "Set Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
          + "WHERE Id = ?"
      );

      preparedStatement.setString(1, seller.getName());
      preparedStatement.setString(2, seller.getEmail());
      preparedStatement.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
      preparedStatement.setDouble(4, seller.getSalary());
      preparedStatement.setInt(5, seller.getDepartment().getId());
      preparedStatement.setInt(6, seller.getId());

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
