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

    private static final String dbUrl = "jdbc:postgresql://image-gallery.cs6pn3k8vrxr.us-east-2.rds.amazonaws.com/image_gallery";
    private Connection connection;

    private JSONObject getSecret() {
        String s = Secrets.getSecretImageGallery();
        return new JSONObject(s);
    }

    private String getPassword(JSONObject secret) {
        return secret.getString("password");
    }

    private String getHostname() {
        String host = "";
        try {
            File file = new File("/home/ec2-user/.dbhostname");
            Scanner scanner = new Scanner(file);
            if (scanner.hasNextLine()) {
                host = "jdbc:postgresql://" + scanner.nextLine() + "/image_gallery";
            }
            scanner.close();
        }
        catch (FileNotFoundException ex) {
            System.out.println("[ERR]: .dbhostname file not found or is empty..." + ex.getMessage());
            ex.printStackTrace();
        }
        return host;
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
            JSONObject secret = getSecret();
            connection = DriverManager.getConnection(getHostname(), "image_gallery", getPassword(secret));
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            System.exit(1);
        }

    }

    public void close() throws SQLException {
        connection.close();
    }
}
