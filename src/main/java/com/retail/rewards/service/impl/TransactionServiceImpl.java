package com.retail.rewards.service.impl;

import com.retail.rewards.entity.CustomerEntity;
import com.retail.rewards.entity.TransactionEntity;
import com.retail.rewards.model.CustomerModel;
import com.retail.rewards.model.RewardsModel;
import com.retail.rewards.model.RewardsResponseModel;
import com.retail.rewards.model.TransactionModel;
import com.retail.rewards.repository.CustomerRepository;
import com.retail.rewards.repository.TransactionRepository;
import com.retail.rewards.service.TransactionService;
import com.retail.rewards.util.Util;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.retail.rewards.util.Constants.DATE_TIME_FORMAT_FOR_JSON_2;
import static com.retail.rewards.util.ErrorConstants.BILL_AMOUNT_REQUIRED;
import static com.retail.rewards.util.ErrorConstants.CUSTOMER_ID_REQUIRED;
import static com.retail.rewards.util.ErrorConstants.CUSTOMER_NOT_FOUND_BY_ID;
import static com.retail.rewards.util.ErrorConstants.INVALID_BILL_AMOUNT;
import static com.retail.rewards.util.ErrorConstants.TRANSACTION_DATE_REQUIRED;
import static com.retail.rewards.util.ErrorConstants.TRANSACTION_NOT_FOUND_BY_ID;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@AllArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    private Util util;

    private TransactionRepository transactionRepository;

    private CustomerRepository customerRepository;

    /**
     * Save {@link TransactionEntity}
     * @param transactionEntity {@link TransactionEntity}
     * @return {@link TransactionEntity}
     */
    private TransactionEntity saveTransactionEntity(TransactionEntity transactionEntity) {
        log.info("Saving transaction entity={}", transactionEntity);
        TransactionEntity transactionEntity1 = transactionRepository.save(transactionEntity);
        log.info("Saved transaction entity={}", transactionEntity1);
        return transactionEntity1;
    }

    /**
     * Validate transaction model values
     * @param transactionModel {@link TransactionModel}
     */
    private void validateTransactionModel(TransactionModel transactionModel) {
        log.info("Start transaction values validation");
        Integer billAmount = transactionModel.getBillAmount();
        if(null == billAmount) {
            throw util.buildAppException(BILL_AMOUNT_REQUIRED, BAD_REQUEST);
        }

        if(billAmount < 0) {
            throw util.buildAppException(INVALID_BILL_AMOUNT, BAD_REQUEST, billAmount);
        }

        if(null == transactionModel.getTransactionDate()) {
            throw util.buildAppException(TRANSACTION_DATE_REQUIRED, BAD_REQUEST);
        }

        log.info("Completed transaction values validation");
    }

    /**
     * Find transaction by id
     * @param id Transaction id
     * @return {@link TransactionEntity}
     */
    private TransactionEntity findById(String id) {
        log.info("Find transaction by id={}", id);
        return transactionRepository.findById(id).orElseThrow(() -> util.buildAppException(TRANSACTION_NOT_FOUND_BY_ID, BAD_REQUEST, id));
    }

    /**
     * Save or update transaction
     * @param transactionModel {@link TransactionModel}
     * @return {@link TransactionModel}
     */
    @Override
    @Transactional
    public TransactionModel saveUpdateTransaction(TransactionModel transactionModel) {
        log.info("Save or update transaction");
        validateTransactionModel(transactionModel);

        TransactionEntity transactionEntity = null;
        String transactionId = transactionModel.getId();
        if(null == transactionId) {
            log.info("Id is null, creating new transaction");
            transactionEntity = new TransactionEntity();
        } else {
            log.info("updating transaction id={}", transactionId);
            transactionEntity = findById(transactionId);
        }

        log.info("Build transaction entity object");
        util.copyProperties(transactionEntity, transactionModel);
        transactionEntity.setDeleted(BooleanUtils.toBooleanDefaultIfNull(transactionModel.getDeleted(), false));
        transactionEntity.setRewardPoints(calculateRewards(transactionModel.getBillAmount()));

        String customerId = transactionModel.getCustomerId();
        CustomerEntity customerEntity = findCustomerEntityById(customerId);
        transactionEntity.setCustomerEntity(customerEntity);

        log.info("Save transaction entity");
        transactionEntity = saveTransactionEntity(transactionEntity);
        log.info("Build transaction model from new or updated transaction entity");
        transactionModel = util.copyProperties(transactionModel, transactionEntity);
        transactionModel.setCustomer(util.copyProperties(new CustomerModel(), customerEntity));

        return transactionModel;
    }

    /**
     * Find customer entity by id
     * @param customerId Customer id
     * @return {@link CustomerEntity}
     */
    private CustomerEntity findCustomerEntityById(String customerId) {
        log.info("Find customer entity by id={}", customerId);
        if(StringUtils.isBlank(customerId)) {
            throw util.buildAppException(CUSTOMER_ID_REQUIRED, BAD_REQUEST);
        }

        return customerRepository.findById(customerId).orElseThrow(() -> util.buildAppException(CUSTOMER_NOT_FOUND_BY_ID, BAD_REQUEST, customerId));
    }

    /**
     * Calculate rewards
     * @param billAmount Bill Amount
     * @return Reward points
     */
    public Integer calculateRewards(Integer billAmount) {
        int rewardPoints = 0;
        log.info("Calculating reward points, bill-amount={}, reward-points={}", billAmount, rewardPoints);
        if(billAmount > 50 && billAmount <= 100) {
            rewardPoints = billAmount - 50;
        } else if(billAmount > 100){
            rewardPoints = 50 + ((billAmount - 100) * 2);
        }

        log.info("Completed calculating reward points, bill-amount={}, reward-points={}", billAmount, rewardPoints);
        return rewardPoints;
    }

    /**
     * Find transactions by customer
     * @param customerId - Customer id
     * @return {@link RewardsResponseModel}
     */
    @Override
    public RewardsResponseModel findTransactionsByCustomer(String customerId) {
        log.info("Find transactions by customer id={}", customerId);
        CustomerEntity customerEntity = findCustomerEntityById(customerId);

        RewardsModel rewardsModel = buildRewardsModel(customerEntity);
        return RewardsResponseModel.builder()
                .response(List.of(rewardsModel))
                .build();
    }

    /**
     * Build {@link RewardsModel} from {@link CustomerEntity}
     * @param customerEntity {@link CustomerEntity}
     * @return {@link RewardsModel}
     */
    private RewardsModel buildRewardsModel(CustomerEntity customerEntity) {
        log.info("Building rewards model from customer entity");
        CustomerModel customerModel = util.copyProperties(new CustomerModel(), customerEntity);
        int totalRewardPoints = 0;
        int totalBillAmount = 0;
        List<TransactionModel> transactionModels = new ArrayList<>();
        List<TransactionEntity> transactionEntities = ListUtils.emptyIfNull(customerEntity.getTransactionEntities());
        for(TransactionEntity transactionEntity : transactionEntities) {
            Integer rewardPoints = transactionEntity.getRewardPoints();
            rewardPoints = rewardPoints == null ? 0 : rewardPoints;
            totalRewardPoints = totalRewardPoints + rewardPoints;

            Integer billAmount = transactionEntity.getBillAmount();
            billAmount = billAmount == null ? 0 : billAmount;
            totalBillAmount = totalBillAmount + billAmount;

            transactionModels.add(util.copyProperties(new TransactionModel(), transactionEntity));
        }

        RewardsModel rewardsModel = RewardsModel.builder()
                .customerModel(customerModel)
                .transactionModels(transactionModels)
                .totalRewardPoints(totalRewardPoints)
                .totalBillAmount(totalBillAmount)
                .numberOfTransactions(transactionEntities.size())
                .build();

        log.info("Completed building rewards model from customer entity");
        return rewardsModel;
    }

    /**
     * Find all transactions
     * @return {@link RewardsResponseModel}
     */
    @Override
    public RewardsResponseModel findAllTransactions() {
        log.info("Find all transactions");
        List<CustomerEntity> customerEntities = ListUtils.emptyIfNull(customerRepository.findAll());
        List<RewardsModel> rewardsModels = customerEntities.stream().map(this::buildRewardsModel).toList();
        int totalRewardPoints = 0;
        int totalBillAmount = 0;
        int numberOfTransactions = 0;
        for(RewardsModel rewardsModel : rewardsModels){
            totalRewardPoints = totalRewardPoints + rewardsModel.getTotalRewardPoints();
            totalBillAmount = totalBillAmount + rewardsModel.getTotalBillAmount();
            numberOfTransactions = numberOfTransactions + rewardsModel.getNumberOfTransactions();
        }

        return RewardsResponseModel.builder()
                .numberOfCustomers(customerEntities.size())
                .totalRewardPoints(totalRewardPoints)
                .totalBillAmount(totalBillAmount)
                .numberOfTransactions(numberOfTransactions)
                .response(rewardsModels)
                .build();
    }

    /**
     * Find all transactions by date range
     * @param fromDateStr From date
     * @param toDateStr To date
     * @return {@link RewardsResponseModel}
     */
    @Override
    @SneakyThrows
    public RewardsResponseModel findTransactionsByDateRange(String fromDateStr, String toDateStr) {
        log.info("Find transactions by date range from-date={}, to-date={}", fromDateStr, toDateStr);
        DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_FOR_JSON_2);
        Date fromDate = dateFormat.parse(fromDateStr);
        Date toDate = dateFormat.parse(toDateStr);
        List<TransactionEntity> transactionEntities = transactionRepository.findByTransactionDateBetween(fromDate, toDate);
        Map<String, CustomerEntity> customerEntityMap = new HashMap<>();
        Map<String, List<TransactionEntity>> transactionEntityMap = new HashMap<>();

        for(TransactionEntity transactionEntity : transactionEntities) {
            CustomerEntity customerEntity = transactionEntity.getCustomerEntity();
            String customerId = customerEntity.getId();
            customerEntityMap.put(customerId, customerEntity);
            transactionEntityMap.computeIfAbsent(customerId, k -> new ArrayList<>());
            transactionEntityMap.get(customerId).add(transactionEntity);

        }

        List<CustomerEntity> customerEntities = new ArrayList<>();
        customerEntityMap.forEach((customerId, customerEntity) -> {
            customerEntity.setTransactionEntities(transactionEntityMap.get(customerId));
            customerEntities.add(customerEntity);
        });

        List<RewardsModel> rewardsModels = customerEntities.stream().map(this::buildRewardsModel).toList();
        int totalRewardPoints = 0;
        int totalBillAmount = 0;
        int numberOfTransactions = 0;
        for(RewardsModel rewardsModel : rewardsModels){
            totalRewardPoints = totalRewardPoints + rewardsModel.getTotalRewardPoints();
            totalBillAmount = totalBillAmount + rewardsModel.getTotalBillAmount();
            numberOfTransactions = numberOfTransactions + rewardsModel.getNumberOfTransactions();
        }

        return RewardsResponseModel.builder()
                .numberOfCustomers(customerEntities.size())
                .totalRewardPoints(totalRewardPoints)
                .totalBillAmount(totalBillAmount)
                .numberOfTransactions(numberOfTransactions)
                .response(rewardsModels)
                .build();
    }

}