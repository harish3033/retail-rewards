package com.retail.rewards.util;

import com.retail.rewards.model.exception.AppException;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class Util {

    /**
     * Build {@link AppException}
     * @param message Error message
     * @param httpStatus {@link HttpStatus}
     * @param vars Arguments in error message
     * @return
     */
    public AppException buildAppException(String message, HttpStatus httpStatus, Object... vars) {
        message = String.format(message, vars);
        return AppException.builder()
                .message(message)
                .httpStatus(httpStatus)
                .build();
    }

    /**
     * Copy properties from source object to destination object
     */
    @SneakyThrows
    public <T> T copyProperties(T dest, Object orig) {
        BeanUtils.copyProperties(dest, orig);
        return dest;
    }

}