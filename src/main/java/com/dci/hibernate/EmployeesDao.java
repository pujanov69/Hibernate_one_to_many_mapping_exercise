package com.dci.hibernate;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
 
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dci.hibernate.model.AssetMgnt;
import com.dci.hibernate.model.Employee;
 
 
public class EmployeesDao {
 
    public List<Employee> getEmployeeList(){
 
        Session session = null;
        List<Employee> empList = null;
        try {
            session = HibernateUtil.getSession();
            String queryStr = "select emp from Employee emp";
            Query query = session.createQuery(queryStr);
            empList = query.list();
        } catch(Exception ex) {
            ex.printStackTrace();
            // handle exception here
        } finally {
            try {if(session != null) session.close();} catch(Exception ex) {}
        }
        return empList;
    }
 
    public Employee getEmployeeById(Long empId){
 
        Session session = null;
        Employee emp = null;
        try {
            session = HibernateUtil.getSession();
            String queryStr = "select emp from Employee emp";
            emp = session.get(Employee.class, empId);
 
        } catch(Exception ex) {
            ex.printStackTrace();
            // handle exception here
        } finally {
            try {if(session != null) session.close();} catch(Exception ex) {}
        }
        return emp;
    }
 
    public void insertEmployee(Employee emp) {
 
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSession();
            transaction = session.beginTransaction();
            session.save(emp);
            System.out.println("inserted employee: "+emp.getName());
            transaction.commit();
        } catch(Exception ex) {
            ex.printStackTrace();
            // handle exception here
            if(transaction != null) transaction.rollback();
        } finally {
            try {if(session != null) session.close();} catch(Exception ex) {}
        }
    }
 
    public void deleteEmployee(Employee emp) {
 
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSession();
            transaction = session.beginTransaction();
            session.delete(emp);
            transaction.commit();
            System.out.println("deleted employee: "+emp.getName());
        } catch(Exception ex) {
            ex.printStackTrace();
            // handle exception here
            if(transaction != null) transaction.rollback();
        } finally {
            try {if(session != null) session.close();} catch(Exception ex) {}
        }
    }
 
    public static void main(String a[]) {
 
    	//Insert data into EMPLOYEE and EMP_DETAILS ID WITH MAPPING
    	EmployeesDao empDao = new EmployeesDao();
    	
    	Employee emp = new Employee(); 
    	emp.setName("Kabir");
		emp.setDepartment("Security"); 
		emp.setJoinedOn(new Date()); 
		emp.setSalary(new Long(5250)); 
		 
		AssetMgnt assetMgnt1 = new AssetMgnt();
		assetMgnt1.setEmployee(emp);
		assetMgnt1.setAssetName("Iphone");
		assetMgnt1.setVendor("Apple");
		
		
		AssetMgnt assetMgnt2 = new AssetMgnt();
		assetMgnt2.setEmployee(emp);
		assetMgnt2.setAssetName("Ipad");
		assetMgnt2.setVendor("Apple");
		
		emp.setAssetMgnt(Arrays.asList(assetMgnt1, assetMgnt2));
		 
		empDao.insertEmployee(emp);
		  
		System.out.println("---------------------------");
    
		//List all the data from the database table.
    
        List<Employee> empList = empDao.getEmployeeList();
        System.out.println("emp size: "+empList.size());
        System.out.println("---------------------------");
        empList.stream().forEach(e -> {
            System.out.println(e);
            System.out.println("-- asset given to "+e.getName()+" --");
            e.getAssetMgnt().stream().forEach(System.out::println);
            System.out.println("---------------------------");
        });
 
        System.out.println("Get Employee By Id");
        System.out.println("---------------------------");
 
        
        //get employee by id
        Employee empObj = empDao.getEmployeeById(emp.getEmpId());
		System.out.println(empObj);
		 

		//delete employee
		System.out.println("---------------------------");
		empDao.deleteEmployee(empObj);
        
		System.out.println("After Deletion");
		System.out.println("---------------------------");
		
		//List all the data from the database table
		empList = empDao.getEmployeeList();
		System.out.println("emp size: "+empList.size());
		empList.stream().forEach(System.out::println);
		 
    }
}
