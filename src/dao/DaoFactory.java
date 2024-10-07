package dao;

import DB.DB;
import dao.impl.DepartmentDaoJDBC;
import dao.impl.SellerDaoJDBC;

public class DaoFactory {

  public static SellerDao createSellerDao() {
    return new SellerDaoJDBC(DB.getConnection());
  }

  public static DepartmentDao createDepartmentDao(){
    return new DepartmentDaoJDBC(DB.getConnection());
  }
}
