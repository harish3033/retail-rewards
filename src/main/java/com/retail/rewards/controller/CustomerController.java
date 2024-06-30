package com.retail.rewards.controller;

import com.retail.rewards.model.CustomerModel;
import com.retail.rewards.model.CustomerResponseModel;
import com.retail.rewards.model.exception.ErrorResponseModel;
import com.retail.rewards.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.retail.rewards.util.Constants.API_V1;
import static com.retail.rewards.util.Constants.DELETED;
import static com.retail.rewards.util.Constants.PAGE;
import static com.retail.rewards.util.Constants.SIZE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@AllArgsConstructor
@Tag(name = "Customer API")
@RestController
@RequestMapping(value = API_V1, produces = APPLICATION_JSON_VALUE)
public class CustomerController {

    private CustomerService customerService;

    /**
     * Save customer
     *
     * @param customerModel {@link CustomerModel}
     * @return {@link CustomerModel}
     */
    @Operation(summary = "Save new customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Save new customer",
                    content = {
                            @Content(mediaType = APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = CustomerModel.class))
                        }),
            @ApiResponse(responseCode = "400", description = "Customer name is required",
                    content = {
                        @Content(mediaType = APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ErrorResponseModel.class))
                    })
    })
    @PostMapping(value = "/customers", consumes = APPLICATION_JSON_VALUE)
    public CustomerModel saveCustomerModel(@RequestBody CustomerModel customerModel) {
        return customerService.saveCustomer(customerModel);
    }

    /**
     * Update customer
     *
     * @param customerModel {@link CustomerModel}
     * @return {@link CustomerModel}
     */
    @Operation(summary = "Update customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update customer",
                    content = {
                            @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CustomerModel.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Customer id required",
                    content = {
                            @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponseModel.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Customer not found by id %s",
                    content = {
                            @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponseModel.class))
                    })
    })
    @PutMapping(value = "/customers", consumes = APPLICATION_JSON_VALUE)
    public CustomerModel updateCustomer(@RequestBody CustomerModel customerModel) {
        return customerService.updateCustomer(customerModel);
    }

    /**
     * Enable or disable list of customers by ids
     *
     * @param customerModelIds - customer id list
     * @param deleted          - delete status
     * @return {@link List} of {@link CustomerModel}
     */
    @Operation(summary = "Enable disable customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enable disable customer",
                    content = {
                            @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CustomerModel.class))
                    })
    })
    @DeleteMapping(value = "/customers/{deleted}", consumes = APPLICATION_JSON_VALUE)
    public List<CustomerModel> enableDisableCustomer(@RequestBody List<String> customerModelIds, @PathVariable(DELETED) Boolean deleted) {
        return customerService.enableDisableCustomer(customerModelIds, deleted);
    }

    /**
     * Get all customers with pagination
     *
     * @param page page number
     * @param size number of records in each page
     * @return {@link CustomerResponseModel}
     */
    @Operation(summary = "Find all customers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Find all customers",
                    content = {
                            @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CustomerModel.class))
                    })
    })
    @GetMapping(value = "/customers")
    public CustomerResponseModel findAllCustomers(
            @Parameter(description = "Page number")
            @RequestParam(value = PAGE, required = false, defaultValue = "0") Integer page,
            @Parameter(description = "Page size")
            @RequestParam(value = SIZE, required = false, defaultValue = "2147483647") Integer size) {
        return customerService.findAllCustomers(page, size);
    }

}