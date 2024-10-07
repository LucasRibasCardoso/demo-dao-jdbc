package application;

import dao.DaoFactory;
import dao.SellerDao;
import model.entities.Department;

public class Program {
  public static void main(String[] args) {

    SellerDao sellerDao = DaoFactory.createSellerDao();

    System.out.println("------- TEST 1: seller findById -------");
    System.out.println(sellerDao.findById(8));

    System.out.println();

    System.out.println("------- TEST 2: seller deleteById -------");
    sellerDao.deleteById(1);

    System.out.println();

    System.out.println("------- TEST 3: seller findAll -------");
    sellerDao.findAll().forEach(System.out::println);

    System.out.println();

    System.out.println("------- TEST 4: seller findByDepartment -------");
    Department department = new Department(4, null);
    sellerDao.findByDepartment(department).forEach(System.out::println);
  }
}
