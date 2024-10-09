package model.dao.impl;

import DB.DbException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import java.util.Map;
import model.entities.Department;
import model.entities.EntityFactory;
import model.entities.Seller;
import model.utils.ValidationUtils;

public class SellerDaoJDBC implements SellerDao {

  // Atributos
  private Connection connection;
  private DepartmentDao departmentDao;

  // Construtor
  public SellerDaoJDBC(Connection connection, DepartmentDao departmentDao) {
    this.connection = connection;
    this.departmentDao = departmentDao;
  }


  // Funcionalidades
  private Department findOrCreateDepartmentFromCache(
      Integer departmentId,
      Map<Integer, Department> departmentCache
  ) {

    if (!departmentCache.containsKey(departmentId)) {
      Department department = departmentDao.findById(departmentId);
      departmentCache.put(departmentId, department);
    }
    return departmentCache.get(departmentId);
  }

  private List<Seller> extractSellerList(ResultSet resultSet) throws SQLException {
    Map<Integer, Department> departmentsCache = new HashMap<>();
    List<Seller> sellers = new ArrayList<>();

    while (resultSet.next()) {
      int departmentId = resultSet.getInt("DepartmentId");
      Department department = findOrCreateDepartmentFromCache(departmentId, departmentsCache);
      Seller seller = EntityFactory.instantiateSeller(resultSet, department);
      sellers.add(seller);
    }
    return sellers;
  }

  private Seller extractSeller(ResultSet resultSet) throws SQLException {
    if (resultSet.next()) {
      Department department = EntityFactory.instantiateDepartment(resultSet);
      Seller seller = EntityFactory.instantiateSeller(resultSet, department);
      return seller;
    }
    return null;
  }

  private void extractGeneratedId(PreparedStatement preparedStatement, Seller seller) throws SQLException {
    try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
      if (resultSet.next()) {
        int id = resultSet.getInt(1);
        seller.setId(id);
      }
    }
  }


  @Override
  public void deleteById(Integer id) {
    String query = "DELETE FROM seller WHERE seller.Id = ? ";

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
  public List<Seller> findAll() {

    String query = """
        SELECT seller.*,department.Name as DepartmentName \
        FROM seller INNER JOIN department \
        ON seller.DepartmentId = department.Id""";

    try (PreparedStatement preparedStatement = connection.prepareStatement(
        query); ResultSet resultSet = preparedStatement.executeQuery()) {
      return extractSellerList(resultSet);
    }
    catch (SQLException e) {
      throw new DbException(e.getMessage());
    }
  }

  @Override
  public Seller findById(Integer id) {
    ValidationUtils.validateId(id);

    String query = """
        SELECT seller.*, department.Name as DepartmentName FROM seller 
        INNER JOIN department ON seller.DepartmentId = department.Id  
        WHERE seller.Id = ?""";

    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, id);

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        return extractSeller(resultSet);
      }
    }
    catch (SQLException e) {
      throw new DbException(e.getMessage());
    }
  }

  @Override
  public List<Seller> findByDepartment(Department department) {
    String query = """
        SELECT seller.*, department.Name as DepartmentName 
        FROM seller INNER JOIN department ON seller.DepartmentId = department.id 
        WHERE seller.DepartmentId = ? 
        ORDER BY Name""";

    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, department.getId());

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        return extractSellerList(resultSet);
      }
    }
    catch (SQLException e) {
      throw new DbException(e.getMessage());
    }
  }

  @Override
  public void insert(Seller seller) {
    String query = """
      INSERT INTO seller (Name, Email, BirthDate, BaseSalary, DepartmentId) 
      VALUES (?, ?, ?, ?, ?)""";

    try (PreparedStatement preparedStatement = connection.prepareStatement(
        query, Statement.RETURN_GENERATED_KEYS)) {

      preparedStatement.setString(1, seller.getName());
      preparedStatement.setString(2, seller.getEmail());

      Date birthDate = new java.sql.Date(seller.getBirthDate().getTime());
      preparedStatement.setDate(3, birthDate);

      preparedStatement.setDouble(4, seller.getBaseSalary());
      preparedStatement.setInt(5, seller.getDepartment().getId());

      int rowsAffected = preparedStatement.executeUpdate();

      if (rowsAffected > 0) {
        extractGeneratedId(preparedStatement, seller);
      }
      else {
        throw new DbException("Error while inserting seller");
      }
    }
    catch (SQLException e) {
      throw new DbException(e.getMessage());
    }
  }

  @Override
  public void update(Seller seller) {
    String query = """
        UPDATE seller Set Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ?
        WHERE Id = ?""";

    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

      preparedStatement.setString(1, seller.getName());
      preparedStatement.setString(2, seller.getEmail());

      Date birthDate = new java.sql.Date(seller.getBirthDate().getTime());
      preparedStatement.setDate(3, birthDate);

      preparedStatement.setDouble(4, seller.getBaseSalary());
      preparedStatement.setInt(5, seller.getDepartment().getId());
      preparedStatement.setInt(6, seller.getId());

      preparedStatement.executeUpdate();
    }
    catch (SQLException e) {
      throw new DbException(e.getMessage());
    }
  }

}