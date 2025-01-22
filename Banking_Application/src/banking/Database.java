package banking;
import java.sql.*;
import java.sql.Connection;
import java.util.ArrayList;
public class Database {
	private Connection connection;
	public Database() {
		try {
			connection  = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "jai1466@1");
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void insertAccount(Account account) {
		String sql = "insert into accounts (customer_id,name,phone_number,address,account_type,balance)"
				+ "values(?,?,?,?,?,?)" ;
		try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","jai1466@1");
				PreparedStatement psmt = conn.prepareStatement(sql))
				{
					psmt.setLong(1, account.getCustomerId());
					psmt.setString(2, account.getName());
					psmt.setString(3, account.getPhoneNumber());
					psmt.setString(4, account.getaccountType());
					psmt.setString(5, account.getAddress());
					psmt.setDouble(6, account.getBalance());
					psmt.executeUpdate();
					
				}catch(SQLException e) {
					System.out.println(e.getMessage());
				}
	}
	public void updateAccountBalance(Account account) {
	    try {
	        String query = "UPDATE accounts SET balance = ? WHERE account_id = ?"; // Corrected column name
	        PreparedStatement statement = connection.prepareStatement(query);
	        statement.setDouble(1, account.getBalance());
	        statement.setInt(2, account.getAccountid()); // Ensure this is account ID, not customer ID
	        statement.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

		public void updateAccountDetails(Account account) {
	    String query = "UPDATE accounts SET name = ?, phone_number = ?, address = ? WHERE account_id = ?";

	    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	        preparedStatement.setString(1, account.getName());
	        preparedStatement.setString(2, account.getPhoneNumber());
	        preparedStatement.setString(3, account.getAddress());
	        preparedStatement.setInt(4, account.getAccountid());

	        int rowsUpdated = preparedStatement.executeUpdate();
	        if (rowsUpdated > 0) {
	            System.out.println("Account details updated successfully.");
	        } else {
	            System.out.println("No account found with the given ID.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	public void closeConnection() {
		try {
			if(connection!= null) {
				connection.close();
			}
		}
			catch(SQLException e){
				e.printStackTrace();
			}
	}
	public ArrayList<Account> getAllAccounts() {
		ArrayList<Account> accounts = new ArrayList<>();
	    try {
	        String query = "SELECT * FROM accounts";
	        Statement statement = connection.createStatement();
	        ResultSet resultSet = statement.executeQuery(query);
	        while (resultSet.next()) {
	            int accountId = resultSet.getInt("account_id");
	            int customerId = resultSet.getInt("customer_id");
	            String accountType = resultSet.getString("account_type");
	            double balance = resultSet.getDouble("balance");
	            String name = resultSet.getString("name");
	            String phoneNumber = resultSet.getString("phone_number");
	            String address = resultSet.getString("address");
	            Account account = new Account(customerId, accountType, name, phoneNumber, address);
	            account.setAccountId(accountId); 
	            account.deposit(balance); 

	            accounts.add(account);
	        }
	    }catch(SQLException e){
	    	e.printStackTrace();
	    }
		return accounts;
	}

}
