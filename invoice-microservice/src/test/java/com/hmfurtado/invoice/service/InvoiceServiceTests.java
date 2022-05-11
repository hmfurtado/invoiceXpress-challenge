package com.hmfurtado.invoice.service;

import com.hmfurtado.invoice.feign.BahamasGovFeign;
import com.hmfurtado.invoice.feign.ClientFeign;
import com.hmfurtado.invoice.model.dto.ClientDTO;
import com.hmfurtado.invoice.model.dto.InvoiceDTO;
import com.hmfurtado.invoice.model.entity.InvoiceEntity;
import com.hmfurtado.invoice.model.entity.InvoicePK;
import com.hmfurtado.invoice.repository.InvoiceRepository;
import feign.FeignException;
import feign.Request;
import feign.RetryableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTests {

    @Mock
    private InvoiceRepository repository;

    @Mock
    private ClientFeign clientFeign;

    @Mock
    private BahamasGovFeign bahamasGovFeign;

    @Mock
    private MockMvc mockMvc;

    @InjectMocks
    private InvoiceService service;

    private InvoiceEntity entity;

    private InvoicePK invoicePK;

    private InvoiceDTO dto;

    private ClientDTO clientDTO;

    private Request requestGet;

    @BeforeEach
    private void setUp() {
        invoicePK = InvoicePK.builder().invoiceId(1L).clientFiscalId(1L).build();
        entity = InvoiceEntity.builder().id(invoicePK).build();

        clientDTO = ClientDTO.builder()
                .name("Paul")
                .email("paul@beatles.com")
                .id(1L)
                .fiscalId(1L)
                .build();
        dto = InvoiceDTO.builder()
                .invoiceId(1L)
                .clientDTO(Stream.of(clientDTO).collect(Collectors.toSet()))
                .build();

        requestGet = Request.create(Request.HttpMethod.GET, "/fiscalId/1", new HashMap<>(), null, null, null);

    }

    /**
     * retrieveClient Tests
     */

    @Test
    void retrieveClientOkTest() {
        when(repository.findByIdInvoiceId(any())).thenReturn(Arrays.asList(entity));
        when(clientFeign.retrieveClient(any())).thenReturn(clientDTO);
        InvoiceDTO result = service.retrieveClient(1L);
        assertEquals(clientDTO, result.getClientDTO().iterator().next());
    }

    @Test
    void retrieveClientInvoiceDoesNotExistsTest() {
        when(repository.findByIdInvoiceId(any())).thenReturn(Collections.EMPTY_LIST);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> service.retrieveClient(1L));
        assertEquals("404 NOT_FOUND \"Invoice not found\"", exception.getMessage());
    }

    @Test
    void retrieveClientNotFoundTest() {
        when(repository.findByIdInvoiceId(any())).thenReturn(Arrays.asList(entity));
        when(clientFeign.retrieveClient(any())).thenThrow(new FeignException.NotFound(null, requestGet,
                null, null));
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> service.retrieveClient(1L));
        assertEquals("404 NOT_FOUND \"Client not found\"", exception.getMessage());
    }

    @Test
    void retrieveClientNotWorkingTest() {
        when(repository.findByIdInvoiceId(any())).thenReturn(Arrays.asList(entity));
        when(clientFeign.retrieveClient(any()))
                .thenThrow(new RetryableException(500, "error", Request.HttpMethod.GET,
                        Date.from(Calendar.getInstance().getTime().toInstant()), requestGet));
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> service.retrieveClient(1L));
        assertEquals("502 BAD_GATEWAY \"Cannot get client\"", exception.getMessage());
    }

    @Test
    void retrieveClientMicroserviceNotRespondingTest() {
        when(repository.findByIdInvoiceId(any())).thenReturn(Arrays.asList(entity));
        when(clientFeign.retrieveClient(any())).thenReturn(new ClientDTO());
        InvoiceDTO invoiceDTO = service.retrieveClient(1L);
        assertEquals(InvoiceDTO.builder().invoiceId(1L)
                .clientDTO(Stream.of(new ClientDTO()).collect(Collectors.toSet())).build(), invoiceDTO);
    }

    /**
     * createInvoice Tests
     */

    @Test
    void createInvoiceOkTest() {
        when(clientFeign.retrieveClient(any())).thenReturn(clientDTO);
        when(repository.existsById(any())).thenReturn(false);
        when(repository.findByIdInvoiceId(any())).thenReturn(new ArrayList<>());
        String result = service.createInvoice(1L, 1L, "Paul", "paul@beatles.com");
        assertEquals("Invoice registration successful: 1", result);
    }

    @Test
    void createInvoiceAlreadyExistsTest() {
        when(repository.existsById(any())).thenReturn(true);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                service.createInvoice(1L, 1L, "Paul", "paul@beatles.com"));
        assertEquals("400 BAD_REQUEST \"Invoice already registered for client\"", exception.getMessage());
    }

    @Test
    void createInvoiceAlreadyHasTwoClientsTest() {
        InvoiceEntity entity = InvoiceEntity.builder().id(new InvoicePK()).build();
        List<InvoiceEntity> list = Arrays.asList(entity, entity, entity);
        when(repository.existsById(any())).thenReturn(false);
        when(repository.findByIdInvoiceId(any())).thenReturn(list);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                service.createInvoice(1L, 1L, "Paul", "paul@beatles.com"));
        assertEquals("400 BAD_REQUEST \"This invoice already have 2 different clients\"", exception.getMessage());
    }

    @Test
    void createInvoiceBahamasApiErrorTest() {
        when(repository.existsById(any())).thenReturn(false);
        when(repository.findByIdInvoiceId(any())).thenReturn(new ArrayList<>());
        when(bahamasGovFeign.register(any(), any(), any(), any()))
                .thenThrow(new RetryableException(500, "error", Request.HttpMethod.GET,
                        Date.from(Calendar.getInstance().getTime().toInstant()), requestGet));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                service.createInvoice(1L, 1L, "Paul", "paul@beatles.com"));

        assertEquals("502 BAD_GATEWAY \"Bahamas government api not responding\"", exception.getMessage());
    }


}
