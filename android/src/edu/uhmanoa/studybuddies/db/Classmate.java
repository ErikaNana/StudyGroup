package edu.uhmanoa.studybuddies.db;

public class Classmate {
	public String name = "";
	public String email = "";
	public String className ="";
	boolean isPendingCreation;
	boolean isConfirmedForCreation;
	
	public Classmate(String name, String email, String className) {
		this.name = name;
		this.email = email;
		this.className = className;
		this.isPendingCreation = false;
		this.isConfirmedForCreation = false;
	}
	
	public Classmate(String name, String email, String className, int pending, int confirmed) {
		this.name = name;
		this.email = email;
		this.className = className;
		this.isPendingCreation = false;
		this.isConfirmedForCreation = false;
		if (pending == ClassmatesDataSource.PENDING_CREATION) {
			this.isPendingCreation = true;
		}
		if (confirmed == ClassmatesDataSource.CONFIRMED_CREATION) {
			this.isConfirmedForCreation = true;
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

}
