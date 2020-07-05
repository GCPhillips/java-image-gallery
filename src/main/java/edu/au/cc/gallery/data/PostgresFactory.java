package edu.au.cc.gallery.data;

import java.sql.SQLException;

public class PostgresFactory {
    public static UserDAO getUserDAO() throws SQLException { return new PostgresUserDAO(); }
}
