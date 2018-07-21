package com.rest.test;

import org.javalite.activejdbc.Model;
import javax.json.JsonObject;
import javax.json.Json;

// CREATE TABLE users (
//   id serial NOT NULL,
//   name varchar(255),
//   token varchar(255),
//   created_at timestamp NOT NULL,
//   updated_at timestamp NOT NULL,
//   admin boolean DEFAULT false,
//   email varchar(255)
// )

public class User extends Model {

  public JsonObject toJson() {
    JsonObject user = Json.createObjectBuilder()
      .add("email", this.get("email").toString())
      .add("name", this.get("name").toString())
      .add("id", Integer.parseInt(this.get("id").toString()))
      .build();
    return user;
  }

}
