package br.com.joserljdev.apicognito.dto;

public class ResetPasswordRequest {
	private String username;
	private String newPassword;

	public String getUsername() {
		return username;
	}

	public String getNewPassword() {
		return newPassword;
	}
}