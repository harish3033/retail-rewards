package com.retail.rewards.controller;

import com.retail.rewards.model.RewardsResponseModel;
import com.retail.rewards.model.TransactionModel;
import com.retail.rewards.model.exception.ErrorResponseModel;
import com.retail.rewards.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.retail.rewards.util.Constants.API_V1;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@AllArgsConstructor
@Tag(name = "Transactions API")
@RestController
@RequestMapping(value = API_V1, produces = APPLICATION_JSON_VALUE)
public class TransactionController {

    private TransactionService transactionService;

    /**
     * Save transaction
     * @param transactionModel {@link TransactionModel}
     * @return {@link TransactionModel}
     */
    @Operation(summary = "Save transaction")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Save new transaction",
                    content = {
                            @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = TransactionModel.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400", description = "Bill amount required",
                    content = {
                            @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponseModel.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid bill amount %s",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseModel.class)
                    )
            )
    })
    @PostMapping(value = "/transactions", consumes = APPLICATION_JSON_VALUE)
    public TransactionModel saveTransaction(@RequestBody TransactionModel transactionModel) {
        return transactionService.saveUpdateTransaction(transactionModel);
    }

    /**
     * Update transaction
     * @param transactionModel {@link TransactionModel}
     * @return {@link TransactionModel}
     */
    @Operation(summary = "Update transaction")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Update transaction",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TransactionModel.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Transaction not found by id",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseModel.class)
                    )
            )
    })
    @PutMapping(value = "/transactions", consumes = APPLICATION_JSON_VALUE)
    public TransactionModel updateTransaction(@RequestBody TransactionModel transactionModel) {
        return transactionService.saveUpdateTransaction(transactionModel);
    }

    /**
     * Find transactions by customer
     * @param customerId customer id
     * @return {@link RewardsResponseModel}
     */
    @Operation(summary = "Find transactions by customer id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Find transactions by customer id",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RewardsResponseModel.class)
                    )
            )
    })
    @GetMapping(value = "/transactions/{customerId}")
    public RewardsResponseModel findTransactionsByCustomer(
            @Parameter(description = "Customer id")
            @PathVariable("customerId") String customerId) {
        return transactionService.findTransactionsByCustomer(customerId);
    }

    /**
     * Find all transactions
     * @return {@link RewardsResponseModel}
     */
    @Operation(summary = "Find all transactions group by customer id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Find all transactions group by customer id",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RewardsResponseModel.class)
                    )
            )
    })
    @GetMapping(value = "/transactions")
    public RewardsResponseModel findAllTransactions() {
        return transactionService.findAllTransactions();
    }

    /**
     * Find transactions by date range
     * @param fromDateStr From date
     * @param toDateStr To date
     * @return {@link RewardsResponseModel}
     */
    @Operation(summary = "Find all transactions by date range")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Find all transactions by date range",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RewardsResponseModel.class)
                    )
            )
    })
    @GetMapping(value = "/transactions/{fromDate}/{toDate}")
    public RewardsResponseModel findTransactionsByDateRange(
            @Parameter(description = "From date")
            @PathVariable("fromDate") String fromDateStr,
            @Parameter(description = "To date")
            @PathVariable("toDate") String toDateStr) {
        return transactionService.findTransactionsByDateRange(fromDateStr, toDateStr);
    }

}