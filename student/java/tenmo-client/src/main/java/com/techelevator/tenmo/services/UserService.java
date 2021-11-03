package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class UserService {

    private static String API_BASE_URL = "http://localhost:8080/user/";
    private final RestTemplate restTemplate = new RestTemplate();

    private AuthenticatedUser authUser = new AuthenticatedUser();

    public UserService(String url) {
        this.API_BASE_URL = url;
    }

    public BigDecimal getBalance(AuthenticatedUser user) {
        AuthenticatedUser balance = null;
        ResponseEntity<AuthenticatedUser> response =
                restTemplate.exchange(API_BASE_URL + user.getUser().getId() + "/balance/", HttpMethod.GET,
                        makeAuthEntity(), AuthenticatedUser.class);
        balance = response.getBody();

        if (balance != null) {
            return balance.getUser().getBalance();
        } else return BigDecimal.valueOf(0);
    }

    private HttpEntity<User> makeUserEntity(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authUser.getToken());
        return new HttpEntity<>(user, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authUser.getToken());
        return new HttpEntity<>(headers);
    }

}
