package com.retail.rewards.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.retail.rewards.util.Constants.DATE_TIME_FORMAT_FOR_JSON;
import static com.retail.rewards.util.Constants.LOCALE_EN;
import static com.retail.rewards.util.Constants.TIMEZONE_IST;

@Data
@Builder
@ToString(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class TransactionModel {
    private String id;
    private String customerId;
    @JsonFormat(shape = STRING, pattern = DATE_TIME_FORMAT_FOR_JSON, timezone = TIMEZONE_IST, locale = LOCALE_EN)
    private Date transactionDate;
    private Integer billAmount;
    private Integer rewardPoints;
    private Boolean deleted;
    private CustomerModel customer;
}