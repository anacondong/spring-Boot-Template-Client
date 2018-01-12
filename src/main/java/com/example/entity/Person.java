/**
 * 
 */
package com.example.entity;

/**
 * @author Ittipol
 *
 */
public class Person {
	
	private Integer id;
	private String name;
	private String email;
	
	
	public Person(){}
	
	public Person(String email,String name) {
		super();
		this.name = name;
		this.email = email;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + ", email=" + email + "]";
	}

	
	
}
