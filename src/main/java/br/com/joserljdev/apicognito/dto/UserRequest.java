package br.com.joserljdev.apicognito.dto;

public class UserRequest {
	private String username;
	private String password;
	private String email;
	private String name;

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}
}