package com.aoa.app.models;

import java.io.Serializable;

public class People implements Serializable {
	
	public String name;
	public String secondname;
	public int age;


	public People(String name, String secondname, int age) {
		this.name = name;
		this.secondname = secondname;
		this.age = age;
	}
}