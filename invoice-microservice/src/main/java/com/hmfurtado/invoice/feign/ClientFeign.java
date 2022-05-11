package com.hmfurtado.invoice.feign;

import com.hmfurtado.invoice.model.dto.ClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "client",
        url = "${url.integrations.client}")
public interface ClientFeign {

    @GetMapping(path = "/fiscalId/{fiscalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ClientDTO retrieveClient(@PathVariable("fiscalId") Long fiscalId);

}
