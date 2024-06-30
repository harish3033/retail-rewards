package com.retail.rewards.it;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.retail.rewards.RetailRewardsApplication;
import com.retail.rewards.model.CustomerModel;
import com.retail.rewards.model.CustomerResponseModel;
import com.retail.rewards.model.exception.ErrorResponseModel;
import com.retail.rewards.util.ErrorConstants;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.retail.rewards.util.ErrorConstants.CUSTOMER_NAME_IS_REQUIRED;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@SpringBootTest(classes = {RetailRewardsApplication.class}, webEnvironment = RANDOM_PORT)
public class CustomerITest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    void saveCustomerModel() {
        CustomerModel customerModel = CustomerModel.builder().name("test-customer-1").deleted(false).build();
        HttpEntity<CustomerModel> httpEntity = new HttpEntity<>(customerModel);
        ResponseEntity<CustomerModel> result = restTemplate.exchange("/api/v1/customers", POST, httpEntity, CustomerModel.class);
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(HttpStatus.OK, result.getStatusCode()),
                () -> assertNotNull(result.getBody().getId())
        );
    }

    @Test
    void saveCustomerModelNoName() {
        CustomerModel customerModel = CustomerModel.builder().deleted(false).build();
        HttpEntity<CustomerModel> httpEntity = new HttpEntity<>(customerModel);
        ResponseEntity<ErrorResponseModel> result = restTemplate.exchange("/api/v1/customers", POST, httpEntity, ErrorResponseModel.class);
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(BAD_REQUEST, result.getStatusCode()),
                () -> assertEquals(CUSTOMER_NAME_IS_REQUIRED, result.getBody().getMessage())
        );
    }

    @Test
    void updateCustomer() {
        // save customer before update
        String name1 = "test-customer-2";
        CustomerModel customerModel = CustomerModel.builder().name(name1).deleted(false).build();
        HttpEntity<CustomerModel> httpEntity = new HttpEntity<>(customerModel);
        ResponseEntity<CustomerModel> result = restTemplate.exchange("/api/v1/customers", POST, httpEntity, CustomerModel.class);
        assertEquals(name1, result.getBody().getName());

        // update customer
        CustomerModel customerModel2 = result.getBody();
        String name2 = "test-customer-2-updated";
        customerModel2.setName(name2);
        HttpEntity<CustomerModel> httpEntity2 = new HttpEntity<>(customerModel2);
        ResponseEntity<CustomerModel> result2 = restTemplate.exchange("/api/v1/customers", PUT, httpEntity2, CustomerModel.class);
        assertEquals(name2, result2.getBody().getName());
    }

    @SneakyThrows
    @Test
    void enableDisableCustomer() {
        // save customer before update
        String name1 = "test-customer-2";
        CustomerModel customerModel = CustomerModel.builder().name(name1).deleted(false).build();
        HttpEntity<CustomerModel> httpEntity = new HttpEntity<>(customerModel);
        ResponseEntity<CustomerModel> result = restTemplate.exchange("/api/v1/customers", POST, httpEntity, CustomerModel.class);
        assertFalse(result.getBody().getDeleted());

        List<String> customerModelIds = List.of(result.getBody().getId());
        HttpEntity<List<String>> httpEntity2 = new HttpEntity<>(customerModelIds);

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("deleted", "true");
        ResponseEntity<String> result2 = restTemplate.exchange("/api/v1/customers/{deleted}", DELETE, httpEntity2, String.class, pathParams);
        List<CustomerModel> customerModelList = OBJECT_MAPPER.readValue(result2.getBody(), new TypeReference<>() {});
        assertTrue(customerModelList.get(0).getDeleted());
    }

    @Test
    void findAllCustomers() {
        CustomerResponseModel customerResponseModel = restTemplate.getForObject("/api/v1/customers?page=0&page=10", CustomerResponseModel.class);
        assertAll(
                () -> assertFalse(customerResponseModel.getCustomers().isEmpty())
        );
    }

}