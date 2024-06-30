package com.retail.rewards.service.impl;

import com.retail.rewards.entity.CustomerEntity;
import com.retail.rewards.entity.TransactionEntity;
import com.retail.rewards.model.CustomerModel;
import com.retail.rewards.model.RewardsResponseModel;
import com.retail.rewards.model.TransactionModel;
import com.retail.rewards.model.exception.AppException;
import com.retail.rewards.repository.CustomerRepository;
import com.retail.rewards.repository.TransactionRepository;
import com.retail.rewards.util.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.retail.rewards.util.ErrorConstants.CUSTOMER_ID_REQUIRED;
import static com.retail.rewards.util.ErrorConstants.INVALID_BILL_AMOUNT;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionServiceImpl;

    @Spy
    private Util util;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;

    private TransactionEntity buildTransactionEntity() {
        return TransactionEntity.builder()
                .id(UUID.randomUUID().toString())
                .transactionDate(new Date())
                .billAmount(120)
                .deleted(false)
                .build();
    }

    private TransactionModel buildTransactionModel() {
        return TransactionModel.builder()
                .id(UUID.randomUUID().toString())
                .customerId(UUID.randomUUID().toString())
                .transactionDate(new Date())
                .billAmount(120)
                .deleted(false)
                .build();
    }

    private CustomerEntity buildCustomerEntity() {
        return CustomerEntity.builder()
                .id(UUID.randomUUID().toString())
                .name("customer-1")
                .deleted(false)
                .build();
    }

    private CustomerModel buildCustomerModel() {
        return CustomerModel.builder()
                .id(UUID.randomUUID().toString())
                .name("customer-1")
                .deleted(false)
                .build();
    }

    @Test
    void saveTransaction() {
        TransactionModel transactionModel = buildTransactionModel();
        TransactionEntity transactionEntity = buildTransactionEntity();
        when(transactionRepository.findById(anyString())).thenReturn(Optional.ofNullable(transactionEntity));
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(transactionEntity);

        CustomerEntity customerEntity = buildCustomerEntity();
        when(customerRepository.findById(anyString())).thenReturn(Optional.ofNullable(customerEntity));

        TransactionModel result = transactionServiceImpl.saveUpdateTransaction(transactionModel);
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(90, result.getRewardPoints())
        );
    }

    @Test
    void updateTransaction() {
        TransactionModel transactionModel = buildTransactionModel();
        transactionModel.setId(null);
        TransactionEntity transactionEntity = buildTransactionEntity();
        transactionEntity.setRewardPoints(90);
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(transactionEntity);

        CustomerEntity customerEntity = buildCustomerEntity();
        when(customerRepository.findById(anyString())).thenReturn(Optional.ofNullable(customerEntity));

        TransactionModel result = transactionServiceImpl.saveUpdateTransaction(transactionModel);
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(90, result.getRewardPoints())
        );
    }

    @Test
    void saveTransactionInvalidCustomerId() {
        TransactionModel transactionModel = buildTransactionModel();
        transactionModel.setCustomerId(null);
        TransactionEntity transactionEntity = buildTransactionEntity();
        when(transactionRepository.findById(anyString())).thenReturn(Optional.ofNullable(transactionEntity));

        Exception exception = Assertions.assertThrows(AppException.class, () -> {
            transactionServiceImpl.saveUpdateTransaction(transactionModel);
        });
        assertEquals(exception.getMessage(), String.format(CUSTOMER_ID_REQUIRED));
    }

    @Test
    void saveTransactionInvalidBillAmount() {
        TransactionModel transactionModel = buildTransactionModel();
        transactionModel.setBillAmount(-1);

        Exception exception = Assertions.assertThrows(AppException.class, () -> {
            transactionServiceImpl.saveUpdateTransaction(transactionModel);
        });
        assertEquals(exception.getMessage(), String.format(INVALID_BILL_AMOUNT, -1));
    }

    @Test
    void findTransactionsByCustomer() {
        TransactionEntity transactionEntity = buildTransactionEntity();
        List<TransactionEntity> transactionEntities = List.of(transactionEntity);
        CustomerEntity customerEntity = buildCustomerEntity();
        customerEntity.setTransactionEntities(transactionEntities);
        when(customerRepository.findById(anyString())).thenReturn(Optional.ofNullable(customerEntity));

        RewardsResponseModel rewardsResponseModel = transactionServiceImpl.findTransactionsByCustomer(UUID.randomUUID().toString());
        assertAll(
                () -> assertNotNull(rewardsResponseModel.getResponse()),
                () -> Assertions.assertFalse(rewardsResponseModel.getResponse().isEmpty())
        );
    }

    @Test
    void findAllTransactions() {
        TransactionEntity transactionEntity = buildTransactionEntity();
        List<TransactionEntity> transactionEntities = List.of(transactionEntity);
        CustomerEntity customerEntity = buildCustomerEntity();
        customerEntity.setTransactionEntities(transactionEntities);
        List<CustomerEntity> customerEntities = List.of(customerEntity);
        when(customerRepository.findAll()).thenReturn(customerEntities);
        RewardsResponseModel rewardsResponseModel = transactionServiceImpl.findAllTransactions();
        assertAll(
                () -> assertNotNull( rewardsResponseModel),
                () -> assertNotNull(rewardsResponseModel.getResponse()),
                () -> Assertions.assertFalse(rewardsResponseModel.getResponse().isEmpty())
        );
    }

    @Test
    void findTransactionsByDateRange() {
        String fromDateStr = "20062024";
        String toDateStr = "21062024";
        TransactionEntity transactionEntity = buildTransactionEntity();
        CustomerEntity customerEntity = buildCustomerEntity();
        transactionEntity.setCustomerEntity(customerEntity);
        List<TransactionEntity> transactionEntities = List.of(transactionEntity);
        when(transactionRepository.findByTransactionDateBetween(any(), any())).thenReturn(transactionEntities);
        RewardsResponseModel responseModel = transactionServiceImpl.findTransactionsByDateRange(fromDateStr, toDateStr);
    }

    @Test
    void calculateRewards() {
        Integer result1 = transactionServiceImpl.calculateRewards(-1);
        assertEquals(0, result1);

        Integer result2 = transactionServiceImpl.calculateRewards(0);
        assertEquals(0, result2);

        Integer result3 = transactionServiceImpl.calculateRewards(10);
        assertEquals(0, result3);

        Integer result4 = transactionServiceImpl.calculateRewards(50);
        assertEquals(0, result4);

        Integer result5 = transactionServiceImpl.calculateRewards(60);
        assertEquals(10, result5);

        Integer result6 = transactionServiceImpl.calculateRewards(70);
        assertEquals(20, result6);

        Integer result7 = transactionServiceImpl.calculateRewards(100);
        assertEquals(50, result7);

        Integer result8 = transactionServiceImpl.calculateRewards(110);
        assertEquals(70, result8);

        Integer result9 = transactionServiceImpl.calculateRewards(120);
        assertEquals(90, result9);

        Integer result10 = transactionServiceImpl.calculateRewards(130);
        assertEquals(110, result10);
    }
}
