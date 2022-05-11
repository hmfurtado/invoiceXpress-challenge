package com.hmfurtado.invoice.repository;

import com.hmfurtado.invoice.model.entity.InvoiceEntity;
import com.hmfurtado.invoice.model.entity.InvoicePK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, InvoicePK> {

    List<InvoiceEntity> findByIdInvoiceId(Long invoiceId);
}
