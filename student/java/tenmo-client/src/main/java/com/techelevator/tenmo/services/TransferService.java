package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class TransferService {

    private static String API_BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();

    private AuthenticatedUser authUser = new AuthenticatedUser();

    public TransferService(String url) {
        this.API_BASE_URL = url;
    }

    public HttpHeaders headers(String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return headers;
    }

    public boolean transferFrom(String authToken) {
        HttpEntity<?> entity = new HttpEntity<>(headers(authToken));
        ResponseEntity<Boolean> response = restTemplate.exchange(API_BASE_URL + "transfer/",
                HttpMethod.POST, entity, Boolean.class);
        return response.getBody();
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authUser.getToken());
        return new HttpEntity<>(transfer, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authUser.getToken());
        return new HttpEntity<>(headers);
    }
}
