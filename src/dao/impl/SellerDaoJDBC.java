package dao.impl;

import DB.DB;
import DB.DbException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

  private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
    Department department = new Department();
    department.setId(resultSet.getInt("DepartmentID"));
    department.setName(resultSet.getString("DepartmentName"));
    return department;
  }

  @Override
  public void deleteById(int id) {
    PreparedStatement preparedStatement = null;

    try {
      preparedStatement = connection.prepareStatement("DELETE FROM seller WHERE seller.Id = ? ");
      preparedStatement.setInt(1, id);
      preparedStatement.execute();
      System.out.println("Deleted seller successfully");
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
    List<Seller> sellers = new ArrayList<>();

    try {
      preparedStatement = connection.prepareStatement(
          "SELECT seller.*,department.Name as DepartmentName "
              + "FROM seller INNER JOIN department "
              + "ON seller.DepartmentId = department.Id "
      );
      resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        Department department = instantiateDepartment(resultSet);
        Seller seller = instantiateSeller(resultSet, department);
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
          "SELECT seller.*,department.Name as DepartmentName "
              + "FROM seller INNER JOIN department "
              + "ON seller.DepartmentId = department.Id "
              + "WHERE seller.Id = ?"
      );
      preparedStatement.setInt(1, id);
      resultSet = preparedStatement.executeQuery();

      if (resultSet.next()) {
        Department department = instantiateDepartment(resultSet);
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
          "SELECT seller.*, department.Name as DepartmentName "
              + "FROM seller INNER JOIN department "
              + "ON seller.DepartmentId = department.id "
              + "WHERE departmentId = ? "
              + "ORDER BY Name"
      );
      preparedStatement.setInt(1, department.getId());
      resultSet = preparedStatement.executeQuery();

      List<Seller> sellers = new ArrayList<>();
      Map<Integer, Department> departmentMap = new HashMap<>();

      while (resultSet.next()) {
        Department dep = departmentMap.get(resultSet.getInt("DepartmentId"));

        if (dep == null) {
          dep = instantiateDepartment(resultSet);
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
  }

  @Override
  public void update(Seller seller) {

  }

}
