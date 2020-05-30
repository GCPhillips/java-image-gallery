package edu.au.cc.gallery.tools;

import java.util.Scanner;

public class UserAdmin {

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

    public static void run() {
        int value = -1;

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
    }
    

    public static void listUsers() {

    }

    public static void addUser() {

    }

    public static void editUser() {

    }

    public static void deleteUser() {

    }
}
