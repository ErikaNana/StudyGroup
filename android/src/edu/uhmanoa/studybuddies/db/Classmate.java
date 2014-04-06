package edu.uhmanoa.studybuddies.db;

public class Classmate {
	public String name = "";
	public String email = "";
	public String className ="";
	boolean isMember;
	
	public Classmate(String name, String email, String className) {
		this.name = name;
		this.email = email;
		this.className = className;
		this.isMember = false;
		
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
	public void setMembership(boolean click) {
		if (click) {
			isMember = true;
		}
		else {
			isMember = false;
		}
	}
	public boolean isMember() {
		return isMember;
	}
}
