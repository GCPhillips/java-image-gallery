package edu.au.cc.gallery.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.au.cc.gallery.aws.Secrets;

public class DB {

    private Connection connection;
    private static String pg_host;
    private static String ig_user;
    private static String ig_password;


    private JSONObject getSecret() {
        String s = Secrets.getSecretImageGallery();
        return new JSONObject(s);
    }

    private static String getHostname() {
        return pg_host;
    }

    public static void setHostname(String hostname, String pg_port, String ig_database) {
        pg_host = "jdbc:postgresql://" + hostname + ":" + pg_port + "/" + ig_database;
    }

    private static String getIg_user() {
        return ig_user;
    }

    public static void setIg_user(String iguser) {
        ig_user = iguser;
    }

    private static String getIg_password() {
        return ig_password;
    }

    public static void setIg_password(String igpassword) {
        ig_password = igpassword;
    }

    public ResultSet executeQuery(String query) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    public ResultSet executeQuery(String query, String[] values) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        for (int i = 0; i < values.length; i++) {
            stmt.setString(i + 1, values[i]);
        }

        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    public void execute(String query, String[] values) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        for (int i = 0; i < values.length; i++) {
            stmt.setString(i + 1, values[i]);
        }
        stmt.execute();
    }

    public void connect() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            //JSONObject secret = getSecret();
            connection = DriverManager.getConnection(getHostname(), getIg_user(), getIg_password());
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            System.exit(1);
        }

    }

    public void close() throws SQLException {
        connection.close();
    }
}
