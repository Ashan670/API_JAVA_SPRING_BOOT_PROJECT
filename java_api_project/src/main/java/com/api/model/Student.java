package com.api.model;

public class Student {
	    
	    private int id;
	    private String name;
	    private int age;
	    private String grade;
	    private int endpointId;
	    
	    
		public Student(int id, String name, int age, String grade, int endpointId) {
			this.id = id;
			this.name = name;
			this.age = age;
			this.grade = grade;
			this.endpointId = endpointId;
		}



		public Student() {
			
		}



		public int getId() {
			return id;
		}


		public void setId(int id) {
			this.id = id;
		}


		public String getName() {
			return name;
		}


		public void setName(String name) {
			this.name = name;
		}


		public int getAge() {
			return age;
		}


		public void setAge(int age) {
			this.age = age;
		}


		public String getGrade() {
			return grade;
		}


		public void setGrade(String grade) {
			this.grade = grade;
		}


		public int getEndpointId() {
			return endpointId;
		}


		public void setEndpointId(int endpointId) {
			this.endpointId = endpointId;
		}
	    
	    
	
}
