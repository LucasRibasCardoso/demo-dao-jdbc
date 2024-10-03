package application;

import java.util.Date;
import model.entities.Department;
import model.entities.Seller;

public class Program {
  public static void main(String[] args) {

    Seller seller = new Seller(1, "Lucas", "lucas@gmail.com", new Date(), 2000.0, new Department(1, "Roupas"));

    System.out.println(seller);
  }
}
