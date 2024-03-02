package com.altqart.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.altqart.model.SaleReturnInvoice;

public interface SaleReturnInvoiceRepository extends CrudRepository<SaleReturnInvoice, Integer> {

	public Optional<SaleReturnInvoice> getSaleReturnInvoiceByPublicId(String id);

}
