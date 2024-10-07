package application;

import dao.DaoFactory;
import dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

  public static void main(String[] args) {

    DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

    System.out.println("----- TEST 7: department findAll -----");
    departmentDao.findAll()
        .forEach(System.out::println);

    System.out.println();

    System.out.println("----- TEST 8: department findById -----");
    System.out.println(departmentDao.findById(1));

    System.out.println();

    System.out.println("----- TEST 9: department insert -----");
    Department departmentInsert = new Department(null, "Test");
    departmentDao.insert(departmentInsert);
    System.out.println("edited!");

    System.out.println();

    System.out.println("----- TEST 10: department deleteById -----");
    departmentDao.deleteByID(departmentInsert.getId() - 1);
    System.out.println("deleted!");

    System.out.println();

    System.out.println("----- TEST 11: department update -----");
    Department departmentUpdate = departmentDao.findById(8);
    departmentUpdate.setName("Food");
    departmentDao.update(departmentUpdate);
    System.out.println("Update completed");
  }
}
