package com.ecommerce.proyecto.business.service;

public interface ReportService {

	byte[] generateSalesByCustomerPdf();

	byte[] generateTopProductsByCategoryPdf();
}
