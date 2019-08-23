package com.mariakamachine.dentoice.util.invoice.pdf;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.mariakamachine.dentoice.data.entity.InvoiceEntity;
import com.mariakamachine.dentoice.data.entity.MonthlyEntity;
import com.mariakamachine.dentoice.model.FileResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static com.itextpdf.kernel.events.PdfDocumentEvent.END_PAGE;
import static com.itextpdf.kernel.geom.PageSize.A4;
import static java.lang.String.format;

@Slf4j
public class PdfGenerator {

    public FileResource generateMonthlyPdfInvoice(MonthlyEntity monthlyEntity, List<InvoiceEntity> invoices) {
        final String pdfName = monthlyEntity.getDescription();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                Document pdf = new Document(A4, 50, 50, 110, 150);
        PdfDocument pdf = new PdfDocument(new PdfWriter(stream));
        Document doc = new Document(pdf, A4);

//        try {


//            PdfWriter writer = new PdfWriter(stream);
//            pdf.open();
//            writer.setPageEvent(new PdfPageEvent(pdfName, true));
//            pdf.add(recipientDetails(monthlyEntity.getDentist()));
//            addTablesToPdf(pdf, writer, new MonthlyPdfInvoiceGenerator().generateTables(monthlyEntity, invoices));
        pdf.close();
//        } catch (DocumentException e) {
//            log.error("unable to generate pdf invoice for monthly invoice", e);
//        }
        return new FileResource(new ByteArrayResource(stream.toByteArray()), format("%s - %s.pdf", invoices.get(0).getDentist().getLastName(), pdfName));
    }

    public FileResource generatePdfInvoice(InvoiceEntity invoice) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PdfDocument pdf = new PdfDocument(new PdfWriter(stream));
        Document doc = new Document(pdf, A4);
        try {
            pdf.addEventHandler(END_PAGE, new PdfPageEvent(invoice, false));
            doc.setTopMargin(140);
            doc.setBottomMargin(65);
            addTablesToPdf(doc, new PdfInvoiceGenerator().generateTables(invoice));
        } finally {
            doc.close();
        }
        return new FileResource(new ByteArrayResource(stream.toByteArray()), format("%s.pdf", invoice.getId()));
    }

    private void addTablesToPdf(Document pdf, List<Table> tables) {
        int counter = 1;
        for (Table table : tables) {
//            if (counter == tables.size()) {
//                table.writeSelectedRows(0, -1, pdf.left(), table.getTotalHeight() + 45, writer.getDirectContent());
//            } else {
            pdf.add(table);
//            }
//            counter++;
        }
    }

}
