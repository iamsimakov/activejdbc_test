package com.rest.test;

import static spark.Spark.*;
import org.javalite.activejdbc.Base;

import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
// import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;

public class App {
    public static void main(String[] args) {
        before("/*", (request, response) -> {
            Base.open("com.mysql.cj.jdbc.Driver", "jdbc:mysql://localhost:3306/converter_api?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
        });
        after("/*", (request, response) -> {
            Base.close();
        });
        get("/users/:id", (request, response) -> {
            User u = User.findFirst("id = ?", request.params(":id"));
            List<User> users = new ArrayList<>();
            users.add(u);
            response.type("application/json");
            return listUsersToJson(users);
        });
        get("/users", (request, response) -> {
            List<User> users = User.findAll();
            response.type("application/json");
            return listUsersToJson(users);
        });
    }

    public static String listUsersToJson(List<User> users) {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        users.forEach(u -> {
            builder.add(u.toJson());
        });
        return builder.build().toString();
    }
}
