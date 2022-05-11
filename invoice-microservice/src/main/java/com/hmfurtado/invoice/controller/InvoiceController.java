package com.hmfurtado.invoice.controller;

import com.hmfurtado.invoice.service.InvoiceService;
import com.hmfurtado.invoice.model.dto.InvoiceDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record InvoiceController(InvoiceService service) {

    @GetMapping(path = "retrieve-bahamas-client/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InvoiceDTO> retrieveClient(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.retrieveClient(id));
    }

    @PostMapping(path = "/store-bahamas-client/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createInvoice(@PathVariable("id") Long id,
                                                @RequestParam(value = "fiscal_id") Long clientFiscalId,
                                                @RequestParam(value = "name") String clientName,
                                                @RequestParam(value = "email") String clientEmail) {
        return ResponseEntity.ok(service.createInvoice(id, clientFiscalId, clientName, clientEmail));
    }

}
