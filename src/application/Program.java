package application;

import java.util.Date;
import models.entities.Department;
import models.entities.Seller;

public class Program {
  public static void main(String[] args) {

    Seller seller = new Seller(1, "Lucas", "lucas@gmail.com", new Date(), 2000.0, new Department(1, "Roupas"));

    System.out.println(seller);
  }
}
