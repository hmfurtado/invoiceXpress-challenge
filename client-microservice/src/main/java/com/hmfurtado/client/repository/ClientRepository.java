package com.hmfurtado.client.repository;

import com.hmfurtado.client.model.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    boolean existsByEmail(String email);

    Optional<ClientEntity> findByFiscalId(Long fiscalId);

}
