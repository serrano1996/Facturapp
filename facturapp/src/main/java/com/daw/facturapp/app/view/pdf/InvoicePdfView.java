package com.daw.facturapp.app.view.pdf;

import java.awt.Color;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.daw.facturapp.app.models.entities.Invoice;
import com.daw.facturapp.app.models.entities.ItemInvoice;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Component("enterprise/invoice/show_invoice")
public class InvoicePdfView extends AbstractPdfView {
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private LocaleResolver localeResolver;

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Invoice invoice = (Invoice) model.get("invoice");
		
		Locale locale = localeResolver.resolveLocale(request);
		
		PdfPCell cell = null;
		
		byte[] img = invoice.getClient().getEnterprise().getLogo(); 
		Image image = Image.getInstance(img);
		image.scalePercent((float) 10);
		//image.setRotationDegrees(30);
		image.setIndentationLeft((float) 52);
		
		PdfPTable table = new PdfPTable(1);
		cell = new PdfPCell(new Phrase(messageSource.getMessage("text.invoice.show.data",  null, locale)));
		cell.setBackgroundColor(new Color(184, 218, 255));
		cell.setPadding(8f);
		table.addCell(cell);
		table.addCell(invoice.getClient().getNif());
		table.addCell(invoice.getClient().getName());
		table.setSpacingAfter(20);
		table.setSpacingBefore(8f);
		
		PdfPTable table2 = new PdfPTable(4);
		table2.setWidths(new float[] {3.5f, 1, 1, 1});
		table2.setSpacingAfter(20);
		table2.addCell("Producto");
		table2.addCell("Precio");
		table2.addCell("Cantidad");
		table2.addCell("Total");
		
		for(ItemInvoice item: invoice.getItems()) {
			table2.addCell(item.getProduct().getLongName());
			table2.addCell(item.getPrice().toString());
			cell = new PdfPCell(new Phrase(item.getQuantity().toString()));
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			table2.addCell(cell);
			table2.addCell(item.calculateAmount().toString());
		}
		
		cell = new PdfPCell(new Phrase("Total: "));
		cell.setColspan(3);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		table2.addCell(cell);
		table2.addCell(invoice.getTotal().toString());
		
		document.addTitle("FAC"+ invoice.getId() + "-" + invoice.getCreateAt().toString());
		document.add(image);
		document.add(table);
		document.add(table2);
	}

}
