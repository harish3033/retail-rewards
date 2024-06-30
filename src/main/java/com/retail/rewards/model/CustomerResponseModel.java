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
@ToString(of = {"page", "pageSize"})
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class CustomerResponseModel {

    private Integer page;
    private Integer pageSize;
    private Integer count;
    private Integer totalPages;
    private Long totalCount;
    private List<CustomerModel> customers;

}
