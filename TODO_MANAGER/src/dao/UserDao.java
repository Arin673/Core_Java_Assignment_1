package dao;

import model.User;
import database.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;

public class UserDao {
    Connection conn = DBConnection.getConnection();
    List<User> userList = new ArrayList<>();
    public void addUser(User user) throws Exception{
        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        try{
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1,user.getUserName());
            statement.setString(2,user.getEmail());
            statement.setString(3,user.getPassword());
            statement.executeUpdate();
        }
        catch(SQLException e){
            throw new Exception(e.getMessage());
        }
        userList.add(user);
    }
    public void updateUser(User user) throws Exception{
        String sql = "UPDATE users SET username = ?, email = ?, password = ? WHERE userId = ?";
        try{
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1,user.getUserName());
            statement.setString(2,user.getEmail());
            statement.setString(3,user.getPassword());
            statement.setInt(4,user.getUserId());

            statement.executeUpdate();
        }
        catch(SQLException e){
            throw new Exception(e.getMessage());
        }
    }
    public void deleteUser(int userId) throws Exception{
        String sql = "DELETE FROM users WHERE userId = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        userList.removeIf(user -> user.getUserId() == userId);
    }
    public List<User> viewAllUsers() throws Exception
    {
        List<User> allUsers = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("userId");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String password = rs.getString("password");
                User user = new User(username, email, password,userId);
                allUsers.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allUsers;
    }
	public User getUserByEmailAndPassword(String email, String password) throws Exception {
		// TODO Auto-generated method stub
		 String sql = "SELECT * FROM users where email=? and password=?";
		 //String sql = "select * from user where email=?";
	        User user = null;
	        try{
	            PreparedStatement statement = conn.prepareStatement(sql);
	            statement.setString(1,email);
	            statement.setString(2,password);
	            ResultSet rs = statement.executeQuery();

	            if(rs.next()){
	                user = new User();
	                user.setUserName(rs.getString(1));
	                user.setEmail(rs.getString(2));
	                user.setPassword(rs.getString(3));
	                user.setUserId(rs.getInt(4));
	            }
	            else
	                throw new Exception("No customer with "+email+" found");
	        } catch (SQLException e) {
	            throw new Exception(e.getMessage());
	        }
	        return user;
	}
}
