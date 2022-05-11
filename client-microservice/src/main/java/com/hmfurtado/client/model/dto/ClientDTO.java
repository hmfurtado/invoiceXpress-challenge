package com.hmfurtado.client.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientDTO {
    Long id;
    @NotBlank(message = "name cannot be null or empty")
    String name;
    @NotBlank(message = "email cannot be null or empty")
    String email;
    Long fiscalId;

}
