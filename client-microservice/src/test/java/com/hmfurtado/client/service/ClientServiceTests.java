package com.hmfurtado.client.service;

import com.hmfurtado.client.model.dto.ClientDTO;
import com.hmfurtado.client.model.entity.ClientEntity;
import com.hmfurtado.client.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static java.util.Objects.nonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTests {

    @InjectMocks
    private ClientService service;
    @Mock
    private ClientRepository repository;

    private ClientEntity entity;

    private ClientDTO dto;

    @BeforeEach
    private void setUp() {
        entity = ClientEntity.builder()
                .id(1L)
                .name("Paul")
                .email("paul@beatles.com")
                .fiscalId(1L)
                .build();

        dto = ClientDTO.builder()
                .name("Paul")
                .email("paul@beatles.com")
                .build();

        ModelMapper modelMapper = new ModelMapper();

        service = new ClientService(repository, modelMapper);
    }

    /**
     * getClientByFiscalId Tests
     */

    @Test
    void getClientByFiscalIdOkTest() {
        when(repository.findByFiscalId(any())).thenReturn(Optional.of(entity));
        ClientDTO result = service.getClientByFiscalId(1L);
        assertEquals(entity.getName(), result.getName());
    }

    @Test
    void getClientByFiscalIdNotFoundTest() {
        when(repository.findByFiscalId(any())).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> service.getClientByFiscalId(1L));
        assertEquals("404 NOT_FOUND \"Client not found\"", exception.getMessage());
    }

    /**
     * createClient Tests
     */

    @Test
    void createClientOkTest() {
        when(repository.existsByEmail(any())).thenReturn(false);
        ClientDTO result = service.createClient(dto);
        assertTrue(nonNull(result.getFiscalId()));
    }

    @Test
    void createClientAlreadyExistsTest() {
        when(repository.existsByEmail(any())).thenReturn(true);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> service.createClient(dto));
        assertEquals("400 BAD_REQUEST \"Client already exists\"", exception.getMessage());

    }
}
