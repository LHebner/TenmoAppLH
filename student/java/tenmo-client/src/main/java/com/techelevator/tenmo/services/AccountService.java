package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;

import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class AccountService {

    private static String API_BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();

    private AuthenticatedUser authUser = new AuthenticatedUser();

    public AccountService(String url) {
        this.API_BASE_URL = url;
    }

    public HttpHeaders headers(String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return headers;
    }

    public BigDecimal getBalance(String authToken) {
        HttpEntity<?> entity = new HttpEntity<>(headers(authToken));
        ResponseEntity<BigDecimal> balance = restTemplate.exchange(API_BASE_URL + "account/balance",
                HttpMethod.GET, entity, BigDecimal.class);
        return balance.getBody();
    }

    public List<User> getUserList(String authToken) {
        HttpEntity<?> entity = new HttpEntity<>(headers(authToken));
        ResponseEntity <User[]> userList = restTemplate.exchange(API_BASE_URL + "user/all",
                HttpMethod.GET, entity, User[].class);
        return Arrays.asList(userList.getBody());
    }

    private HttpEntity<Account> makeAccountEntity(Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authUser.getToken());
        return new HttpEntity<>(account, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authUser.getToken());
        return new HttpEntity<>(headers);
    }

}
