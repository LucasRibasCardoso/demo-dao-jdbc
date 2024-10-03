package dao;

import java.util.List;
import model.entities.Department;

public interface DepartmentDao {

  void insert(Department department);
  void update(Department department);
  void deleteByID(int id);
  Department findById(int id);
  List<Department> findAll();

}
