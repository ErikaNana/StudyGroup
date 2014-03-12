package edu.uhmanoa.studybuddies;

public class Classmate {
	String name = "";
	String email = "";
	
	public Classmate(String name, String email) {
		this.name = name;
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}
	
	public String toString() {
		return name + " Email: " + email;
	}
}
