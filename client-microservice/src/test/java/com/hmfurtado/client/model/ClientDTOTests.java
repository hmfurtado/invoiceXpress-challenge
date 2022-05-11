package com.hmfurtado.client.model;

import com.hmfurtado.client.model.dto.ClientDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ClientDTOTests {

    @Test
    void validateDtoNameNullOrEMpty() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        ClientDTO dto = ClientDTO.builder()
                .email("paul@beatles.com")
                .build();
        Set<ConstraintViolation<ClientDTO>> validate = validator.validate(dto);
        assertEquals("name cannot be null or empty", validate.iterator().next().getMessage());
    }

    @Test
    void validateDtoEmailNullOrEMpty() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        ClientDTO dto = ClientDTO.builder()
                .name("Paul")
                .build();
        Set<ConstraintViolation<ClientDTO>> validate = validator.validate(dto);
        assertEquals("email cannot be null or empty", validate.iterator().next().getMessage());
    }
}
