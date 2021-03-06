/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.au.cc.gallery.ui;

import edu.au.cc.gallery.data.*;

import static spark.Spark.*;

import spark.Response;
import spark.Request;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import javax.servlet.MultipartConfigElement;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Base64;
import java.io.InputStream;
import java.io.File;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws Exception {
        String portString = System.getenv("JETTY_PORT");
        String pg_host = System.getenv("PG_HOST");
        String pg_port = System.getenv("PG_PORT");
        String ig_database = System.getenv("IG_DATABASE");
        String ig_user = System.getenv("IG_USER");
        String ig_passwd = System.getenv("IG_PASSWD");
        String ig_passwd_file = System.getenv("IG_PASSWD_FILE");
        String s3_image_bucket = System.getenv("S3_IMAGE_BUCKET");

        if (portString == null || portString.equals(""))
            port(5000);
        else
            port(Integer.parseInt(portString));

        if (ig_passwd_file != null && !ig_passwd_file.equals("")) {
            try {
                File file = new File(ig_passwd_file);
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    ig_passwd = scanner.nextLine();
                }
            }
            catch (Exception ex) {
                System.out.println("[ERR][App.main()]: " + ex.getMessage());
            }
        }

        DB.setHostname(pg_host, pg_port, ig_database);
        DB.setIg_password(ig_passwd);
        DB.setIg_user(ig_user);
        S3ImageDAO.setBucketname(s3_image_bucket);

        addRoutes();
        Admin.addRoutes();
    }

    private static void addRoutes() {
        get("/", (req, res) -> { res.redirect("/login"); return "";});
        get("/sessionDemo", (req, res) -> sessionDemo(req, res));
        get("/debugSession", (req, res) -> debugSession(req, res));
        get("/login", (req, res) -> login(req, res));
        post("/login", (req, res) -> loginPost(req, res));
        before("/user/:username/*", (req, res) -> checkUser(req, res));
        get("/user/:username/images", (req, res) -> getUserHome(req, res));
        post("/user/:username/images", (req, res) -> addImage(req, res));
        get("/user/:username/images/:uuid", (req, res) -> getImage(req, res));
        post("/user/:username/images/:uuid", (req, res) -> deleteImage(req, res));
    }

    public static ImageDAO getImageDAO() throws Exception {
        return S3ImageFactory.getImageDAO();
    }

    private static String login(Request req, Response resp) {
        Map<String, Object> model = new HashMap<>();
        return render(model, "login.hbs");
    }

    private static String loginPost(Request req, Response resp) {
        try {
            String username = req.queryParams("username");
            User user = Admin.getUserDAO().getUserByUsername(username);
            if (user == null || !user.getPassword().equals(req.queryParams("password")))
                resp.redirect("/login");
            req.session().attribute("user", username);
            resp.redirect("/user/" + username + "/images");
        } catch (Exception ex) {
            return "[ERR][App.loginPost()]: " + ex.getMessage();
        }

        return "";
    }

    private static String sessionDemo(Request req, Response resp) {
        if (req.session().isNew()) {
            req.session().attribute("value", 0);
        } else {
            req.session().attribute("value", (int) req.session().attribute("value") + 1);
        }

        return "<h1>" + req.session().attribute("value") + "</h1>";
    }

    private static String debugSession(Request req, Response resp) {
        StringBuffer sb = new StringBuffer();
        for (String key : req.session().attributes()) {
            sb.append(key + "->" + req.session().attribute(key) + "<br />");
        }
        return sb.toString();
    }

    private static boolean isUser(String username, String currentUser) {
        return username != null && currentUser != null && username.equals(currentUser);
    }

    private static String checkUser(Request req, Response res) {
        try {
            String username = req.params("username");
            User currentUser = Admin.getUserDAO().getUserByUsername(username);
            if (!isUser(req.session().attribute("user"), username) || currentUser == null) {
                res.redirect("/login");
                halt();
            }
        } catch (Exception ex) {
            return "[ERR][App.checkUser()]: " + ex.getMessage();
        }
        return "";
    }

    private static String getUserHome(Request req, Response res) {
        try {
            Map<String, Object> model = new HashMap<>();
            List<Map<String, Object>> images = new ArrayList<>();
            String username = req.params("username");
            User user = Admin.getUserDAO().getUserByUsername(username);
            model.put("username", user.getUsername());
            List<Image> userImages = getImageDAO().getImages(user);
            if (userImages != null) {
                for (Image i : userImages) {
                    Map<String, Object> imageInfo = new HashMap<>();
                    imageInfo.put("imagedata", i.getImageData());
                    imageInfo.put("uuid", i.getUuid());
                    images.add(imageInfo);
                }
                model.put("images", images);
            }
            return render(model, "userhome.hbs");
        } catch (Exception ex) {
            return "[ERR][App.getUserHome()]: " + ex.getMessage();
        }
    }

    private static String addImage(Request req, Response res) {
        String username = req.params("username");
        try {
            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
            InputStream inputStream = req.raw().getPart("imagedata").getInputStream();
            byte[] imageData = inputStream.readAllBytes();
            String imageDataString = Base64.getEncoder().encodeToString(imageData);
            User user = Admin.getUserDAO().getUserByUsername(username);
            String uuid = UUID.randomUUID().toString();
            Image image = new Image(user, uuid, imageDataString);
            getImageDAO().addImage(user, image);
        } catch (Exception ex) {
            return "[ERR][App.addImage()]: " + ex.getMessage();
        }

        res.redirect("/user/" + username + "/images");
        return "";
    }

    private static String deleteImage(Request req, Response res) {
        try {
            String username = req.params("username");
            User user = Admin.getUserDAO().getUserByUsername(username);
            String uuid = req.params("uuid");
            Image image = getImageDAO().getImage(user, uuid);
            if (image == null)
                return "";
            getImageDAO().deleteImage(user, image);
            res.redirect("/user/" + username + "/images");
        } catch (Exception ex) {
            return "[ERR][App.deleteImage()]: " + ex.getMessage();
        }
        return "";
    }

    private static String getImage(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        String username = req.params("username");
        String uuid = req.params("uuid");
        model.put("username", username);
        model.put("uuid", uuid);
        try {
            User user = Admin.getUserDAO().getUserByUsername(username);
            Image image = getImageDAO().getImage(user, uuid);
            model.put("image", image.getImageData());
        } catch (Exception ex){
            return "[ERR][App.getImage()]: " + ex.getMessage();
        }
        return render(model, "singleimage.hbs");
    }

    public static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine()
                .render(new ModelAndView(model, templatePath));
    }
}
