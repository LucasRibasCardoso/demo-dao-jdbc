package model.utils;

import DB.DbException;
import model.entities.Department;

public class ValidationUtils {

  public static void validateId(Integer id) throws DbException {
    if (id == null) {
      throw new IllegalArgumentException("ID must not be null.");
    }
  }

  public static void validateDepartmentForInsert(Department department) throws IllegalArgumentException {
    if (department == null || department.getName() == null || department.getName().isEmpty()) {
      throw new IllegalArgumentException(
          "Department must not be null and must have a valid name for insertion."
      );
    }
  }

  public static void validateDepartment(Department department) throws DbException {
    if (department == null) {
      throw new IllegalArgumentException("Department must not be null.");
    }

    if (department.getId() == null) {
      throw new IllegalArgumentException("Department must have a valid ID.");
    }

    if (department.getName() == null || department.getName()
        .isEmpty()) {
      throw new IllegalArgumentException("Department must have a valid name.");
    }
  }

  public static void validateUpdateOperation(int rowsAffected) throws DbException {
    if (rowsAffected == 0) {
      throw new DbException("nothing found with this ID");
    }
  }
}
