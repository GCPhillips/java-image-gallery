package edu.au.cc.gallery.ui;

import edu.au.cc.gallery.data.PostgresFactory;
import edu.au.cc.gallery.data.User;
import edu.au.cc.gallery.data.UserDAO;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.before;
import static spark.Spark.halt;

public class Admin {
    private static final String homePage = "/admin/users";
    private static final String adminName = "admin";

    public static void addRoutes() {
        get(homePage, (res, req) -> listUsers());
        get("/admin/users/:username",
                (req, res) -> getUser(req.params(":username")));
        post("/admin/adduser",
                (req, res) -> addUser(req.queryParams("name"), req.queryParams("pass"), req.queryParams("fullname"), res));
        post("/admin/deleteuserform",
                (req, res) -> deleteUserForm(req.queryParams("name"), req));
        post("/admin/deleteuser",
                (req, res) -> deleteUser(req.queryParams("name"), res));
        post("/admin/edituserform",
                (req, res) -> editUserForm(req.queryParams("name"), req));
        post("/admin/edituser",
                (req, res) -> editUser(req.queryParams("name"), req, res));
        post("/admin/adduserform",
                (req, res) -> addUserForm());
        before("/admin/*",
                (req, res) -> checkAdmin(req, res));
    }

    public static UserDAO getUserDAO() throws Exception {
        return PostgresFactory.getUserDAO();
    }

    private static String listUsers() {
        try {
            Map<String, Object> model = new HashMap<>();
            List<Map<String, Object>> users = new ArrayList<>();
            UserDAO dao = getUserDAO();
            for (User u: dao.getUsers()) {
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("name", u.getUsername());
                userInfo.put("pass", u.getPassword());
                userInfo.put("fullname", u.getFullName());
                users.add(userInfo);
            }
            model.put("users", users);
            return App.render(model, "userlist.hbs");
        } catch (Exception ex) {
            return "[ERR][Admin.listUsers()]: " + ex.getMessage();
        }
    }

    private static String getUser(String username) {
        try {
            UserDAO dao = getUserDAO();
            return dao.getUserByUsername(username).toString();
        } catch (Exception ex) {
            return "[ERR]: " + ex.getMessage();
        }
    }

    private static String addUserForm() {
        return App.render(null, "adduserform.hbs");
    }

    private static String addUser(String username, String password, String fullName, Response r) {
        try {
            UserDAO dao = getUserDAO();
            dao.addUser(new User(username, password, fullName));
            r.redirect(homePage);
            return "";
        } catch (Exception ex) {
            return "[ERR][Admin.addUser()]: " + ex.getMessage();
        }
    }

    private static String deleteUserForm(String username, Request req) {
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("name", req.queryParams("name"));
            return App.render(model, "deleteuserform.hbs");
        }
        catch (Exception ex) {
            return "[ERR][Admin.deleteUserForm()]: " + ex.getMessage();
        }
    }

    private static String deleteUser(String username, Response r) {
        try {
            UserDAO dao = getUserDAO();
            dao.deleteUser(dao.getUserByUsername(username));
            r.redirect(homePage);
            return "";
        }
        catch (Exception ex) {
            return "[ERR][Admin.deleteUser()]: " + ex.getMessage();
        }
    }

    private static String editUserForm(String username, Request req) {
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("name", req.queryParams("name"));
            return App.render(model, "edituserform.hbs");
        }
        catch (Exception ex) {
            return "[ERR][Admin.editUserForm()]: " + ex.getMessage();
        }
    }

    private static boolean isAdmin(String username) {
        return username != null && username.equals(adminName);
    }

    private static void checkAdmin(Request req, Response res) {
        if (!isAdmin(req.session().attribute("user"))) {
            res.redirect("/login");
            halt();
        }
    }

    private static String editUser(String username, Request req, Response r) {
        try {
            UserDAO dao = getUserDAO();
            User userToEdit = dao.getUserByUsername(username);
            userToEdit.setFullName(req.queryParams("fullname"));
            userToEdit.setPassword(req.queryParams("pass"));
            dao.editUser(userToEdit);
            r.redirect(homePage);
            return "";
        }
        catch (Exception ex) {
            return "[ERR][Admin.editUser()]: " + ex.getMessage();
        }
    }
}
