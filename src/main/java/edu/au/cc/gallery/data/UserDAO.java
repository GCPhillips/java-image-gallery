package edu.au.cc.gallery.data;

import java.util.List;

public interface UserDAO {
    
    /**
     * @return return the (possibly empty) list of users
     */	
    List<User> getUsers() throws Exception;

    /**
     * @return user with specified username or null if no such user
     */
    User getUserByUsername(String username) throws Exception;

    /**
     * Add a user to the database
     */
    void addUser(User u) throws Exception;

    /**
     * Remove a user from the database
     */
    void deleteUser(User u) throws Exception;

    /**
     * Edit a user on the database
     */
    void editUser(User u) throws Exception;
}
