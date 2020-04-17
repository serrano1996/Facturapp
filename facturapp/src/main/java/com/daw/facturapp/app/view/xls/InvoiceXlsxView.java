package com.daw.facturapp.app.view.xls;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.daw.facturapp.app.models.entities.Invoice;
import com.daw.facturapp.app.models.entities.ItemInvoice;


@Component("enterprise/invoice/show_invoice.xlsx")
public class InvoiceXlsxView extends AbstractXlsxView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setHeader("Content-Disposition", "attachment; filename=\"invoice.xlsx\""); 
		Invoice invoice = (Invoice) model.get("invoice");
		Sheet sheet = workbook.createSheet(invoice.getClient().getEnterprise().getName());

		MessageSourceAccessor msg = getMessageSourceAccessor();
		
		Row row = sheet.createRow(0);
		Cell cell = row.createCell(0);
		cell.setCellValue(msg.getMessage("text.invoice.show.data").toUpperCase());
		
		row = sheet.createRow(1);
		cell = row.createCell(0);
		cell.setCellValue(invoice.getClient().getNif());
		
		row = sheet.createRow(2);
		cell = row.createCell(0);
		cell.setCellValue(invoice.getClient().getName());
		
		sheet.createRow(4).createCell(0).setCellValue(msg.getMessage("text.invoice.date") + ": " + invoice.getCreateAt());
		
		CellStyle theaderStyle = workbook.createCellStyle();
		theaderStyle.setBorderBottom(BorderStyle.MEDIUM);
		theaderStyle.setBorderTop(BorderStyle.MEDIUM);
		theaderStyle.setBorderLeft(BorderStyle.MEDIUM);
		theaderStyle.setBorderRight(BorderStyle.MEDIUM);
		theaderStyle.setFillForegroundColor(IndexedColors.AQUA.index);
		theaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		
		CellStyle tbodyStyle = workbook.createCellStyle();
		tbodyStyle.setBorderBottom(BorderStyle.THIN);
		tbodyStyle.setBorderTop(BorderStyle.THIN);
		tbodyStyle.setBorderLeft(BorderStyle.THIN);
		tbodyStyle.setBorderRight(BorderStyle.THIN);
		
		Row header = sheet.createRow(6);
		header.createCell(0).setCellValue(msg.getMessage("text.invoice.show.line.name").toUpperCase());
		header.createCell(1).setCellValue(msg.getMessage("text.invoice.show.line.price").toUpperCase());
		header.createCell(2).setCellValue(msg.getMessage("text.invoice.show.line.quantity").toUpperCase());
		header.createCell(3).setCellValue(msg.getMessage("text.invoice.show.line.subtotal").toUpperCase());
		
		header.getCell(0).setCellStyle(theaderStyle);
		header.getCell(1).setCellStyle(theaderStyle);
		header.getCell(2).setCellStyle(theaderStyle);
		header.getCell(3).setCellStyle(theaderStyle);
		
		int rownum = 7;
		
		for(ItemInvoice item : invoice.getItems()) {
			Row r = sheet.createRow(rownum++);
			
			cell = r.createCell(0);
			cell.setCellValue(item.getProduct().getLongName());
			cell.setCellStyle(tbodyStyle);
			
			cell = r.createCell(1);
			cell.setCellValue(item.getPrice());
			cell.setCellStyle(tbodyStyle);
			
			cell = r.createCell(2);
			cell.setCellValue(item.getQuantity());
			cell.setCellStyle(tbodyStyle);
			
			cell = r.createCell(3);
			cell.setCellValue(item.calculateAmount());
			cell.setCellStyle(tbodyStyle);
		}
		
		Row rowTotal = sheet.createRow(rownum++);
		
		cell = rowTotal.createCell(2);
		cell.setCellValue("Total".toUpperCase());
		cell.setCellStyle(tbodyStyle);
		
		cell = rowTotal.createCell(3);
		cell.setCellValue(invoice.getTotal());
		cell.setCellStyle(tbodyStyle);
	}

}
