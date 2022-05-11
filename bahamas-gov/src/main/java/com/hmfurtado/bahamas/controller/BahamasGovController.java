package com.hmfurtado.bahamas.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("register")
@Slf4j
public record BahamasGovController() {

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> register(@RequestParam("invoice") Long invoiceId,
                                           @RequestParam(value = "fiscal_id") Long fiscalId,
                                           @RequestParam(value = "name") String name,
                                           @RequestParam(value = "email") String email) {
        log.info("Fiscal id: {} registered on Tax Deductions", fiscalId);
        return ResponseEntity.ok("Request completed");
    }

}