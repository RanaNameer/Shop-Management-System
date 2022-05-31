package attr;

import java.lang.*;
import java.util.*;
import java.sql.*;
import javax.swing.table.*;
import java.awt.*;
import java.text.*;
import attr.*;
import activity.*;
import javax.swing.*;

public class Product {
	private String productId;
	private String productName;
	private double price;
	private int quantity;
	public static String[] columnNames = {"PID", "Name", "Price", "AvailableQuantity"};
	
	public Product() {}
	public Product(String productId) {
		setProductId(productId);
	}
	public void setProductId(String productId){
		if (!productId.isEmpty())
			this.productId = productId;
		else
			throw new IllegalArgumentException("Fill in the ID");
	}
	public void setProductName(String name) {
		if (!name.isEmpty())
			this.productName = name;
		else
			throw new IllegalArgumentException("Fill in the name");
	}
	public void setPrice(double p) {
		if (p<0)
			throw new IllegalArgumentException("Price can not be negative");
		else
			this.price = p;
	}

	public void setQuantity(int q) {
		if (q<0)
			throw new IllegalArgumentException("Quantity can not be negative");
		else
			this.quantity = q;
	}
	public String getProductId() {
		return productId;
	}
	public String getProductName() {
		return productName;
	}
	public double getPrice() {
		return price;
	}
	public int getQuantity() {
		return quantity;
	}

	private void connection(String id){
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String query = "SELECT `productId` FROM `product` WHERE productId='"+id+"';";


		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(Database.HOST_URI, Database.USER, Database.PASSWORD);
			st = con.createStatement();//create statement
			rs = st.executeQuery(query);//getting result

			if(!rs.next()) {
				throw new IllegalArgumentException("No Product Exist with this id");
			}
		}
		catch(Exception ex) {
			System.out.println("Exception : " +ex.getMessage());
			throw new IllegalArgumentException("No Product Exist with this id");
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
		connection(this.productId);
		String query = "SELECT `productId`, `productName`, `price`, `quantity` FROM `product` WHERE productId='"+this.productId+"';";     
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
				this.productName = rs.getString("productName");
				this.price = rs.getDouble("price");
				this.quantity = rs.getInt("quantity");
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
	}
	
	public void sellProduct(String uid, int amount) {
		String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
		String query = "INSERT INTO `purchaseInfo` (`userId`, `productId`, `amount`, `date`, `cost`) VALUES ('"+uid+"','"+this.productId+"',"+amount+", '"+date+"', "+(amount*this.price)+");";
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
			st.execute(query);//insert
			System.out.println("data inserted");
			updateProduct(this.productName, this.price, this.quantity-amount);
		}
        catch(Exception ex) {
			JOptionPane.showMessageDialog(null,"Customer doesn't exist!"); 
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
	
	public void updateProduct(String name, double price, int quantity) {
		connection(this.productId);
		String query = "UPDATE `product` SET `productName`='"+name+"', `price`="+price+", `quantity`="+quantity+" WHERE `productId`='"+this.productId+"';";
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
			JOptionPane.showMessageDialog(null,"Done!");
		}
        catch(Exception ex) {
			JOptionPane.showMessageDialog(null,"Failed!");
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
	
	public void createProduct() {
		String query = "INSERT INTO `product` (`productName`, `price`, `quantity`) VALUES ('"+productName+"','"+price+"','"+quantity+"');";
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
			st.execute(query);//insert
			System.out.println("data inserted");
			JOptionPane.showMessageDialog(null,"Product Created!");
		}
        catch(Exception ex) {
			JOptionPane.showMessageDialog(null,"Failed to add Product!");
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

	private static void found(String key,String by){
		String query = "SELECT `productId` FROM `product` WHERE `productId`='"+key+"';";
		if (by.equals("By Name"))
			query = "SELECT `productId` FROM `product` WHERE `productName` LIKE '%"+key+"%';";

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
				throw new IllegalArgumentException("No product found");
			}
		}
		catch(Exception ex) {
			System.out.println("Exception : " +ex.getMessage());
			throw new IllegalArgumentException("No product found");
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

	
	public static DefaultTableModel searchProduct(String keyword, String byWhat) {
		found(keyword,byWhat);
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(columnNames);

		String query = "SELECT `productId`, `productName`, `price`, `quantity` FROM `product` WHERE `productId`='"+keyword+"';";
		if (byWhat.equals("By Name"))
			query = "SELECT `productId`, `productName`, `price`, `quantity` FROM `product` WHERE `productName` LIKE '%"+keyword+"%';";
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
				System.out.println("Product id "+rs.getString("productId"));
				System.out.println("Product name "+rs.getString("productName"));
				System.out.println("Price "+rs.getDouble("price"));
				System.out.println("Quantity "+rs.getDouble("quantity"));
				model.addRow(new Object[]{rs.getString("productId"), rs.getString("productName"), rs.getDouble("price"), rs.getInt("quantity")});
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
	
	public void deleteProduct() {
		connection(this.productId);
		String query1 = "DELETE FROM `product` WHERE `productId`='"+this.productId+"';";
		Connection con = null;
        Statement st = null;
		System.out.println(query1);
        try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("driver loaded");
			con = DriverManager.getConnection(Database.HOST_URI, Database.USER, Database.PASSWORD);
			System.out.println("connection done");//connection with database established
			st = con.createStatement();//create statement
			System.out.println("statement created");
			st.execute(query1);//delete
			System.out.println("data deleted");
			JOptionPane.showMessageDialog(null,"Product Deleted!");
		}
        catch(Exception ex) {
			JOptionPane.showMessageDialog(null,"Failed to delete product!");
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
}