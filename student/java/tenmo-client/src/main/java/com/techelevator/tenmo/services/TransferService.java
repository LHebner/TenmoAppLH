package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserCredentials;
import org.springframework.http.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

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

    public Transfer transfer(Transfer transfer) {
        HttpEntity<?> entity = makeTransferEntity(transfer);
        ResponseEntity<Transfer> newTransfer = null;
        try {
            newTransfer = restTemplate.exchange(API_BASE_URL + "transfer/",
                    HttpMethod.POST, entity, Transfer.class);
        } catch (RestClientResponseException e) {}

        return newTransfer.getBody();
    }

    public List<Transfer> getTransferList(String authToken) {
        HttpEntity<?> entity = new HttpEntity<>(headers(authToken));
        ResponseEntity<Transfer[]> transferList = restTemplate.exchange(API_BASE_URL + "transfer/all",
                HttpMethod.GET, entity, Transfer[].class);
        return Arrays.asList(transferList.getBody());
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
