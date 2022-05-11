package com.hmfurtado.invoice.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class InvoicePK implements Serializable {

    private static final long serialVersionUID = -3510780259088104117L;

    private Long invoiceId;

    private Long clientFiscalId;
}
