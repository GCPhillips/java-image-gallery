package edu.au.cc.gallery.tools;

import java.util.Scanner;

import java.sql.ResultSet;
import java.sql.SQLException;

import edu.au.cc.gallery.DB;

public class UserAdmin {

    private static DB db;

    public static int printMenu() {
        int value = -1;
        
        Scanner sc = new Scanner(System.in);

        System.out.println(
            "1) List Users\n"
            + "2) Add User\n"
            + "3) Edit User\n"
            + "4) Delete User\n"
            + "5) Quit");
        System.out.print("Enter command> ");
        String entry = sc.nextLine();

        try {
            value = Integer.parseInt(entry);
        }
        catch (Exception e) {
            return -1;
        }             

        return value;   
    }

    public static void run() throws SQLException {
        int value = -1;

        db = new DB();
        db.connect();

        while (value != 5) {
            value = printMenu();
            switch(value) {
                case 1:
                    listUsers();
                    break;
                case 2:
                    addUser();
                    break;
                case 3: 
                    editUser();
                    break;
                case 4:
                    deleteUser();
                    break;
            }
        }

        db.close();
    }
    

    public static void listUsers() {
        ResultSet rs;

        try {
            rs = db.execute("select username,password,fullname from users");
            System.out.println("\nusername     password     full name");
            System.out.println("-----------------------------------");           
            
            while(rs.next()) {
                System.out.printf("%-12s %-12s %-12s\n", rs.getString(1), rs.getString(2), rs.getString(3));
            }

            System.out.println();
        }
        catch (SQLException ex) {
            System.out.println("[ERR] " + ex.getMessage());
        } 
    }

    public static void addUser() {

    }

    public static void editUser() {

    }

    public static void deleteUser() {

    }
}
