package edu.au.cc.gallery;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONObject;

public class DB {
    
    private static final String dbUrl = "jdbc:postgresql://image-gallery.cs6pn3k8vrxr.us-east-2.rds.amazonaws.com/image_gallery";
    private Connection connection;

    private JSONObject getSecret() {
	String s = Secrets.getSecretImageGallery();
	return new JSONObject(s);
    }

    private String getPassword(JSONObject secret) {
	return secret.getString("password");
    }

    public ResultSet executeQuery(String query) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    public ResultSet executeQuery(String query, String[] values) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        for(int i=0; i < values.length; i++) {
            stmt.setString(i + 1, values[i]);
        }
        
        ResultSet rs = stmt.executeQuery();
        return rs;
    }
   
    public void execute(String query, String[] values) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        for(int i=0; i < values.length; i++) {
            stmt.setString(i + 1, values[i]);
        }
        stmt.execute();
    }

    public void connect() throws SQLException {
	try {
	    Class.forName("org.postgresql.Driver");
	    JSONObject secret = getSecret();
	    connection = DriverManager.getConnection(dbUrl, "image_gallery", getPassword(secret));
	} catch (ClassNotFoundException ex) {
	    ex.printStackTrace();
	    System.exit(1);
	}
	
    }
    
    public void close() throws SQLException {
        connection.close();
    }

    public static void demo() throws Exception {
        DB db = new DB();
        db.connect();
        db.executeQuery("update users set password=? where username=?",
                                    new String[] {"monkey", "fred"});
        ResultSet rs = db.executeQuery("select username,password,fullname from users");
        while(rs.next()) { 
            System.out.println(rs.getString(1)+","+rs.getString(2)+","+rs.getString(3));
        }
        rs.close();
        db.close();
    }

    public boolean checkIfUserExists(String userName) throws SQLException {
        ResultSet rs;
        
        rs = executeQuery("select count(*) from users where username = ?", 
                                    new String[] { userName.trim().toLowerCase() });
        while (rs.next()) {
            int totalMatch = rs.getInt(1);
            if (totalMatch > 0) {
                return true;
            }
        }

        return false;
    }

    private boolean isValidUsername(String userName) {
        return userName.matches("^[a-zA-Z]+[a-zA-Z0-9]+$");
    }

    private boolean isValidFullname(String fullName) {
        return fullName.matches("^[a-zA-Z-']+ [a-zA-Z-']+$");
    }
    
    public void addUser(String userName, String password, String fullName) throws SQLException, Exception {
        if (checkIfUserExists(userName))
            throw new SQLException("The username \"" + userName + "\" already exists.");
 
        if (! isValidFullname(fullName))
            throw new Exception("The fullname is not in a valid format. "
                + "A single space must be used between First and Last name. "
                + "Hyphens and single-quotes are allowed.");
        if (! isValidUsername(userName))
            throw new Exception("The username is not in a valid format. "
                + "Username must start with a letter followed by a mixture of (optional) "
                + "numbers and letters.  All other characters are not valid.");

        execute("insert into users (username, password, fullname) values (?, ?, ?)",
                                    new String[] { userName, password, fullName });
        
    }

    public void editUser(String userName, String password, String fullName) throws SQLException, Exception {
        ResultSet rs;
        
        if (userName.isEmpty() || ! checkIfUserExists(userName))
            throw new SQLException("The user does not exist in the database.");

        if (fullName.isEmpty()) {
            rs = executeQuery("select fullname from users where username = \'" + userName + "\'");
            while(rs.next()) {
                fullName = rs.getString(1);
            }
        }
        if (password.isEmpty()) {
            rs = executeQuery("select password from users where username = \'" + userName + "\'");
            while(rs.next()) {
                password = rs.getString(1);
            }
        }

        if (! isValidFullname(fullName))
            throw new Exception("The fullname is not in a valid format. "
                + "A single space must be used between First and Last name. "
                + "Hyphens and single-quotes are allowed.");
        if (! isValidUsername(userName))
            throw new Exception("The username is not in a valid format. "
                + "Username must start with a letter followed by a mixture of (optional) "
                + "numbers and letters.  All other characters are not valid.");
        execute("update users set password=?, fullname=? where username=?", 
                                    new String[] { password, fullName, userName });
    }

    public void deleteUser(String userName) throws SQLException, Exception {
        if (userName.isEmpty() || ! checkIfUserExists(userName))
            throw new SQLException("The user does not exist in the database.");

        execute("delete from users where username=?",
                                    new String[] {  userName.trim().toLowerCase() });
    }
}
