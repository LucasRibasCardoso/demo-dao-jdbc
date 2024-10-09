package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class ProgramTestDepartment {

  public static void main(String[] args) {

    DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

    System.out.println("----- TEST 1: department findAll -----");
    departmentDao.findAll().forEach(System.out::println);

    System.out.println();

    System.out.println("----- TEST 2: department findById -----");
    System.out.println(departmentDao.findById(1));

    System.out.println();

    System.out.println("----- TEST 3: department insert -----");
    Department departmentInsert = new Department(null, "TEST_02");
    departmentDao.insert(departmentInsert);
    System.out.println("inserted!");

    System.out.println();

    System.out.println("----- TEST 4: department deleteById -----");
    departmentDao.deleteById(departmentInsert.getId() - 1);
    System.out.println("deleted!");

    System.out.println();

    System.out.println("----- TEST 5: department update -----");
    Department departmentUpdate = departmentDao.findById(13);
    departmentUpdate.setName("TEST_03");
    departmentDao.update(departmentUpdate);
    System.out.println("Update completed");
  }
}
