package com.retail.rewards.service;

import com.retail.rewards.model.RewardsResponseModel;
import com.retail.rewards.model.TransactionModel;
import lombok.SneakyThrows;

public interface TransactionService {

    /**
     * Save or update transaction
     * @param transactionModel {@link TransactionModel}
     * @return {@link TransactionModel}
     */
    TransactionModel saveUpdateTransaction(TransactionModel transactionModel);

    /**
     * Find transactions by customer
     * @param customerId - Customer id
     * @return {@link RewardsResponseModel}
     */
    RewardsResponseModel findTransactionsByCustomer(String customerId);

    /**
     * Find all transactions
     * @return {@link RewardsResponseModel}
     */
    RewardsResponseModel findAllTransactions();

    /**
     * Find all transactions by date range
     * @param fromDateStr From date
     * @param toDateStr To date
     * @return {@link RewardsResponseModel}
     */
    @SneakyThrows
    RewardsResponseModel findTransactionsByDateRange(String fromDateStr, String toDateStr);
}
