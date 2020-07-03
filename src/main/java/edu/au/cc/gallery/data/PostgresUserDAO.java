package edu.au.cc.gallery.data;

import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgresUserDAO implements UserDAO {
    private DB connection;

    public PostgresUserDAO() throws SQLException {
        connection = new DB();
        connection.connect();
    }

    public List<User> getUsers() throws SQLException {
        List<User> result = new ArrayList<>();
        ResultSet rs = connection.executeQuery("select username,password,fullname from users");
        while (rs.next()) {
            result.add(new User(rs.getString(1), rs.getString(2), rs.getString(3)));
        }

        return result;
    }

    public User getUserByUsername(String username) throws SQLException {
        ResultSet rs = connection.executeQuery("select username,password,fullname from users where username=?", new String[]{username});
        if (rs.next()) {
            return new User(rs.getString(1), rs.getString(2), rs.getString(3));
        }
        return null;
    }

    public void addUser(User u) throws SQLException {
        connection.execute("insert into users(username,password,fullname) values (?,?,?)",
                new String[]{u.getUsername(), u.getPassword(), u.getFullName()});
    }

    public void deleteUser(User u) throws SQLException {
        connection.execute("delete from users where username=?",
                new String[]{u.getUsername()});
    }

    public void editUser(User u) throws SQLException {
        connection.execute("update users set password=?, fullname=? where username=?",
                new String[]{u.getPassword(), u.getFullName(), u.getUsername()});
    }

    public void addImage(User u, Image i) throws SQLException {
        connection.execute("insert into images(imageid, username) values (?,?)",
                new String[]{i.getUuid(), u.getUsername()});
    }

    public List<String> getImageUuids(User u) throws SQLException {
        List<String> uuids = new ArrayList<>();
        ResultSet rs = connection.executeQuery("select imageid from images where username=?",
                new String[] { u.getUsername()});
        while (rs.next()) {
            uuids.add(rs.getString(1));
        }

        return uuids;
    }

    public void deleteImage(User u, Image i) throws SQLException {
        connection.execute("delete from images where username=? and imageid=?",
                new String[] {u.getUsername(), i.getUuid()});
    }
}
