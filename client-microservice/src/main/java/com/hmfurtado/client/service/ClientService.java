package com.hmfurtado.client.service;

import com.hmfurtado.client.model.dto.ClientDTO;
import com.hmfurtado.client.model.entity.ClientEntity;
import com.hmfurtado.client.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.Random;

@Service
@Slf4j
public record ClientService(ClientRepository repository, ModelMapper modelMapper) {

    public ClientDTO getClientByFiscalId(Long fiscalId) {
        ClientEntity entity = repository.findByFiscalId(fiscalId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        return modelMapper.map(entity, ClientDTO.class);
    }

    public ClientDTO createClient(ClientDTO dto) {

        if (repository.existsByEmail(dto.getEmail())) {
            log.error("Email is already in use: {}", dto.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client already exists");
        }

        //mocking generate client fiscal id
        SecureRandom random = new SecureRandom();
        long mockedFiscalId = random.nextLong();

        ClientEntity entity = ClientEntity.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .fiscalId(mockedFiscalId)
                .build();

        repository.saveAndFlush(entity);
        log.info("New user created: {}", entity.getId());

        return modelMapper.map(entity, ClientDTO.class);
    }
}
