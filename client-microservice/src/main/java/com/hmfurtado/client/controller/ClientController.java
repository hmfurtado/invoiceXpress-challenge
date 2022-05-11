package com.hmfurtado.client.controller;

import com.hmfurtado.client.model.dto.ClientDTO;
import com.hmfurtado.client.service.ClientService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("client")
public record ClientController(ClientService service) {

    @GetMapping(path = "fiscalId/{fiscalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDTO> getClientByFiscalId(@PathVariable(value = "fiscalId") Long fiscalId) {
        return ResponseEntity.ok().body(service.getClientByFiscalId(fiscalId));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDTO> createClient(@Valid @RequestBody ClientDTO dto) {
        return ResponseEntity.ok().body(service.createClient(dto));
    }
}