package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import java.util.Date;
import java.util.Scanner;
import model.entities.Department;
import model.entities.Seller;

public class ProgramTestSeller {

  public static void main(String[] args) {

    Scanner sc = new Scanner(System.in);

    DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
    SellerDao sellerDao = DaoFactory.createSellerDao(departmentDao);

    System.out.println("------- TEST 1: seller findById -------");
    System.out.println(sellerDao.findById(5));

    System.out.println();

    // System.out.println("------- TEST 2: seller deleteById -------");
    // sellerDao.deleteById(6);
    // System.out.println("Delete completed");

    // System.out.println();

    System.out.println("------- TEST 3: seller findAll -------");
    sellerDao.findAll()
        .forEach(System.out::println);

    System.out.println();

    System.out.println("------- TEST 4: seller findByDepartment -------");
    Department department = new Department(4, null);
    sellerDao.findByDepartment(department).forEach(System.out::println);

    System.out.println();

    System.out.println("------- TEST 5: seller insert -------");
    Department department1 = new Department(6, null);
    Seller seller = new Seller(null, "TEST_1", "TESTE_1@gmail.com", new Date(), 900.0, department1);
    sellerDao.insert(seller);
    System.out.println("inserted!");

    System.out.println();

    System.out.println("------ TEST 6: seller update -------");
    seller = sellerDao.findById(20);
    seller.setName("Gustavo");
    sellerDao.update(seller);
    System.out.println("updated!");

    System.out.println();
  }
}
