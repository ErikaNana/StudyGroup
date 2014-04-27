package edu.uhmanoa.studybuddies.utils;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.uhmanoa.studybuddies.db.Classmate;


public class JsonUtils{
	
	public static String getJson(ArrayList<Classmate> classmates, String userName, String className) {
		JsonObject json = new JsonObject(); //overall container
		JsonArray jsonArray = new JsonArray(); //array of classmates
		
		for (Classmate classmate : classmates) {
			//create classmate json object
			JsonObject jsonClassmate = new JsonObject();
			jsonClassmate.addProperty("name", classmate.name);
			jsonClassmate.addProperty("email", classmate.email);
			jsonClassmate.addProperty("isMember", classmate.isPendingCreation());
			
			//add to array
			jsonArray.add(jsonClassmate);
		}
				
		JsonObject jsonContainer = new JsonObject();
		jsonContainer.addProperty("user", userName);
		jsonContainer.addProperty("class", className);
		jsonContainer.add("members", jsonArray);
		json.add("createGroup", jsonContainer);
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setPrettyPrinting();
		Gson gson = gsonBuilder.create();
		
		//string formatted json
		return gson.toJson(json);
	}
}
