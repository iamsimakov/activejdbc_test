package com.rest.test;

import org.javalite.activejdbc.Model;
import javax.json.JsonObject;
import javax.json.Json;

// CREATE TABLE notifications (
//   id serial NOT NULL,
//   severity varchar(255),
//   body varchar(255),
//   user_id integer NOT NULL,
//   created_at timestamp NOT NULL,
//   updated_at timestamp NOT NULL
// )

public class Notification extends Model {

  public JsonObject toJson() {
    JsonObject notification = Json.createObjectBuilder()
      .add("id", Integer.parseInt(this.get("id").toString()))
      .add("severity", this.get("severity").toString())
      .add("body", this.get("body").toString())
      .build();
    return notification;
  }

}
