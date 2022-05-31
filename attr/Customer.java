package attr;

import java.lang.*;
import java.util.*;
import java.text.*;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;
import attr.*;
import activity.*;

public class Customer extends User {
	private String customerName;
	private String phoneNumber;
	private String address;
	public static String valUpdateCustomer;
	public static String[] columnNames = {"PurchaseID", "ProductID", "ProductName", "Amount", "Cost", "Date"};
	public static String[] columnName = {"CustomerID", "CustomerName", "PhoneNumber", "Address"};
	public Customer(String userId) {
		super(userId);
		
		this.setStatus(1);
	}
	
	public void setCustomerName(String name) {
		if(name.isEmpty())
			throw new IllegalArgumentException("Fill in the Name");
		else if(isNumeric(name)){
			throw new IllegalArgumentException("Name must contains alphabets only");
		}
		else
			this.customerName = name;

	}
	private boolean isNotNumeric(String pass) {
		char[] ch = pass.toCharArray();
		for (char c : ch) {
			if (Character.isLetter(c)) {
				return true;
			}
		}
		return false;
	}
	public void setPhoneNumber(String num) {
		if(num.isEmpty())
			throw new IllegalArgumentException("Fill in the Phone Number");
		else if(isNotNumeric(num))
			throw new IllegalArgumentException("Phone number must contains digits only");
		else if(num.length() != 10)
			throw new IllegalArgumentException("Phone number must be 10 characters long");

		else
			this.phoneNumber = "+92"+num;

	}
	public void setAddress(String address) {
		if(address.isEmpty())
			throw new IllegalArgumentException("Fill in the address");
		else if(address.length()<10){
			throw new IllegalArgumentException("Address must be atleast 10 characters long");
		}
		else
			this.address = address;

	}
	public String getCustomerName() {
		return customerName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public String getAddress() {
		return address;
	}

	public void createCustomer(JFrame sa) {
		String query1 = "INSERT INTO `login` VALUES ('"+userId+"','"+password+"',"+status+");";
		String query2 = "INSERT INTO `customer` VALUES ('"+userId+"','"+customerName+"','"+phoneNumber+"','"+address+"');";
		Connection con = null;
        Statement st = null;

        try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("driver loaded");
			con = DriverManager.getConnection(Database.HOST_URI, Database.USER, Database.PASSWORD);
			System.out.println("connection done");
			st = con.createStatement();
			System.out.println("statement created");
			st.execute(query1);//insert
			st.execute(query2);
			System.out.println("data inserted");
			JOptionPane.showMessageDialog(sa,"Account Created!");
			sa.setVisible(false);
			new LoginActivity().setVisible(true);
		}
        catch(Exception ex) {
			JOptionPane.showMessageDialog(sa,"Failed to create account!");
			//throw new IllegalArgumentException("Failed to create account!");
		}
        finally {
            try {
                if(st!=null)
					st.close();

                if(con!=null)
					con.close();
            }
            catch(Exception ex) {}
        }
	}

	private void connection(String id){
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String query = "SELECT `userId` FROM `customer` WHERE userId='"+id+"';";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(Database.HOST_URI, Database.USER, Database.PASSWORD);
			st = con.createStatement();//create statement
			rs = st.executeQuery(query);//getting result

			if(!rs.next()) {
				throw new IllegalArgumentException("No User Exist with this id");
			}
		}
		catch(Exception ex) {
			System.out.println("Exception : " +ex.getMessage());
			throw new IllegalArgumentException("No User Exist with this id");
		}
		finally {
			try {
				if(rs!=null)
					rs.close();

				if(st!=null)
					st.close();

				if(con!=null)
					con.close();
			}
			catch(Exception ex) {}
		}
	}

	
	public void fetch() {
		connection(this.userId);
		String query = "SELECT `userId`, `customerName`, `phoneNumber`, `address` FROM `customer` WHERE userId='"+this.userId+"';";     

		Connection con = null;
        Statement st = null;
		ResultSet rs = null;
		System.out.println(query);
        try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("driver loaded");
			con = DriverManager.getConnection(Database.HOST_URI, Database.USER, Database.PASSWORD);
			System.out.println("connection done");//connection with database established
			st = con.createStatement();//create statement
			System.out.println("statement created");
			rs = st.executeQuery(query);//getting result
			System.out.println("results received");
			
			while(rs.next()) {
				this.customerName = rs.getString("customerName");
				this.phoneNumber = rs.getString("phoneNumber");
				this.address = rs.getString("address");

			}
		}
        catch(Exception ex) {
			System.out.println("Exception : " +ex.getMessage());
			throw new IllegalArgumentException("Failed to fetch");
        }
        finally {
            try {
                if(rs!=null)
					rs.close();

                if(st!=null)
					st.close();

                if(con!=null)
					con.close();
            }
            catch(Exception ex) {}
        }
	}
	
	public void updateCustomer(String name, int phone, String address) {
		connection(this.userId);
		String query = "UPDATE `customer` SET `customerName`='"+name+"', `phoneNumber`='+92"+phone+"', `address`='"+address+"' WHERE `userId`='"+this.userId+"';";
		Connection con = null;
        Statement st = null;
		System.out.println(query);
        try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("driver loaded");
			con = DriverManager.getConnection(Database.HOST_URI, Database.USER, Database.PASSWORD);
			System.out.println("connection done");//connection with database established
			st = con.createStatement();//create statement
			System.out.println("statement created");
			st.executeUpdate(query);//insert
			System.out.println("data inserted");
			JOptionPane.showMessageDialog(null,"Information Updated!");
			this.customerName = name;
			this.phoneNumber = "+92"+phone;
			this.address = address;
			valUpdateCustomer = "Passed";
		}
        catch(Exception ex) {
			JOptionPane.showMessageDialog(null,"Failed to update account!");
			System.out.println("Exception : " +ex.getMessage());
			throw new IllegalArgumentException("Failed to update account!");
		}
        finally {
            try {
                if(st!=null)
					st.close();

                if(con!=null)
					con.close();
            }
            catch(Exception ex) {}
        }
	}
	public void deleteCustomer() {
		connection(this.userId);
		String query1 = "DELETE FROM `login` WHERE `userId`='"+this.userId+"';";
		String query2 = "DELETE FROM `customer` WHERE `userId`='"+this.userId+"';";
		Connection con = null;
        Statement st = null;
		System.out.println(query1);
		System.out.println(query2);
        try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("driver loaded");
			con = DriverManager.getConnection(Database.HOST_URI, Database.USER, Database.PASSWORD);
			System.out.println("connection done");//connection with database established
			st = con.createStatement();//create statement
			System.out.println("statement created");
			st.execute(query1);
			st.execute(query2);//delete
			System.out.println("data deleted");
			JOptionPane.showMessageDialog(null,"Account Deleted!");
			this.userId = "";
			this.customerName = "";
			this.phoneNumber = "";
			this.address = "";
		}
        catch(Exception ex) {
			JOptionPane.showMessageDialog(null,"Failed to delete account!");
			System.out.println("Exception : " +ex.getMessage());
        }
        finally {
            try {
                if(st!=null)
					st.close();

                if(con!=null)
					con.close();
            }
            catch(Exception ex) {}
        }
	}
	
	public DefaultTableModel myProduct() {
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(columnNames);
		String query = "SELECT purchaseInfo.purchaseId, purchaseInfo.productId, product.productName, purchaseInfo.cost, purchaseInfo.amount, purchaseInfo.date FROM purchaseInfo, product WHERE (`purchaseInfo`.`userId`='"+this.userId+"' AND `purchaseInfo`.`productId`=`product`.`productId`) ORDER BY `date` DESC;";     
        Connection con = null;
        Statement st = null;
		ResultSet rs = null;
		System.out.println(query);
        try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("driver loaded");
			con = DriverManager.getConnection(Database.HOST_URI, Database.USER, Database.PASSWORD);
			System.out.println("connection done");//connection with database established
			st = con.createStatement();//create statement
			System.out.println("statement created");
			rs = st.executeQuery(query);//getting result
			System.out.println("results received");
			
			while(rs.next()) {
				String col1 = rs.getString("purchaseId");
				String col2 = rs.getString("productId");
				String col3 = rs.getString("productName");
				int col4 = rs.getInt("amount");
				double col5 = rs.getDouble("cost");
				String col6 = rs.getString("date");
				model.addRow(new Object[]{col1, col2, col3, col4, col5, col6});
			}
		}
        catch(Exception ex) {
			System.out.println("Exception : " +ex.getMessage());
        }
        finally {
            try {
                if(rs!=null)
					rs.close();

                if(st!=null)
					st.close();

                if(con!=null)
					con.close();
            }
            catch(Exception ex) {}
        }
		return model;
	}

	private static void found(String key,String by){
		String query = "SELECT * FROM `customer` WHERE `userId`='"+key+"';";
		if (by.equals("By Name"))
			query = "SELECT * FROM `customer` WHERE `customerName` LIKE '%"+key+"%';";

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		System.out.println(query);
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("driver loaded");
			con = DriverManager.getConnection(Database.HOST_URI, Database.USER, Database.PASSWORD);
			System.out.println("connection done");//connection with database established
			st = con.createStatement();//create statement
			System.out.println("statement created");
			rs = st.executeQuery(query);//getting result
			System.out.println("results received");

			if(!rs.next()) {
				throw new IllegalArgumentException("No customer found");
			}
		}
		catch(Exception ex) {
			System.out.println("Exception : " +ex.getMessage());
			throw new IllegalArgumentException("No customer found");
		}
		finally {
			try {
				if(rs!=null)
					rs.close();

				if(st!=null)
					st.close();

				if(con!=null)
					con.close();
			}
			catch(Exception ex) {}
		}

	}
	
	public static DefaultTableModel searchCustomer(String keyword, String byWhat) {
		found(keyword,byWhat);
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(columnName);
		String query = "SELECT * FROM `customer` WHERE `userId`='"+keyword+"';";
		if (byWhat.equals("By Name"))
			query = "SELECT * FROM `customer` WHERE `customerName` LIKE '%"+keyword+"%';";
		else {}
        Connection con = null;
        Statement st = null;
		ResultSet rs = null;
		System.out.println(query);
        try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("driver loaded");
			con = DriverManager.getConnection(Database.HOST_URI, Database.USER, Database.PASSWORD);
			System.out.println("connection done");//connection with database established
			st = con.createStatement();//create statement
			System.out.println("statement created");
			rs = st.executeQuery(query);//getting result
			System.out.println("results received");
			
			while(rs.next()) {
				System.out.println("User id "+rs.getString("userId"));
				System.out.println("Customer "+rs.getString("customerName"));

				model.addRow(new Object[]{rs.getString("userId"), rs.getString("customerName"), rs.getString("phoneNumber"), rs.getString("address")});
			}
		}
        catch(Exception ex) {
			System.out.println("Exception : " +ex.getMessage());
        }
        finally {
            try {
                if(rs!=null)
					rs.close();

                if(st!=null)
					st.close();

                if(con!=null)
					con.close();
            }
            catch(Exception ex) {}
        }
		return model;
	}
}