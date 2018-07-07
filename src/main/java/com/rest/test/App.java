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
// import javax.json.JsonArray;
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

        before("/*", (request, response) -> {
            Base.open("com.mysql.cj.jdbc.Driver", "jdbc:mysql://localhost:3306/converter_api?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
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
}
