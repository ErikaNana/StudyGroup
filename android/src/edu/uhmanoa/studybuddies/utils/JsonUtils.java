package edu.uhmanoa.studybuddies.utils;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.uhmanoa.studybuddies.db.Classmate;


public class JsonUtils{
	
	public static String getJson(ArrayList<Classmate> classmates) {
		JsonObject json = new JsonObject(); //overall container
		JsonArray jsonArray = new JsonArray(); //array of classmates
		
		for (Classmate classmate : classmates) {
			//create classmate json object
			JsonObject jsonClassmate = new JsonObject();
			jsonClassmate.addProperty("name", classmate.name);
			jsonClassmate.addProperty("className", classmate.className);
			jsonClassmate.addProperty("email", classmate.email);
			jsonClassmate.addProperty("isMember", classmate.isMember());
			
			//add to array
			jsonArray.add(jsonClassmate);
		}
		json.addProperty("groupMembers", classmates.get(0).getClassName());
		json.add("members", jsonArray);
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setPrettyPrinting();
		Gson gson = gsonBuilder.create();
		
		//string formatted json
		return gson.toJson(json);
	}
}
