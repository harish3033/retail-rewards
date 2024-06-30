package com.retail.rewards.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorConstants {

    public static final String CUSTOMER_NOT_FOUND_BY_ID = "Customer not found by id %s";
    public static final String CUSTOMER_ID_REQUIRED = "Customer id required";
    public static final String INVALID_BILL_AMOUNT = "Invalid bill amount %s";

    public static final String BILL_AMOUNT_REQUIRED = "Bill amount required";

    public static final String TRANSACTION_DATE_REQUIRED = "Transaction date required";
    public static final String TRANSACTION_NOT_FOUND_BY_ID = "Transaction not found by id %s";
    public static final String CUSTOMER_NAME_IS_REQUIRED = "Customer name is required";
}