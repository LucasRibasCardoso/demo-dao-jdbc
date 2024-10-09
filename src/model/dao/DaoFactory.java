package model.dao;

import DB.DB;
import model.dao.impl.DepartmentDaoJDBC;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {

  public static SellerDao createSellerDao(DepartmentDao department) {
    return new SellerDaoJDBC(DB.getConnection(), department);
  }

  public static DepartmentDao createDepartmentDao(){
    return new DepartmentDaoJDBC(DB.getConnection());
  }
}
