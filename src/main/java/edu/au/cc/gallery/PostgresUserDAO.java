package edu.au.cc.gallery;

import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgresUserDAO implements UserDAO{
    private DB connection;

    public PostgresUserDAO() throws SQLException {
        connection = new DB();
        connection.connect();
    }

    public List<User> getUsers() throws SQLException{
        List<User> result = new ArrayList<>();
        ResultSet rs = connection.executeQuery("select username,password,fullname from users");
        while (rs.next()) {
            result.add(new User(rs.getString(1), rs.getString(2), rs.getString(3)));
        }

        return result;
    }
}
