package com.rest.test;

import static spark.Spark.*;
import org.javalite.activejdbc.Base;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArrayBuilder;

public class App {
    private static FreeMarkerEngine freeMarkerEngine = null;

    private static void initConfig() {
        Configuration freeMarkerConfiguration = new Configuration(Configuration.VERSION_2_3_28);
        freeMarkerConfiguration.setTemplateLoader(new ClassTemplateLoader(App.class, "/"));
        freeMarkerEngine = new FreeMarkerEngine(freeMarkerConfiguration);
    }

    public static void main(String[] args) {
        initConfig();

        port(80);

        before("/*", (request, response) -> {
            Base.open("com.mysql.cj.jdbc.Driver", "jdbc:mysql://localhost:3306/notification_app?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
        });
        after("/*", (request, response) -> {
            Base.close();
        });
        // api
        get("/api/users/:id", (request, response) -> {
            User u = User.findFirst("id = ?", request.params(":id"));
            List<User> users = new ArrayList<>();
            users.add(u);
            response.type("application/json");
            return listUsersToJson(users);
        });
        get("/api/users", (request, response) -> {
            List<User> users = User.findAll();
            response.type("application/json");
            return listUsersToJson(users);
        });
        // create users, notifications
        get("/create_user", (request, response) -> {
            if (!request.queryParams("admin_token_access").equals("987654321")) {
                return "Incorrect admin credentials";
            } 
            User u = new User();
            u.set("name", request.queryParams("name"));
            u.set("email", request.queryParams("email"));
            u.set("token", request.queryParams("token"));
            u.saveIt();
            return "OK";
        });
        get("/send_notification", (request, response) -> {
            User u = User.findFirst("token = ?", request.queryParams("token"));
            if (u == null) {
                return "Incorrect credentials";
            }
            Notification n = new Notification();
            n.set("severity", request.queryParams("severity"));
            n.set("body", request.queryParams("body"));
            n.set("user_id", u.get("id"));
            n.saveIt();
            return "OK";
        });
        // html
        get("/users/:id", (request, response) -> {
            User u = User.findFirst("id = ?", request.params(":id"));
            List<User> users = new ArrayList<>();
            users.add(u);
            return listUsersToHtml(users);
        });
        get("/users", (request, response) -> {
            List<User> users = User.findAll();
            return listUsersToHtml(users);
        });
        get("/notifications", (request, response) -> {
            List<Notification> notifications = Notification.findAll();
            return listNotificationsToHtml(notifications);
        });
    }

    public static String listUsersToJson(List<User> users) {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        users.forEach(u -> {
            builder.add(u.toJson());
        });
        return builder.build().toString();
    }

    public static String listUsersToHtml(List<User> users) {
        Map<String, List<User>> model = new HashMap<>();
        model.put("users", users);
        return freeMarkerEngine.render(new ModelAndView(model, "users.ftl"));
    }

    public static String listNotificationsToHtml(List<Notification> notifications) {
        Map<String, List<Notification>> model = new HashMap<>();
        model.put("notifications", notifications);
        return freeMarkerEngine.render(new ModelAndView(model, "notifications.ftl"));
    }
}
