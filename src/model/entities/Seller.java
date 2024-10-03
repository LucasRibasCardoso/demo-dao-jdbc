package model.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Seller implements Serializable {

  private static final long serialVersionUID = 1L;

  private int id;
  private String name;
  private String email;
  private Date birthDate;
  private Double salary;

  private Department department;

  public Seller(int id, String name, String email, Date birthDate, Double salary, Department department) {
    this.salary = salary;
    this.name = name;
    this.id = id;
    this.email = email;
    this.department = department;
    this.birthDate = birthDate;
  }

  public Date getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Double getSalary() {
    return salary;
  }

  public void setSalary(Double salary) {
    this.salary = salary;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Seller seller = (Seller) o;
    return getId() == seller.getId();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "Seller{" + "birthDate=" + birthDate + ", id=" + id + ", name='" + name + '\'' + ", email='"
        + email + '\'' + ", salary=" + salary + ", department=" + department + '}';
  }

}
