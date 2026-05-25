package com.ecommerce.proyecto.presentation.controller;

import com.ecommerce.proyecto.business.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

	private final ReportService reportService;

	public ReportController(ReportService reportService) {
		this.reportService = reportService;
	}

	@GetMapping("/sales-by-customer")
	public ResponseEntity<byte[]> salesByCustomer() {
		byte[] pdf = reportService.generateSalesByCustomerPdf();
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"ventas-por-cliente.pdf\"")
				.contentType(MediaType.APPLICATION_PDF)
				.body(pdf);
	}

	@GetMapping("/top-products")
	public ResponseEntity<byte[]> topProducts() {
		byte[] pdf = reportService.generateTopProductsByCategoryPdf();
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"top-productos-categoria.pdf\"")
				.contentType(MediaType.APPLICATION_PDF)
				.body(pdf);
	}
}
