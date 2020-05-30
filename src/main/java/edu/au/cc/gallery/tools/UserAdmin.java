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
}
