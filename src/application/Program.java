package application;

import dao.DaoFactory;
import dao.SellerDao;
import java.sql.SQLOutput;
import java.util.Date;
import java.util.Scanner;
import model.entities.Department;
import model.entities.Seller;

public class Program {
  public static void main(String[] args) {

    Scanner sc = new Scanner(System.in);

    SellerDao sellerDao = DaoFactory.createSellerDao();

    System.out.println("------- TEST 1: seller findById -------");
    System.out.println(sellerDao.findById(8));

    System.out.println();

    System.out.println("------- TEST 2: seller deleteById -------");
    System.out.print("Enter id for delete test: ");
    int id = sc.nextInt();
    sellerDao.deleteById(id);
    System.out.println("Delete completed");


    System.out.println();

    System.out.println("------- TEST 3: seller findAll -------");
    sellerDao.findAll().forEach(System.out::println);

    System.out.println();

    System.out.println("------- TEST 4: seller findByDepartment -------");
    Department department = new Department(4, null);
    sellerDao.findByDepartment(department).forEach(System.out::println);

    System.out.println("------- TEST 5: seller insert -------");
    Department department1 = new Department(6, null);
    Seller seller = new Seller(null, "Thiago", "thiago@gmail.com", new Date(), 900.0, department1);
    sellerDao.insert(seller);
    System.out.println("inserted!");

    System.out.println("------ TEST 6: seller update -------");
    seller = sellerDao.findById(15);
    seller.setName("Gabriel Toledo");
    sellerDao.update(seller);
    System.out.println("updated!");
  }
}
