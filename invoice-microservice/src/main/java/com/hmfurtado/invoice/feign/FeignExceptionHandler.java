package com.hmfurtado.invoice.feign;

import feign.FeignException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class FeignExceptionHandler {

    @ExceptionHandler(FeignException.class)
    public void handleFeignExceptions(FeignException e, HttpServletResponse response) {
        response.setStatus(e.status());
    }
}
