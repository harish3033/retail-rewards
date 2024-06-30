package com.retail.rewards.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@ToString(of = {"totalBillAmount", "totalRewardPoints"})
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class RewardsModel {

    private CustomerModel customerModel;
    private Integer totalRewardPoints;
    private Integer totalBillAmount;
    private Integer numberOfTransactions;
    private List<TransactionModel> transactionModels;

}