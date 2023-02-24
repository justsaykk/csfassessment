package vttp2022.csf.assessment.server.models;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class RestView2 {
    private String name;
    private String resId;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getResId() {
        return resId;
    }
    public void setResId(String resId) {
        this.resId = resId;
    }

    public RestView2(Document doc) {
        this.name = doc.getString("name");
        this.resId = doc.getString("restaurant_id");
    }
    
    public JsonObject toJson() {
        return Json.createObjectBuilder()
        .add("name", this.name)
        .add("id", this.resId)
        .build();
    }
}