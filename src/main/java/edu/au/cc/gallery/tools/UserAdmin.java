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
            "\n1) List Users\n"
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

    public static void run() {
        int value = -1;

        db = new DB();

        try {
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
        catch (SQLException ex) {
            System.out.println("[ERR]: " + ex.getMessage());
        }
    }

    public static void listUsers() {
        ResultSet rs;

        try {
            rs = db.executeQuery("select username,password,fullname from users");
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

    private static boolean userInfoIsValid(String[] userInfo) {
        for (int i=0; i < 2; i++) {
            if (userInfo[i].contains(" ") || userInfo[i].isEmpty())
                return false;
        }

        if (userInfo[2].isEmpty()) 
            return false;

        return true;        
    }

    private static String[] getUserInfo() {
        String[] userInfo = new String[3];
        Scanner sc = new Scanner(System.in);

        System.out.print("\nUsername> ");
        userInfo[0] = sc.nextLine().trim().toLowerCase();

        System.out.print("Password> ");
        userInfo[1] = sc.nextLine().trim();

        System.out.print("First Name> ");
        userInfo[2] = sc.nextLine().trim();

        System.out.print("Last Name> ");
        userInfo[2] = userInfo[2] + " " + sc.nextLine().trim();

        if (! userInfoIsValid(userInfo)) {
            System.out.println("The information entered is not valid:");
            System.out.println("Username and Password can't contain spaces,");
            System.out.println("values entered can't be blank");

            return null;
        }

        return userInfo;
    }

    private static String getUsername() {
        Scanner sc = new Scanner(System.in);
        
        System.out.print("\nUsername> ");
        return sc.nextLine().trim().toLowerCase();
    }

    public static void addUser() {
        String[] userInfo = getUserInfo();
        ResultSet rs;

        if (userInfo == null)
            return; 

        try {
            db.addUser(userInfo[0], userInfo[1], userInfo[2]);        
            
        }
        catch (Exception ex) {
            System.out.println("[ERR] " + ex.getMessage());
            return;
        }
    }

    public static void editUser() {
        String userName = getUsername();
        Scanner sc = new Scanner(System.in);
        String password, fullName;

        if (userName.isEmpty()) {
            System.out.println("[ERR] Username can't be empty.");
            return;
        }

        try {
            if (! db.checkIfUserExists(userName)) {
                System.out.println("[ERR] User does not exist.");
                return;
            }
        }
        catch (Exception ex) {
                System.out.println("[ERR] " + ex.getMessage());
        }
        
        
        System.out.print("\nPassword> ");
        password = sc.nextLine();

        System.out.print("Full name> ");
        fullName = sc.nextLine();

        try {
            db.editUser(userName, password, fullName);
        }
        catch (Exception ex) {
            System.out.print("[ERR] Could not edit the user: " + ex.getMessage());
        }
        
    }

    public static void deleteUser() {

    }
}
