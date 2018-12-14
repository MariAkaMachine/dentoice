package com.mariakamachine.dentoice.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.mariakamachine.dentoice.data.entity.InvoiceEntity;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;

import static com.itextpdf.text.PageSize.A4;
import static com.itextpdf.text.pdf.PdfWriter.getInstance;

@Slf4j
public class InvoicePdfGenerator {

    public static byte[] generatePdf(InvoiceEntity invoice) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Document pdf = new Document(A4, 50, 20, 150, 30);
        Font font = FontFactory.getFont(BaseFont.COURIER, 10);
        pdf.addAuthor("Dentaltechnik Udo Baumann");
        pdf.addCreator("Dentaltechnik Udo Baumann");
        pdf.addTitle("1");
        pdf.addCreationDate();
        try {
            getInstance(pdf, stream);
            pdf.open();
            pdf.add(new Paragraph("Herr Zahnarzt", font));
            pdf.add(new Paragraph("Mat Fraser", font));
            pdf.add(new Paragraph("Leinenweberstr. 47", font));
            pdf.add(new Paragraph("70567 Stuttgart", font));


            pdf.add(new Paragraph("RECHNUNG", font));








            pdf.close();
        } catch (DocumentException e) {
            log.error("unable to generate pdf invoice for invoice {}", invoice.getId(), e);
        }

        return stream.toByteArray();
    }


}
