package com.rest.test;

import org.javalite.activejdbc.Model;
import javax.json.JsonObject;
import javax.json.Json;

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
