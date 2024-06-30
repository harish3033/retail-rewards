package com.retail.rewards.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@ToString(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class CustomerModel {

    private String id;
    private String name;
    private Boolean deleted;

}