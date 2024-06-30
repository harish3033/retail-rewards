package com.retail.rewards.it;

import com.retail.rewards.RetailRewardsApplication;
import com.retail.rewards.model.CustomerModel;
import com.retail.rewards.model.RewardsResponseModel;
import com.retail.rewards.model.TransactionModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.retail.rewards.util.Constants.DATE_TIME_FORMAT_FOR_JSON_2;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.POST;

@SpringBootTest(classes = {RetailRewardsApplication.class}, webEnvironment = RANDOM_PORT)
public class TransactionControllerITest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void saveTransaction() {
        // save customer
        CustomerModel customerModel = CustomerModel.builder().name("test-customer-1").deleted(false).build();
        HttpEntity<CustomerModel> httpEntity = new HttpEntity<>(customerModel);
        ResponseEntity<CustomerModel> result = restTemplate.exchange("/api/v1/customers", POST, httpEntity, CustomerModel.class);

        // save transaction
        TransactionModel transactionModel = TransactionModel.builder()
                .customerId(result.getBody().getId())
                .transactionDate(new Date())
                .billAmount(120)
                .deleted(false)
                .build();
        HttpEntity<TransactionModel> transactionModelHttpEntity = new HttpEntity<>(transactionModel);
        ResponseEntity<TransactionModel> transactionModelResponseEntity = restTemplate.exchange("/api/v1/transactions", POST, transactionModelHttpEntity, TransactionModel.class);
        assertAll(
                () -> assertNotNull(transactionModelResponseEntity.getBody().getId()),
                () -> assertNotNull(transactionModelResponseEntity.getBody().getCustomer())
        );
    }

    @Test
    void updateTransaction() {
        // save customer
        CustomerModel customerModel = CustomerModel.builder().name("test-customer-1").deleted(false).build();
        HttpEntity<CustomerModel> httpEntity = new HttpEntity<>(customerModel);
        ResponseEntity<CustomerModel> result = restTemplate.exchange("/api/v1/customers", POST, httpEntity, CustomerModel.class);

        // save transaction
        TransactionModel transactionModel = TransactionModel.builder()
                .customerId(result.getBody().getId())
                .transactionDate(new Date())
                .billAmount(120)
                .deleted(false)
                .build();
        HttpEntity<TransactionModel> transactionModelHttpEntity = new HttpEntity<>(transactionModel);
        ResponseEntity<TransactionModel> transactionModelResponseEntity = restTemplate.exchange("/api/v1/transactions", POST, transactionModelHttpEntity, TransactionModel.class);
        assertAll(
                () -> assertNotNull(transactionModelResponseEntity.getBody().getId()),
                () -> assertNotNull(transactionModelResponseEntity.getBody().getCustomer()),
                () -> assertEquals(90, transactionModelResponseEntity.getBody().getRewardPoints())
        );

        // update customer
        TransactionModel transactionModel2 = TransactionModel.builder()
                .id(transactionModel.getId())
                .customerId(result.getBody().getId())
                .transactionDate(new Date())
                .billAmount(130)
                .deleted(false)
                .build();
        HttpEntity<TransactionModel> transactionModelHttpEntity2 = new HttpEntity<>(transactionModel2);
        ResponseEntity<TransactionModel> transactionModelResponseEntity2 = restTemplate.exchange("/api/v1/transactions", POST, transactionModelHttpEntity2, TransactionModel.class);
        assertAll(
                () -> assertNotNull(transactionModelResponseEntity2.getBody().getId()),
                () -> assertNotNull(transactionModelResponseEntity2.getBody().getCustomer()),
                () -> assertEquals(110, transactionModelResponseEntity2.getBody().getRewardPoints())
        );
    }

    @Test
    void findTransactionsByCustomer() {
        // save customer
        CustomerModel customerModel = CustomerModel.builder().name("test-customer-1").deleted(false).build();
        HttpEntity<CustomerModel> httpEntity = new HttpEntity<>(customerModel);
        ResponseEntity<CustomerModel> result = restTemplate.exchange("/api/v1/customers", POST, httpEntity, CustomerModel.class);

        // save transaction
        TransactionModel transactionModel = TransactionModel.builder()
                .customerId(result.getBody().getId())
                .transactionDate(new Date())
                .billAmount(120)
                .deleted(false)
                .build();
        HttpEntity<TransactionModel> transactionModelHttpEntity = new HttpEntity<>(transactionModel);
        ResponseEntity<TransactionModel> transactionModelResponseEntity = restTemplate.exchange("/api/v1/transactions", POST, transactionModelHttpEntity, TransactionModel.class);
        assertAll(
                () -> assertNotNull(transactionModelResponseEntity.getBody().getId()),
                () -> assertNotNull(transactionModelResponseEntity.getBody().getCustomer()),
                () -> assertEquals(90, transactionModelResponseEntity.getBody().getRewardPoints())
        );

        // find transactions by customer id
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("customerId", result.getBody().getId());
        RewardsResponseModel rewardsResponseModel = restTemplate.getForObject("/api/v1/transactions/{customerId}?page=0&page=10", RewardsResponseModel.class, pathParams);
        assertAll(
                () -> assertFalse(rewardsResponseModel.getResponse().isEmpty()),
                () -> assertEquals(90, rewardsResponseModel.getResponse().get(0).getTotalRewardPoints())
        );
    }

    @Test
    void findAllTransactions() {
        // save customer
        CustomerModel customerModel = CustomerModel.builder().name("test-customer-1").deleted(false).build();
        HttpEntity<CustomerModel> httpEntity = new HttpEntity<>(customerModel);
        ResponseEntity<CustomerModel> result = restTemplate.exchange("/api/v1/customers", POST, httpEntity, CustomerModel.class);

        // save transaction
        TransactionModel transactionModel = TransactionModel.builder()
                .customerId(result.getBody().getId())
                .transactionDate(new Date())
                .billAmount(120)
                .deleted(false)
                .build();
        HttpEntity<TransactionModel> transactionModelHttpEntity = new HttpEntity<>(transactionModel);
        ResponseEntity<TransactionModel> transactionModelResponseEntity = restTemplate.exchange("/api/v1/transactions", POST, transactionModelHttpEntity, TransactionModel.class);

        // find all transactions
        RewardsResponseModel rewardsResponseModel = restTemplate.getForObject("/api/v1/transactions", RewardsResponseModel.class);
        assertAll(
                () -> assertFalse(rewardsResponseModel.getResponse().isEmpty())
        );
    }

    @Test
    void findTransactionsByDateRange() {
        // save customer
        CustomerModel customerModel = CustomerModel.builder().name("test-customer-1").deleted(false).build();
        HttpEntity<CustomerModel> httpEntity = new HttpEntity<>(customerModel);
        ResponseEntity<CustomerModel> result = restTemplate.exchange("/api/v1/customers", POST, httpEntity, CustomerModel.class);

        // save transaction
        Date today = new Date();
        TransactionModel transactionModel = TransactionModel.builder()
                .customerId(result.getBody().getId())
                .transactionDate(today)
                .billAmount(120)
                .deleted(false)
                .build();
        HttpEntity<TransactionModel> transactionModelHttpEntity = new HttpEntity<>(transactionModel);
        ResponseEntity<TransactionModel> transactionModelResponseEntity = restTemplate.exchange("/api/v1/transactions", POST, transactionModelHttpEntity, TransactionModel.class);

        // find transactions by date range
        DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_FOR_JSON_2);

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("fromDate", dateFormat.format(today));
        pathParams.put("toDate", dateFormat.format(today));
        RewardsResponseModel rewardsResponseModel = restTemplate.getForObject("/api/v1/transactions/{fromDate}/{toDate}", RewardsResponseModel.class, pathParams);
        assertAll(
                () -> assertFalse(rewardsResponseModel.getResponse().isEmpty())
        );
    }
}
