package com.ecommerce.proyecto.business.implementation;

import com.ecommerce.proyecto.business.service.ReportService;
import com.ecommerce.proyecto.data.repository.OrderDetailRepository;
import com.ecommerce.proyecto.data.repository.OrderRepository;
import com.ecommerce.proyecto.domain.dto.SalesByCustomerDto;
import com.ecommerce.proyecto.domain.dto.TopProductByCategoryDto;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportImplementation implements ReportService {

	private static final DateTimeFormatter DATE_FMT =
			DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

	private final OrderRepository orderRepository;
	private final OrderDetailRepository orderDetailRepository;

	public ReportImplementation(
			OrderRepository orderRepository,
			OrderDetailRepository orderDetailRepository
	) {
		this.orderRepository = orderRepository;
		this.orderDetailRepository = orderDetailRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public byte[] generateSalesByCustomerPdf() {
		LocalDateTime since = LocalDateTime.now().minusMonths(1);
		List<SalesByCustomerDto> data = orderRepository.findSalesByCustomerSince(since);

		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Document doc = new Document(PageSize.A4);
			PdfWriter.getInstance(doc, out);
			doc.open();

			Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Color.BLACK);
			Font subFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.DARK_GRAY);
			Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.WHITE);
			Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);

			Paragraph title = new Paragraph("Reporte de Ventas por Cliente", titleFont);
			title.setAlignment(Element.ALIGN_CENTER);
			doc.add(title);

			Paragraph sub = new Paragraph(
					"Período: último mes (desde " + since.format(DATE_FMT) + ")", subFont
			);
			sub.setAlignment(Element.ALIGN_CENTER);
			sub.setSpacingAfter(20);
			doc.add(sub);

			PdfPTable table = new PdfPTable(new float[]{1.2f, 3.5f, 3.5f, 2f});
			table.setWidthPercentage(100);

			addHeaderCell(table, "Código", headerFont);
			addHeaderCell(table, "Cliente", headerFont);
			addHeaderCell(table, "Email", headerFont);
			addHeaderCell(table, "Total (CRC)", headerFont);

			if (data.isEmpty()) {
				PdfPCell empty = new PdfPCell(new Paragraph("No hay ventas en el período", cellFont));
				empty.setColspan(4);
				empty.setHorizontalAlignment(Element.ALIGN_CENTER);
				empty.setPadding(10);
				table.addCell(empty);
			} else {
				for (SalesByCustomerDto row : data) {
					addCell(table, String.valueOf(row.userId()), cellFont, Element.ALIGN_LEFT);
					addCell(table, row.customerName(), cellFont, Element.ALIGN_LEFT);
					addCell(table, row.email(), cellFont, Element.ALIGN_LEFT);
					addCell(table, row.totalPurchased().toPlainString(), cellFont, Element.ALIGN_RIGHT);
				}
			}

			doc.add(table);
			doc.close();
			return out.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException("Error generando PDF", e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public byte[] generateTopProductsByCategoryPdf() {
		LocalDateTime since = LocalDateTime.now().minusMonths(1);
		List<TopProductByCategoryDto> all = orderDetailRepository.findTopProductsByCategorySince(since);

		Map<Long, Integer> countByCategory = new HashMap<>();
		List<TopProductByCategoryDto> top2 = all.stream()
				.filter(p -> {
					int c = countByCategory.getOrDefault(p.categoryId(), 0);
					if (c >= 2) return false;
					countByCategory.put(p.categoryId(), c + 1);
					return true;
				})
				.toList();

		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Document doc = new Document(PageSize.A4);
			PdfWriter.getInstance(doc, out);
			doc.open();

			Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Color.BLACK);
			Font subFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.DARK_GRAY);
			Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.WHITE);
			Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);

			Paragraph title = new Paragraph("Top 2 Productos por Categoría", titleFont);
			title.setAlignment(Element.ALIGN_CENTER);
			doc.add(title);

			Paragraph sub = new Paragraph(
					"Período: último mes (desde " + since.format(DATE_FMT) + ")", subFont
			);
			sub.setAlignment(Element.ALIGN_CENTER);
			sub.setSpacingAfter(20);
			doc.add(sub);

			PdfPTable table = new PdfPTable(new float[]{3f, 4f, 2f});
			table.setWidthPercentage(100);
			addHeaderCell(table, "Categoría", headerFont);
			addHeaderCell(table, "Producto", headerFont);
			addHeaderCell(table, "Unidades vendidas", headerFont);

			if (top2.isEmpty()) {
				PdfPCell empty = new PdfPCell(new Paragraph("No hay ventas en el período", cellFont));
				empty.setColspan(3);
				empty.setHorizontalAlignment(Element.ALIGN_CENTER);
				empty.setPadding(10);
				table.addCell(empty);
			} else {
				for (TopProductByCategoryDto row : top2) {
					addCell(table, row.categoryName(), cellFont, Element.ALIGN_LEFT);
					addCell(table, row.productName(), cellFont, Element.ALIGN_LEFT);
					addCell(table, String.valueOf(row.unitsSold()), cellFont, Element.ALIGN_RIGHT);
				}
			}

			doc.add(table);
			doc.close();
			return out.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException("Error generando PDF", e);
		}
	}

	private void addHeaderCell(PdfPTable table, String text, Font font) {
		PdfPCell cell = new PdfPCell(new Paragraph(text, font));
		cell.setBackgroundColor(new Color(33, 37, 41));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPadding(8);
		table.addCell(cell);
	}

	private void addCell(PdfPTable table, String text, Font font, int align) {
		PdfPCell cell = new PdfPCell(new Paragraph(text, font));
		cell.setHorizontalAlignment(align);
		cell.setPadding(6);
		table.addCell(cell);
	}
}
