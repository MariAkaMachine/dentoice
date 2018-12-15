package com.mariakamachine.dentoice.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mariakamachine.dentoice.data.entity.CostWrapperEntity;
import com.mariakamachine.dentoice.data.entity.InvoiceEntity;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;

import static com.itextpdf.text.Element.*;
import static com.itextpdf.text.PageSize.A4;
import static com.itextpdf.text.Rectangle.NO_BORDER;
import static com.itextpdf.text.pdf.PdfWriter.getInstance;

@Slf4j
public class InvoicePdfGenerator {

    private static final Font FONT = FontFactory.getFont(FontFactory.COURIER, 10);

    public static byte[] generatePdf(InvoiceEntity invoice) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Document pdf = new Document(A4, 50, 50, 150, 50);
//        String s = new ClassPathResource("static/invoices/officecodepro-light.otf").getPath();
//        FontFactory.register(s,"office");
        pdf.addAuthor("Dentaltechnik Udo Baumann");
        pdf.addCreator("Dentaltechnik Udo Baumann");
        pdf.addTitle("1");
        pdf.addCreationDate();
        try {
            PdfWriter writer = getInstance(pdf, stream);
            pdf.open();
            pdf.add(recipientDetails(invoice));
            pdf.add(invoiceDetails(invoice));
            pdf.add(costsTable(invoice.getCosts()));
            PdfPTable footerDetails = footerDetails(invoice);
            footerDetails.writeSelectedRows(0, -1, pdf.left(), footerDetails.getTotalHeight() + pdf.bottom(), writer.getDirectContent());
            pdf.close();
        } catch (DocumentException e) {
            log.error("unable to generate pdf invoice for invoice {}", invoice.getId(), e);
        }

        return stream.toByteArray();
    }

    private static PdfPTable recipientDetails(InvoiceEntity invoiceEntity) {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(45);
        table.setHorizontalAlignment(ALIGN_LEFT);

        table.addCell(cell("Herr Zahnarzt"));
        table.addCell(cell("Halo I Bims"));
        table.addCell(cell("Leinenweberstr. 47"));
        table.addCell(cell("70567 Stuttgart"));

        return table;
    }

    private static PdfPTable invoiceDetails(InvoiceEntity invoiceEntity) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(45);
        table.setHorizontalAlignment(ALIGN_RIGHT);

        table.addCell(cell("Rechnugnsnummer"));
        table.addCell(cell("123456"));
        table.addCell(cell("Rechnungsdatum"));
        table.addCell(cell("12.12.2018"));
        table.addCell(cell("Patient"));
        table.addCell(cell("Pfeil, Susanne"));
        table.addCell(cell("Kassenart"));
        table.addCell(cell("Privat"));
        table.addCell(cell("Zahnfarbe"));
        table.addCell(cell("gelb"));

        table.setSpacingAfter(20);

        return table;
    }


    private static PdfPTable costsTable(CostWrapperEntity costs) {
        PdfPTable table = new PdfPTable(new float[]{1, 3, 1, 1, 1});
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(ALIGN_CENTER);
        costsHeaderRow(table);

        for (int i = 0; i < 2; i++) {

            table.addCell(cell("1234"));
            table.addCell(cell("Desinfektion"));
            table.addCell(cell("1,00"));
            table.addCell(cell("6,32", ALIGN_RIGHT));
            table.addCell(cell("3,63", ALIGN_RIGHT));

            table.addCell(cell("4562"));
            table.addCell(cell("123456789 123456789 123456789"));
            table.addCell(cell("1,00"));
            table.addCell(cell("3,55", ALIGN_RIGHT));
            table.addCell(cell("5,53", ALIGN_RIGHT));
        }
        PdfPCell emptyRow = cell(" ");
        emptyRow.setColspan(table.getNumberOfColumns());
        table.addCell(emptyRow);
        table.addCell(emptyRow);

        table.addCell(cell(""));

        PdfPCell testRow = cell("Diese Sonderanfertigung wurde unter Einhaltung der grundlegenden Anforderungen des Anhang I der Richlinie 93/42/EWG erstellt. Sie ist ausschliesslich fuer den oben genannten Patienten bestimmt,");
        testRow.setColspan(3);
        table.addCell(testRow);
        table.addCell(cell(""));


        return table;
    }

    private static void costsHeaderRow(PdfPTable table) {
        table.setHeaderRows(1);
        table.addCell(headerCell("Position"));
        table.addCell(headerCell("Zahntechnische Leistung"));
        table.addCell(headerCell("Menge"));
        table.addCell(headerCell("Einzelpreis"));
        table.addCell(headerCell("Gesamtpreis"));
    }

    private static PdfPTable footerDetails(InvoiceEntity invoiceEntity) {
        PdfPTable table = new PdfPTable(6);
        table.setTotalWidth(PageSize.A4.getWidth() - 100);
        table.setLockedWidth(true);
        table.setHorizontalAlignment(ALIGN_CENTER);

        PdfPCell cell = cell("Hinweis gem. Par. 14 Abs. 4 Satz 1 Nr. 7 UstG:");
        cell.setColspan(table.getNumberOfColumns());
        table.addCell(cell);

        cell = cell("Zahlbar entspr. Konditionenvereinbarung v. 30.7.04");
        cell.setColspan(table.getNumberOfColumns());
        table.addCell(cell);

        table.addCell(headerCell("Material\n184,50"));
        table.addCell(headerCell("Leistung\n244,20"));
        table.addCell(headerCell("Netto EUR\n470,00"));
        table.addCell(headerCell("Mwst. %\n7,00"));
        table.addCell(headerCell("Mwst. EUR\n13,50"));
        table.addCell(headerCell("Betrag EUR\n502,50"));

        cell = cell("Ust. IdNr. DE239653548");
        cell.setColspan(table.getNumberOfColumns());
        table.addCell(cell);

        return table;
    }


    /*
     * CELL FORMATTING
     */

    private static PdfPCell cell(String text) {
        return cell(text, ALIGN_LEFT);
    }

    private static PdfPCell headerCell(String text) {
        PdfPCell cell = cell(text, ALIGN_LEFT, 3);
        cell.setPaddingTop(5);
        cell.setPaddingBottom(5);
        return cell;
    }

    private static PdfPCell footerCell(String text) {
        return cell(text, ALIGN_LEFT, 2);
    }

    private static PdfPCell cell(String text, int alignment) {
        return cell(text, alignment, NO_BORDER);
    }

    private static PdfPCell cell(String text, int alignment, int border) {
        PdfPCell cell = new PdfPCell();
        cell.setHorizontalAlignment(alignment);
        cell.setPhrase(new Phrase(text, FONT));
        cell.setBorder(border);
        return cell;
    }

}
