package de.msg.javatraining.donationmanager.controller.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SignInResponse {
	private String token;
	private String type = "Bearer";
	private Long id;
	private String username;
	private String email;
	private List<String> permissions;
	private Boolean newUser;
	private Boolean disabled;

	public SignInResponse(String accessToken, Long id, String username, String email, List<String> permissions,Boolean newUser,Boolean disabled) {
		this.token = accessToken;
		this.id = id;
		this.username = username;
		this.email = email;
		this.permissions = permissions;
		this.newUser = newUser;
		this.disabled = disabled;
	}
}
