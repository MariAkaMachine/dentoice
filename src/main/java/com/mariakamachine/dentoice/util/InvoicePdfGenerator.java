package com.mariakamachine.dentoice.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mariakamachine.dentoice.data.entity.CostWrapperEntity;
import com.mariakamachine.dentoice.data.entity.DentistEntity;
import com.mariakamachine.dentoice.data.entity.InvoiceEntity;
import com.mariakamachine.dentoice.data.enums.InsuranceType;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

import static com.itextpdf.text.Element.*;
import static com.itextpdf.text.FontFactory.*;
import static com.itextpdf.text.PageSize.A4;
import static com.itextpdf.text.Rectangle.NO_BORDER;
import static com.itextpdf.text.pdf.PdfWriter.getInstance;
import static java.lang.String.valueOf;
import static java.time.format.DateTimeFormatter.ofPattern;

@Slf4j
public class InvoicePdfGenerator {

    private static final Font DEFAULT_FONT = getFont(COURIER, 10);
    private static final Font BOLD_FONT = getFont(COURIER_BOLD, 10);
    private static final Font SMALL_FONT = getFont(COURIER, 8);

    public static byte[] generatePdf(InvoiceEntity invoice) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Document pdf = new Document(A4, 50, 50, 145, 50);
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

    private static PdfPTable recipientDetails(InvoiceEntity invoice) {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(45);
        table.setHorizontalAlignment(ALIGN_LEFT);

//        DentistEntity dentist = invoice.getDentist();
        DentistEntity dentist = new DentistEntity();
        dentist.setTitle("Herr Zahnarzt");
        dentist.setFirstName("Halo I");
        dentist.setLastName("Bims");
        dentist.setStreet("Leinenweberstr. 47");
        dentist.setZip("70567");
        dentist.setCity("Stuttgart");


        table.addCell(cell(dentist.getTitle()));
        table.addCell(cell(dentist.getFirstName() + " " + dentist.getLastName()));
        table.addCell(cell(dentist.getStreet()));
        table.addCell(cell(dentist.getZip() + " " + dentist.getCity()));

        return table;
    }

    private static PdfPTable invoiceDetails(InvoiceEntity invoice) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(45);
        table.setHorizontalAlignment(ALIGN_RIGHT);

        invoice.setId(2L);
        invoice.setDate(LocalDate.now());
        invoice.setPatient("Dieter MacAdam");
        invoice.setInsuranceType(InsuranceType.PRIVATE);

        table.addCell(cell("Rechnugnsnummer", 5));
        table.addCell(cell(valueOf(invoice.getId()), 9));
        table.addCell(cell("Rechnungsdatum", 4));
        table.addCell(cell(invoice.getDate().format(ofPattern("dd.MM.yyyy")), 8));
        table.addCell(cell("Patient", 4));
        table.addCell(cell(invoice.getPatient(), 8));
        table.addCell(cell("Kassenart", 4));
        table.addCell(cell(invoice.getInsuranceType().toString(), 8));
        table.addCell(cell("Zahnfarbe", 6));
        table.addCell(cell("gelb", 10));

        table.setSpacingAfter(20);

        return table;
    }


    private static PdfPTable costsTable(CostWrapperEntity costs) {
        PdfPTable table = new PdfPTable(new float[]{2, 9, 2, 3, 3});
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(ALIGN_CENTER);
        costsHeaderRow(table);

        for (int i = 0; i < 4; i++) {

            table.addCell(cell("1234"));
            table.addCell(cell("Desinfektion"));
            table.addCell(cell("1,00"));
            table.addCell(cellRight("6,32"));
            table.addCell(cellRight("3,63"));

            table.addCell(cell("4562"));
            table.addCell(cell("123456789 123456789 123456789"));
            table.addCell(cell("1,00"));
            table.addCell(cellRight("3,55"));
            table.addCell(cellRight("5,53"));
        }
        PdfPCell emptyRow = cell(" ");
        emptyRow.setColspan(table.getNumberOfColumns());
        table.addCell(emptyRow);
        table.addCell(emptyRow);

        table.addCell(cell(""));
        table.addCell(fineCell("Diese Sonderanfertigung wurde unter Einhaltung der grundlegenden Anforderungen des Anhang I der Richlinie 93/42/EWG erstellt. Sie ist ausschliesslich fuer den oben genannten Patienten bestimmt.", 3));
        table.addCell(cell(""));

        return table;
    }

    private static void costsHeaderRow(PdfPTable table) {
        table.setHeaderRows(1);
        table.addCell(headerCell("Position"));
        table.addCell(headerCell("Zahntechnische Leistung"));
        table.addCell(headerCell("Menge"));
        table.addCell(headerCellRight("Einzelpreis"));
        table.addCell(headerCellRight("Gesamtpreis"));
    }

    private static PdfPTable footerDetails(InvoiceEntity invoice) {
        PdfPTable table = new PdfPTable(5);
        table.setTotalWidth(A4.getWidth() - 100);
        table.setLockedWidth(true);
        table.setHorizontalAlignment(ALIGN_CENTER);

        table.addCell(fineCell("Hinweis gemäß § 14 Absatz 4 Satz 1 Nr. 7 Umsatzsteuergesetz:", table.getNumberOfColumns()));
        table.addCell(fineCell("Zahlbar entsprechend Konditionenvereinbarung vom 30.7.2004", table.getNumberOfColumns()));


        table.addCell(footerCell("Material\n184,50 €"));
        table.addCell(footerCell("Material\n184,50 €"));
        table.addCell(footerCell("Leistung\n244,20 €"));
        table.addCell(footerCell("Netto\n470,00 €"));
        table.addCell(footerCell("Mehrwertsteuer\n13,50 € (7,00%)"));
        table.addCell(footerCell("Gesamtbetrag\n502,50 €"));

        table.addCell(fineCell("Umsatzsteuer-Identifikationsnummer DE239653548", table.getNumberOfColumns()));

        return table;
    }


    /*
     * CELL FORMATTING
     */

    private static PdfPCell headerCell(String text) {
        return marginCell(text, ALIGN_LEFT, DEFAULT_FONT);
    }

    private static PdfPCell headerCellRight(String text) {
        return marginCell(text, ALIGN_RIGHT, DEFAULT_FONT);
    }

    private static PdfPCell footerCell(String text) {
        return marginCell(text, ALIGN_CENTER, BOLD_FONT);
    }

    private static PdfPCell marginCell(String text, int alignment, Font font) {
        PdfPCell cell = cell(text, alignment, font, 3);
        cell.setPaddingTop(5);
        cell.setPaddingBottom(5);
        return cell;
    }

    private static PdfPCell cell(String text) {
        return cell(text, ALIGN_LEFT, NO_BORDER);
    }

    private static PdfPCell cellRight(String text) {
        return cell(text, ALIGN_RIGHT, NO_BORDER);
    }

    private static PdfPCell cell(String text, int border) {
        return cell(text, ALIGN_LEFT, border);
    }

    private static PdfPCell cell(String text, int alignment, int border) {
        return cell(text, alignment, DEFAULT_FONT, border);
    }

    private static PdfPCell fineCell(String text, int colSpan) {
        PdfPCell cell = cell(text, ALIGN_LEFT, SMALL_FONT, NO_BORDER);
        cell.setColspan(colSpan);
        return cell;
    }

    private static PdfPCell cell(String text, int alignment, Font font, int border) {
        PdfPCell cell = new PdfPCell();
        cell.setHorizontalAlignment(alignment);
        cell.setPhrase(new Phrase(text, font));
        cell.setBorder(border);
        return cell;
    }

}
