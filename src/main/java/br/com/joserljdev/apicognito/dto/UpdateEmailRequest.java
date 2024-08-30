package br.com.joserljdev.apicognito.dto;

public class UpdateEmailRequest {
	private String username;
	private String newEmail;

	public String getUsername() {
		return username;
	}

	public String getNewEmail() {
		return newEmail;
	}
}