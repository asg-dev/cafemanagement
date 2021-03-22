package com.org.cafemgmt.repository;

import com.org.cafemgmt.model.CafeOrderInvoices;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CafeInvoiceRepository extends JpaRepository<CafeOrderInvoices, Long> {
}
