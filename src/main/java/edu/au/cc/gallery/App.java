/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.au.cc.gallery;

import edu.au.cc.gallery.tools.UserAdmin;
import edu.au.cc.gallery.tools.WebAdmin;

public class App {
   /* 
    public static void main(String[] args) throws Exception {
//	DB.demo();
//        UserAdmin.printMenu();
        UserAdmin.run();        
    }
   */ 

/*	
    public static void main(String[] args) throws Exception {
	port(5000);
	get("/hello", (req, res) -> "Hello World");
	get("/goodbye", (req, res) -> "goodbye");
	get("/greet/:name", (req, res) -> {
		return "Hello: " + req.params(":name"); 
	});
	// localhost:5000/add?x=5&y=10
	post("/add", (req, res) -> "The sum is " + (Integer.parseInt(req.queryParams("x")) + Integer.parseInt(req.queryParams("y"))));
	get("/calculator", (req, res) -> {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("name", "Fred");
		return new HandlebarsTemplateEngine()
			.render(new ModelAndView(model, "calculator.hbs"));
	});

    } 
    */

    public static void main(String[] args) throws Exception {
	String portString = System.getenv("JETTY_PORT");
	int port = 5000;
	if (portString != null || ! portString.equals(""))
	    port = Integer.parseInt(portString);
	WebAdmin.run(port);
    }
}
