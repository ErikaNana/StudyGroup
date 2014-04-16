package edu.uhmanoa.studybuddies.db;

public class Classmate {
	public String name = "";
	public String email = "";
	public String className ="";
	boolean isPendingCreation; //not in a group yet
	boolean isConfirmedForCreation; //was invited
	boolean joined;
	
	public Classmate(String name, String email, String className) {
		this.name = name;
		this.email = email;
		this.className = className;
		this.isPendingCreation = false;
		this.isConfirmedForCreation = false;
		this.joined = false;
	}
	
	public Classmate(String name, String email, String className, int pending, int confirmed, int joined) {
		this.name = name;
		this.email = email;
		this.className = className;
		this.isPendingCreation = false;
		this.isConfirmedForCreation = false;
		this.joined = false;
		
		if (pending == ClassmatesDataSource.PENDING_CREATION) {
			this.isPendingCreation = true;
		}
		if (confirmed == ClassmatesDataSource.CONFIRMED_CREATION) {
			this.isConfirmedForCreation = true;
		}
		if (joined == ClassmatesDataSource.JOINED) {
			this.joined = true;
		}
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
	
	public boolean isPendingCreation() {
		return isPendingCreation;
	}
	public void setPendingCreation(boolean click) {
		if (click) {
			isPendingCreation = true;
		}
		else {
			isPendingCreation = false;
		}
	}
	public boolean isConfirmedCreation() {
		return isConfirmedForCreation;
	}
	
	public void setConfirmedCreation(boolean click) {
		if (click) {
			isConfirmedForCreation = true;
		}
		else {
			isConfirmedForCreation = false;
		}
	}
	
	public boolean isJoined() {
		return joined;
	}
	
	public void setJoined(boolean click) {
		if (click) {
			joined = true;
		}
		else {
			joined = false;
		}
	}

}
