package com.retail.rewards.service.impl;

import com.retail.rewards.entity.CustomerEntity;
import com.retail.rewards.model.CustomerModel;
import com.retail.rewards.model.CustomerResponseModel;
import com.retail.rewards.repository.CustomerRepository;
import com.retail.rewards.service.CustomerService;
import com.retail.rewards.util.ErrorConstants;
import com.retail.rewards.util.Util;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.retail.rewards.util.Constants.NAME;
import static com.retail.rewards.util.ErrorConstants.CUSTOMER_ID_REQUIRED;
import static com.retail.rewards.util.ErrorConstants.CUSTOMER_NAME_IS_REQUIRED;
import static com.retail.rewards.util.ErrorConstants.CUSTOMER_NOT_FOUND_BY_ID;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private Util util;

    private CustomerRepository customerRepository;

    /**
     * Save {@link CustomerEntity}
     * @param customer {@link CustomerEntity}
     * @return {@link CustomerEntity}
     */
    private CustomerEntity saveCustomerEntity(CustomerEntity customer) {
        log.info("Saving customer entity={}", customer);
        CustomerEntity customerEntity = customerRepository.save(customer);
        log.info("Saved customer entity={}", customerEntity);
        return customerEntity;
    }

    /**
     * Save {@link List} of {@link CustomerEntity}
     * @param customerEntityList {@link List} of {@link CustomerEntity}
     * @return {@link List} of {@link CustomerEntity}
     */
    private List<CustomerEntity> saveCustomerEntityList(List<CustomerEntity> customerEntityList) {
        log.info("Saving customer entity list={}", customerEntityList);
        List<CustomerEntity> customerEntities = customerRepository.saveAll(customerEntityList);
        log.info("Saved customer entity list={}", customerEntities);
        return customerEntities;
    }

    /**
     *  Save customer
     * @param customerModel {@link CustomerModel}
     * @param customerEntity {@link CustomerEntity}
     * @return {@link CustomerModel}
     */
    private CustomerModel _saveCustomer(CustomerModel customerModel, CustomerEntity customerEntity) {
        util.copyProperties(customerEntity, customerModel);
        customerEntity.setDeleted(BooleanUtils.toBooleanDefaultIfNull(customerModel.getDeleted(), false));
        customerEntity = saveCustomerEntity(customerEntity);
        util.copyProperties(customerModel, customerEntity);
        return customerModel;
    }

    /**
     * Save customer
     * @param customerModel {@link CustomerModel}
     * @return {@link CustomerModel}
     */
    @Override
    @Transactional
    public CustomerModel saveCustomer(CustomerModel customerModel) {
        log.info("Saving new customer name={}, deleted={}", customerModel.getName(), customerModel.getDeleted());
        if(StringUtils.isBlank(customerModel.getName())) {
            throw util.buildAppException(CUSTOMER_NAME_IS_REQUIRED, BAD_REQUEST);
        }

        CustomerEntity customerEntity = new CustomerEntity();

        return _saveCustomer(customerModel, customerEntity);
    }

    /**
     * Update customer
     * @param customerModel {@link CustomerModel}
     * @return {@link CustomerModel}
     */
    @Override
    @Transactional
    public  CustomerModel updateCustomer(CustomerModel customerModel) {
        log.info("Update customer details id={}, name={}, deleted={}", customerModel.getId(), customerModel.getName(), customerModel.getDeleted());
        String id = customerModel.getId();
        if(StringUtils.isBlank(id)) {
            throw util.buildAppException(CUSTOMER_ID_REQUIRED, BAD_REQUEST);
        }
        if(StringUtils.isBlank(customerModel.getName())) {
            throw util.buildAppException(CUSTOMER_NAME_IS_REQUIRED, BAD_REQUEST);
        }

        CustomerEntity customerEntity = customerRepository.findById(id).orElseThrow(() -> util.buildAppException(CUSTOMER_NOT_FOUND_BY_ID, BAD_REQUEST, id));
        return _saveCustomer(customerModel, customerEntity);
    }

    /**
     * Enable disable customer
     * @param customerModelIds {@link List} of {@link CustomerModel}
     * @param deleted - delete status
     * @return {@link List} of {@link CustomerModel}
     */
    @Override
    @Transactional
    public List<CustomerModel> enableDisableCustomer(List<String> customerModelIds, Boolean deleted) {
        log.info("Enable or disable customer ids={}, deleted={}", customerModelIds, deleted);
        List<CustomerEntity> customerEntityList = ListUtils.emptyIfNull(customerRepository.findAllById(customerModelIds));
        customerEntityList.forEach(customerEntity -> customerEntity.setDeleted(BooleanUtils.toBooleanDefaultIfNull(deleted, false)));
        customerEntityList = saveCustomerEntityList(customerEntityList);
        return customerEntityList.stream().map(customerEntity -> util.copyProperties(new CustomerModel(),customerEntity)).toList();
    }

    /**
     * Find all customers with pagination
     * @param page Page number
     * @param size number of records in each page
     * @return {@link CustomerResponseModel}
     */
    @Override
    @SneakyThrows
    public CustomerResponseModel findAllCustomers(Integer page, Integer size) {
        log.info("Find all customers, page-number={}, page-size={}", page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by(ASC, NAME));
        Page<CustomerEntity> customerEntityPage = customerRepository.findAll(pageable);
        List<CustomerEntity> customerEntityList = ListUtils.emptyIfNull(customerEntityPage.getContent());
        List<CustomerModel> customerModelList = customerEntityList.stream().map(customerEntity -> util.copyProperties(new CustomerModel(), customerEntity)).toList();
        return CustomerResponseModel.builder()
                .page(page)
                .pageSize(size)
                .count(customerEntityList.size())
                .totalPages(customerEntityPage.getTotalPages())
                .totalCount(customerEntityPage.getTotalElements())
                .customers(customerModelList)
                .build();
    }

}