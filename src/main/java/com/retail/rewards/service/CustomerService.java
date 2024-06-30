package com.retail.rewards.service;

import com.retail.rewards.entity.CustomerEntity;
import com.retail.rewards.model.CustomerModel;
import com.retail.rewards.model.CustomerResponseModel;
import lombok.SneakyThrows;

import java.util.List;

public interface CustomerService {

    /**
     * Save customer
     * @param customerModel {@link CustomerModel}
     * @return {@link CustomerModel}
     */
    @SneakyThrows
    CustomerModel saveCustomer(CustomerModel customerModel);

    /**
     * Update customer
     * @param customerModel {@link CustomerModel}
     * @return {@link CustomerModel}
     */
    CustomerModel updateCustomer(CustomerModel customerModel);

    /**
     * Enable disable customer
     * @param customerModelIds {@link List} of {@link CustomerModel}
     * @param deleted - delete status
     * @return {@link List} of {@link CustomerModel}
     */
    List<CustomerModel> enableDisableCustomer(List<String> customerModelIds, Boolean deleted);

    /**
     * Find all customers with pagination
     * @param page Page number
     * @param size number of records in each page
     * @return {@link CustomerResponseModel}
     */
    @SneakyThrows
    CustomerResponseModel findAllCustomers(Integer page, Integer size);
}