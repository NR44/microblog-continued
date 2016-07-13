import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nigel on 7/11/16.
 */
public class Main {

    public static final String SESSION_USERNAME = "user";
    public static final String SESSION_PASSWORD = "password";

    static Map<String, User> users = new HashMap<>();
    public static void main(String[] args) {
        Spark.init();
        Spark.get("/",((request, response) -> {

            Session session = request.session();
            String name = session.attribute(SESSION_USERNAME);
            String password = session.attribute(SESSION_PASSWORD);


            HashMap templateData = new HashMap();
            User user = new User(name,password);
            ModelAndView showPage = null;

            if (user.getPassword() == null) {
                showPage = new ModelAndView(templateData, "login.html");
            }else if(users.containsKey(name)){
                User checkUser = users.get(name);
                if (user.getPassword().equals(checkUser.getPassword())) {
                    user = users.get(checkUser.getName());
                    templateData.put("user", user );
                    showPage = new ModelAndView(templateData, "messages.html");
                }

            }else{
                showPage = new ModelAndView(templateData, "create.html");
            }
            return showPage;
        }), new MustacheTemplateEngine());

        Spark.post("/login", ((request, response) -> {
            String name = request.queryParams("loginName");
            String password = request.queryParams("loginPassword");
            Session session = request.session();

            session.attribute(SESSION_USERNAME, name);
            session.attribute(SESSION_PASSWORD, password);

            response.redirect("/");
            return "";
            })
        );

        Spark.post("/logout", (request, response) -> {
            request.session().invalidate();
            response.redirect("/");
            return "";
        });

        Spark.post("/create-message", ((request, response) -> {
            Session session = request.session();
            String input = request.queryParams("message");

            User user = users.get(session.attribute(SESSION_USERNAME));

            user.addMessage(input);
            response.redirect("/");
            return "";
            })
        );

        Spark.post("/create-user", ((request, response) -> {
            String name = request.queryParams("userName");
            String password = request.queryParams("userPassword");

            User user = new User(name, password);
            users.put(name,user);

            Session session = request.session();
            session.attribute(SESSION_USERNAME, name);
            session.attribute(SESSION_PASSWORD, password);

            response.redirect("/");
            return "";
            })
        );

        Spark.post("/delete-message", ((request, response) -> {
            Session session = request.session();
            String name = session.attribute(SESSION_USERNAME);
            String password = session.attribute(SESSION_PASSWORD);

            int lineNum = Integer.parseInt(request.queryParams("deleteNum"));

            User user = users.get(name);
            user.deleteMessage(lineNum);

            response.redirect("/");

            return "";
            })
        );

        Spark.post("/edit-message", ((request, response) -> {
            Session session = request.session();
            String name = session.attribute(SESSION_USERNAME);
            String password = session.attribute(SESSION_PASSWORD);

            int lineNum = Integer.parseInt(request.queryParams("lineNum"));
            Message lineMessage = new Message(request.queryParams("messageEdit"));

            User user = users.get(name);
            user.editMessage(lineNum, lineMessage);

            response.redirect("/");
            return "";
            })
        );
    }
}