package attr;

import java.lang.*;
import java.sql.*;
import attr.*;
import javax.swing.*;
import activity.*;

public  class User {
	protected String userId;
	protected String password;
	protected int status;
	public static String validator;

	public User(){}

	public User(String userId) {
		setUserId(userId);

	}
	public void setUserId(String userId){
		if(userId==""){
			throw new IllegalArgumentException("Fill in username");
		}
		else if(userId.length()<4){
			throw new IllegalArgumentException("User name must be at least 4 characters long");
		}
		else if (!userId.isEmpty())
			this.userId = userId;

	}

	public void fetch(){

	};

	public String getUserId() {
		return userId;
	}

	public void setStatus(int stts) {
		this.status = stts;
	}

	/*public boolean isNumeric(String s) {
		for (int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);
			if (!(c >= 'A' && c <= 'Z') && !(c >= 'a' && c <= 'z')) {
				return false;
			}
		}
		return true;
	}*/
	public boolean isNumeric(String num) {
		char[] ch = num.toCharArray();
		for (char c : ch) {
			if (!(Character.isLetter(c))) {
				return true;
			}
		}
		return false;
	}
	public void setPassword(String passwd) {
		if(passwd.isEmpty())
			throw new IllegalArgumentException("Fill in the password");
		else if (passwd.length()<8)
			throw new IllegalArgumentException("Password must be at least 8 characters long");
		else if(!isNumeric(passwd)){
			throw new IllegalArgumentException("Password must contains at least one digit");
		}
		else {
			this.password = passwd;
		}

	}
	
	public static int checkStatus(String uid, String pass) {

		int result = -1;
		String query = "SELECT `userId`, `password`, `status` FROM `login`;";     
        Connection con = null;
        Statement st = null;
		ResultSet rs = null;
		System.out.println(query);
		LoginActivity login=new LoginActivity();

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
                String userId = rs.getString("userId");
                String password = rs.getString("password");
				int status = rs.getInt("status");
				
				if(userId.equals(uid) && password.equals(pass)) {
					result = status;
				}

			}

			if(result!=0 && result!=1){
				throw new IllegalArgumentException("Username or password incorrect");
				}

		}

        catch(Exception ex) {
			//System.out.println("Exception : " +ex.getMessage());
			JOptionPane.showMessageDialog(login,"Username or password incorrect");
			throw new IllegalArgumentException("Username or password incorrect");
			//ex.printStackTrace();
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
		return result;
	}
	
	public void changePassword(ChangePasswordActivity a, String oldPass, String newPass) {
		String query = "UPDATE `login` SET `password`='"+newPass+"' WHERE (`userId`='"+this.userId+"' AND `password`='"+oldPass+"');";
		Connection con = null;
        Statement st = null;

        try {
			Class.forName("com.mysql.jdbc.Driver");

			con = DriverManager.getConnection(Database.HOST_URI, Database.USER, Database.PASSWORD);

			st = con.createStatement();

			int res = st.executeUpdate(query);

			if (res > 0) {
				validator = "Successful";
				JOptionPane.showMessageDialog(null,"Password Updated!");
			a.setVisible(false);
			}
			else {
				validator = "Unsuccessful";
				JOptionPane.showMessageDialog(null,"Password didn't match!");
			}
		}
        catch(Exception ex) {
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