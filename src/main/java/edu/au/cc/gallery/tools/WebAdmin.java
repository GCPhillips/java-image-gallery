package edu.au.cc.gallery.tools;

import edu.au.cc.gallery.DB;

import static spark.Spark.*;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import spark.Response;
import spark.Request;

import java.util.HashMap;
import java.util.Map;

import java.util.List;
import java.util.ArrayList;

import java.sql.ResultSet;
import java.sql.SQLException;


public class WebAdmin {

    private static final String apiBase = "/admin";
    private static DB db;

    public static void run() {
        port(5000);
	db = new DB();
	addRoutes();
	try {
	    db.connect();
	}
	catch (SQLException ex) {
	    System.out.println("[ERR] Could not connect to the database");
	}
    }

    private static void addRoutes() {
	get(apiBase, (req, res) -> getUsers(req, res));
	post(apiBase + "/edituserform", (req, res) -> editUserForm(req, res));
	post(apiBase + "/deleteuserform", (req, res) -> deleteUserForm(req, res));
	post(apiBase + "/deleteuser", (req, res) -> deleteUser(req, res));
	post(apiBase + "/edituser", (req, res) -> editUser(req, res));
	post(apiBase + "/adduserform", (req, res) -> addUserForm(req, res));
	post(apiBase + "/adduser", (req, res) -> addUser(req, res));
    }

    public static void stop() {
        stop();
	try {
	    db.close();
	}
	catch (SQLException ex) {
	    System.out.println("[ERR] Could not close the database connection");
	}
    }

    private static String getUsers(Request req, Response res) {
	ResultSet rs;
	Map<String, Object> model = new HashMap<>();
	List<Map<String, Object>> users = new ArrayList<>();
	Map<String, Object> userInfo;
        try {
	    rs = db.executeQuery("select username,password,fullname from users");
	    while (rs.next()) {
		userInfo = new HashMap<>();
		userInfo.put("name", rs.getString(1));
		userInfo.put("pass", rs.getString(2));
		userInfo.put("fullname", rs.getString(3));
		users.add(userInfo);
	    }
	    model.put("users", users);
	}
	catch (SQLException ex) {
	    System.out.println("[ERR] " + ex.getMessage());
	}

	return render(model, "userlist.hbs");
    }

    private static String editUserForm(Request req, Response res) {
	Map<String, Object> model = new HashMap<>();

	model.put("name", req.attribute("name"));

	return render(model, "edituserform.hbs");
    }

    private static String editUser(Request req, Response res) {
	Map<String, Object> model = new HashMap<>();

	model.put("name", req.queryParams("name"));

	return render(model, "edituser.hbs");
    }

    private static String deleteUserForm(Request req, Response res) {
	Map<String, Object> model = new HashMap<>();

	model.put("name", req.queryParams("name"));

	return render(model, "deleteuserform.hbs");
    }

    private static String deleteUser(Request req, Response res) {
	try {
	    db.deleteUser(req.queryParams("name"));
	}
	catch (Exception ex) {
	    System.out.println("[ERR] Could not delete the user " + req.queryParams("name") + ": " + ex.getMessage());
	}

	res.redirect("/admin");
	return getUsers(null, null);
    }

    private static String addUserForm(Request req, Response res) {
	Map<String, Object> model = new HashMap<>();

	return render(model, "adduserform.hbs");
    }

    private static String addUser(Request req, Response res) {
	try {
	    db.addUser(req.queryParams("name"), req.queryParams("pass"), req.queryParams("fullname"));
	}
	catch (Exception ex) {
	    System.out.println("[ERR] Could not add the user " + req.queryParams("name") + ": " + ex.getMessage());
	}

	res.redirect("/admin");
	return getUsers(null, null);
    }

    private static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine()
            .render(new ModelAndView(model, templatePath));
    }

}
