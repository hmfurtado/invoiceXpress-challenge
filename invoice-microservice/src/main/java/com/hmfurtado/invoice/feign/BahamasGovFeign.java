package com.hmfurtado.invoice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "bahamas",
        url = "${url.integrations.bahamasgov}")
public interface BahamasGovFeign {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    String register(@RequestParam("invoice") Long invoiceId,
                    @RequestParam(value = "fiscal_id") Long fiscalId,
                    @RequestParam(value = "name") String name,
                    @RequestParam(value = "email") String email);
}
