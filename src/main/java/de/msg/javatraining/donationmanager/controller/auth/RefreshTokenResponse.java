package de.msg.javatraining.donationmanager.controller.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RefreshTokenResponse {

    private String renewedAccesToken;
}
