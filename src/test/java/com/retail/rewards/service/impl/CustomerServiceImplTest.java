package com.retail.rewards.service.impl;

import com.retail.rewards.entity.CustomerEntity;
import com.retail.rewards.model.CustomerModel;
import com.retail.rewards.model.CustomerResponseModel;
import com.retail.rewards.repository.CustomerRepository;
import com.retail.rewards.util.Util;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Spy
    private Util util;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private Page<CustomerEntity> customerEntityPage;

    private CustomerModel buildCustomerModel() {
        return CustomerModel.builder()
                .id(UUID.randomUUID().toString())
                .name("customer-1")
                .deleted(false)
                .build();
    }

    private CustomerEntity buildCustomerEntity() {
        return CustomerEntity.builder()
                .id(UUID.randomUUID().toString())
                .name("saved-customer-1")
                .deleted(false)
                .build();
    }

    @Test
    void saveCustomer() {
        CustomerModel customerModel = buildCustomerModel();
        CustomerEntity customerEntity = buildCustomerEntity();
        when(customerRepository.save(any())).thenReturn(customerEntity);
        CustomerModel result = customerService.saveCustomer(customerModel);
        assertAll(
                () -> assertNotNull(result)
        );
    }

    @Test
    void updateCustomer() {
        CustomerModel customerModel = buildCustomerModel();
        CustomerEntity customerEntity = buildCustomerEntity();
        when(customerRepository.findById(anyString())).thenReturn(Optional.ofNullable(customerEntity));
        when(customerRepository.save(any())).thenReturn(customerEntity);
        CustomerModel result = customerService.updateCustomer(customerModel);
        assertAll(
                () -> assertNotNull(result)
        );
    }

    @Test
    void enableDisableCustomer() {
        List<String> customerModelIds = List.of(UUID.randomUUID().toString());
        Boolean deleted = false;
        CustomerEntity customerEntity = buildCustomerEntity();
        List<CustomerEntity> customerEntities = List.of(customerEntity);
        when(customerRepository.findAllById(anyList())).thenReturn(customerEntities);
        when(customerRepository.saveAll(any())).thenReturn(customerEntities);
        List<CustomerModel> result = customerService.enableDisableCustomer(customerModelIds, deleted);
        assertAll(
                () -> assertNotNull(result)
        );
    }

    @Test
    void findAllCustomers() {
        Integer page = 0;
        Integer size = 10;
        CustomerEntity customerEntity = buildCustomerEntity();
        List<CustomerEntity> customerEntities = List.of(customerEntity);
        when(customerRepository.findAll(any(Pageable.class))).thenReturn(customerEntityPage);
        when(customerEntityPage.getContent()).thenReturn(customerEntities);
        when(customerEntityPage.getTotalPages()).thenReturn(10);
        when(customerEntityPage.getTotalElements()).thenReturn(200L);
        CustomerResponseModel result = customerService.findAllCustomers(page, size);
        assertAll(
                () -> assertNotNull(result)
        );
    }

}