package edu.uhmanoa.studybuddies;

public class Classmate {
	String name = "";
	String email = "";
	String className ="";
	
	public Classmate(String name, String email, String className) {
		this.name = name;
		this.email = email;
		this.className = className;
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
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public String getClassName() {
		return className;
	}
	
}
