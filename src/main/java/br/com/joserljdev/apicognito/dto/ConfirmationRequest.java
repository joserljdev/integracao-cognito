package br.com.joserljdev.apicognito.dto;

public class ConfirmationRequest {
	private String username;
	private String confirmationCode;

	public String getUsername() {
		return username;
	}

	public String getConfirmationCode() {
		return confirmationCode;
	}
}