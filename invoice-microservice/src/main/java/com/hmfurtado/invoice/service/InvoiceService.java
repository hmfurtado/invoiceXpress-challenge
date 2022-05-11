package com.hmfurtado.invoice.service;

import com.hmfurtado.invoice.feign.BahamasGovFeign;
import com.hmfurtado.invoice.feign.ClientFeign;
import com.hmfurtado.invoice.model.dto.ClientDTO;
import com.hmfurtado.invoice.model.dto.InvoiceDTO;
import com.hmfurtado.invoice.model.entity.InvoiceEntity;
import com.hmfurtado.invoice.model.entity.InvoicePK;
import com.hmfurtado.invoice.repository.InvoiceRepository;
import feign.FeignException;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public record InvoiceService(InvoiceRepository repository,
                             ClientFeign clientFeign,
                             BahamasGovFeign bahamasFeign) {

    public InvoiceDTO retrieveClient(Long id) {
        List<InvoiceEntity> invoices = repository.findByIdInvoiceId(id);
        if (CollectionUtils.isEmpty(invoices)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found");
        }
        InvoiceDTO result = InvoiceDTO.builder().invoiceId(id).build();

        Set<ClientDTO> clients = new HashSet<>();

        //retrieving clients from client microservice
        invoices.stream().forEach(r ->
                clients.add(retrieveClientFromClientMicroservice(r.getId().getClientFiscalId())));
        result.setClientDTO(clients);
        return result;
    }

    public String createInvoice(Long invoiceId, Long fiscalId, String name, String email) {
        InvoicePK invoicePk = InvoicePK.builder()
                .invoiceId(invoiceId)
                .clientFiscalId(fiscalId)
                .build();
        //avoid duplicates from same invoice and client
        if (repository.existsById(invoicePk)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invoice already registered for client");
        }
        //check if invoice already has 2 or more clients
        if (repository.findByIdInvoiceId(invoiceId).size() >= 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "This invoice already have 2 different clients");
        }
        //checking if client exists
        retrieveClientFromClientMicroservice(fiscalId);

        repository.save(InvoiceEntity.builder().id(invoicePk).build());
        log.info("Invoice created: {}, Client Fiscal id: {}",
                invoiceId, fiscalId);

        bahamasTaxDeductions(invoiceId, fiscalId, name, email);
        return "Invoice registration successful: " + invoiceId;
    }

    private void bahamasTaxDeductions(Long invoiceId, Long fiscalId, String name, String email) {
        try {
            bahamasFeign.register(invoiceId, fiscalId, name, email);
            log.info("Invoice sent to Bahamas Government API. Invoice id: {}, Client Fiscal id: {}",
                    invoiceId, fiscalId);
        } catch (RetryableException e) {
            log.error(e.getMessage());
            log.error("Error reporting to Bahamas government tax deductions. Client fiscal id: {}", fiscalId);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Bahamas government api not responding");
        }
    }

    private ClientDTO retrieveClientFromClientMicroservice(Long clientFiscalId) {
        try {
            return (clientFeign.retrieveClient(clientFiscalId));
        } catch (FeignException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Client not found");
        } catch (RetryableException e) {
            log.error(e.getMessage());
            log.error("Error retrieving client from client microservice. Client fiscal id: {}", clientFiscalId);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Cannot get client");
        }
    }

}
