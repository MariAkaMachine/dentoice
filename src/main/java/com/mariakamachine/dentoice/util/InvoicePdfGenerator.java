package com.mariakamachine.dentoice.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mariakamachine.dentoice.config.properties.InvoiceProperties;
import com.mariakamachine.dentoice.data.entity.CostWrapperEntity;
import com.mariakamachine.dentoice.data.entity.DentistEntity;
import com.mariakamachine.dentoice.data.entity.InvoiceEntity;
import com.mariakamachine.dentoice.data.enums.InsuranceType;
import com.mariakamachine.dentoice.data.jsonb.EffortJsonb;
import com.mariakamachine.dentoice.data.jsonb.MaterialJsonb;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static com.itextpdf.text.Element.*;
import static com.itextpdf.text.FontFactory.*;
import static com.itextpdf.text.PageSize.A4;
import static com.itextpdf.text.Rectangle.*;
import static com.itextpdf.text.pdf.PdfWriter.getInstance;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.math.BigDecimal.ROUND_HALF_DOWN;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
@NoArgsConstructor
public class InvoicePdfGenerator {

    private static final Font DEFAULT_FONT = getFont(COURIER, 10);
    private static final Font BOLD_FONT = getFont(COURIER_BOLD, 10);
    private static final Font SMALL_FONT = getFont(COURIER, 8);

    private BigDecimal effortsSum = new BigDecimal(0.0);
    private BigDecimal materialsSum = new BigDecimal(0.0);
    private BigDecimal metalsSum = new BigDecimal(0.0);

    public byte[] generatePdf(InvoiceProperties invoiceProperties, InvoiceEntity invoice) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Document pdf = new Document(A4, 50, 50, 110, 50);
        pdf.addAuthor("Dentaltechnik Udo Baumann");
        pdf.addCreator("Dentaltechnik Udo Baumann");
        pdf.addTitle("1");
        pdf.addCreationDate();
        try {
            PdfWriter writer = getInstance(pdf, stream);
            pdf.open();

            invoice.setId(186054L);

            writer.setPageEvent(new PdfPageNumberEvent(invoice.getId()));

            PdfPTable recipient = recipientDetails(invoice.getDentist());
            PdfPTable details = invoiceDetails(invoice);
            PdfPTable costsTable = costsTable(invoice.getCosts());
            PdfPTable footerDetails = footerDetails(invoiceProperties);

            pdf.add(recipient);
            pdf.add(details);
            pdf.add(costsTable);

            float margin = writer.getCurrentPageNumber() * pdf.topMargin() + writer.getCurrentPageNumber() * pdf.bottomMargin();
            System.out.println(format("MARGIN HEIGHT: %s", margin));
            System.out.println(format("RECIPIENT HEIGHT: %s", recipient.getTotalHeight()));
            System.out.println(format("DETAILS HEIGHT: %s", details.getTotalHeight()));
            System.out.println(format("COSTS HEIGHT: %s", costsTable.getTotalHeight()));
            System.out.println(format("COSTS HEIGHT: %s", footerDetails.getTotalHeight()));

            float docHeight = margin + recipient.getTotalHeight() + details.getTotalHeight() + 20 + costsTable.getTotalHeight() + writer.getCurrentPageNumber() * 10 + footerDetails.getTotalHeight();

            System.out.println(format("TOTAL HEIGHT: %s", docHeight));


//            System.out.println("FOOTER HEIGHT: " + footerDetails.getTotalHeight());
//            System.out.println("MODULO: " + docHeight % writer.getPageSize().getHeight());
            System.out.println("page: " + writer.getCurrentPageNumber());
            System.out.println(format("leftover: %s", writer.getCurrentPageNumber() * writer.getPageSize().getHeight() - docHeight));


//            if (writer.getCurrentPageNumber() == 1 && costsTable.getTotalHeight() > 400) {
            if (writer.getCurrentPageNumber() * writer.getPageSize().getHeight() - docHeight < 35) {
                pdf.newPage();
//            } else if (writer.getCurrentPageNumber() * writer.getPageSize().getHeight() - costsTable.getTotalHeight() > 400) {
//                pdf.newPage();
            }

            footerDetails.writeSelectedRows(0, -1, pdf.left(), footerDetails.getTotalHeight() + pdf.bottom(), writer.getDirectContent());


            pdf.close();
        } catch (DocumentException e) {
            log.error("unable to generate pdf invoice for invoice {}", invoice.getId(), e);
        }
        return stream.toByteArray();
    }

    private PdfPTable recipientDetails(DentistEntity dentist) {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(50);
        table.setHorizontalAlignment(ALIGN_LEFT);

        /*
         * REMOVE
         */
        dentist = new DentistEntity();
        dentist.setTitle("Herr Zahnarzt");
        dentist.setFirstName("Halo I");
        dentist.setLastName("Bims");
        dentist.setStreet("Leinenweberstr. 47");
        dentist.setZip("70567");
        dentist.setCity("Stuttgart");
        /*
         * REMOVE
         */

        table.addCell(cell(dentist.getTitle()));
        table.addCell(cell(dentist.getFirstName() + " " + dentist.getLastName()));
        table.addCell(cell(dentist.getStreet()));
        table.addCell(cell(dentist.getZip() + " " + dentist.getCity()));

        return table;
    }

    private PdfPTable invoiceDetails(InvoiceEntity invoice) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(45);
        table.setHorizontalAlignment(ALIGN_RIGHT);

        /*
         * REMOVE
         */
        invoice.setId(2L);
        invoice.setDate(LocalDate.now());
        invoice.setDescription("24 Tele");
        invoice.setPatient("Dieter MacAdam");
        invoice.setInsuranceType(InsuranceType.PRIVATE);
        /*
         * REMOVE
         */

        table.addCell(cell("Rechnungsnummer", 5));
        table.addCell(cell(valueOf(invoice.getId()), 9));
        table.addCell(cell("Rechnungsdatum", 4));
        table.addCell(cell(invoice.getDate().format(ofPattern("dd.MM.yyyy")), 8));
        table.addCell(cell("Art der Arbeit", 4));
        table.addCell(cell(invoice.getDescription(), 8));
        table.addCell(cell("Patient", 4));
        table.addCell(cell(invoice.getPatient(), 8));
        table.addCell(cell("Kassenart", 4));
        table.addCell(cell(invoice.getInsuranceType().toString(), 8));
        table.addCell(cell("Zahnfarbe", 6));
        table.addCell(cell("gelb", 10));

        table.setSpacingAfter(20);

        return table;
    }


    private PdfPTable costsTable(CostWrapperEntity costs) {
        PdfPTable table = new PdfPTable(new float[]{2, 9, 2, 3, 3});
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(ALIGN_CENTER);
        costsHeaderRow(table);

        /*
         * REMOVE
         */
        costs = new CostWrapperEntity();
        EffortJsonb effortb = new EffortJsonb();
        effortb.setPosition("1234");
        effortb.setDescription("Desinfektion");
        effortb.setQuantity(1.00);
        effortb.setPricePerUnit(285.50);
        costs.setEfforts(singletonList(effortb));
        MaterialJsonb material2 = new MaterialJsonb();
        material2.setPosition("7895");
        material2.setDescription("PlatinLloyd 100");
        material2.setNotes("BEGO Legierung CE ja");
        material2.setQuantity(3.60);
        material2.setPricePerUnit(51.25);
        material2.setMetal(true);
        MaterialJsonb material1 = new MaterialJsonb();
        material1.setPosition("7895");
        material1.setDescription("PlatinLloyd 100");
        material1.setQuantity(3.60);
        material1.setPricePerUnit(51.25);
        costs.setMaterials(Arrays.asList(material1, material2));
        /*
         * REMOVE
         */
        for (int i = 0; i < 18; i++) {

            for (EffortJsonb effort : costs.getEfforts()) {
                table.addCell(cell(effort.getPosition()));
                table.addCell(cell(effort.getDescription()));
                table.addCell(cell(valueOf(effort.getQuantity())));
                table.addCell(cellRight(round(effort.getPricePerUnit()).toPlainString()));
                BigDecimal sum = round(effort.getQuantity() * effort.getPricePerUnit());
                table.addCell(cellRight(sum.toPlainString()));
                effortsSum = effortsSum.add(sum);
            }
        }

        for (MaterialJsonb material : costs.getMaterials()) {
            table.addCell(cell(material.getPosition()));
            String description = material.getDescription();
            if (isNotBlank(material.getNotes())) {
                description += "\n ";
                description += material.getNotes();
            }
            table.addCell(cell(description));
            table.addCell(cell(valueOf(material.getQuantity())));
            table.addCell(cellRight(round(material.getPricePerUnit()).toPlainString()));
            BigDecimal sum = round(material.getQuantity() * material.getPricePerUnit());
            table.addCell(cellRight(sum.toPlainString()));
            if (material.isMetal()) {
                metalsSum = metalsSum.add(sum);
            }
            materialsSum = materialsSum.add(sum);
        }

        addEmptyRow(table);

        table.addCell(cell(""));
        PdfPCell metalsSumRow = cell("Berechnetes Edelmetall");
        metalsSumRow.setColspan(3);
        table.addCell(metalsSumRow);
        table.addCell(cellRight(metalsSum.toPlainString()));

        addEmptyRow(table);

        table.addCell(cell(""));
        table.addCell(fineCell("Diese Sonderanfertigung wurde unter Einhaltung der grundlegenden Anforderungen des Anhang I der Richlinie 93/42/EWG erstellt. Sie ist ausschliesslich fuer den oben genannten Patienten bestimmt.", 3));
        table.addCell(cell(""));

        return table;
    }

    private void costsHeaderRow(PdfPTable table) {
        table.setHeaderRows(1);
        table.addCell(headerCell("Position"));
        table.addCell(headerCell("Zahntechnische Leistung"));
        table.addCell(headerCell("Menge"));
        table.addCell(headerCellRight("Einzelpreis"));
        table.addCell(headerCellRight("Gesamtpreis"));
    }

    private PdfPTable footerDetails(InvoiceProperties invoiceProperties) {
        PdfPTable table = new PdfPTable(7);
        table.setTotalWidth(A4.getWidth() - 100);
        table.setLockedWidth(true);
        table.setHorizontalAlignment(ALIGN_CENTER);

        table.addCell(fineCell("Hinweis gemäß § 14 Absatz 4 Satz 1 Nr. 7 Umsatzsteuergesetz:", table.getNumberOfColumns()));
        table.addCell(fineCell("Zahlbar entsprechend Konditionenvereinbarung vom 30.7.2004", table.getNumberOfColumns()));

        addFooterRow(table, "Material", materialsSum, DEFAULT_FONT, TOP, NO_BORDER);
        addFooterRow(table, "Leistung", effortsSum, DEFAULT_FONT, NO_BORDER, NO_BORDER);
        BigDecimal netto = materialsSum.add(effortsSum);
        addFooterRow(table, "Netto", netto, DEFAULT_FONT, NO_BORDER, TOP);
        BigDecimal mwst = round(netto.multiply(new BigDecimal(invoiceProperties.getMwstInPercentage())).divide(new BigDecimal(100)));
        addFooterRow(table, "Mehrwertsteuer (7.00%)", mwst, DEFAULT_FONT, NO_BORDER, BOTTOM);
        BigDecimal total = netto.add(mwst);
        addFooterRow(table, "Gesamtbetrag", total, BOLD_FONT, BOTTOM, NO_BORDER);

        table.addCell(fineCell("Umsatzsteuer-Identifikationsnummer DE239653548", table.getNumberOfColumns()));

        return table;
    }

    /*
     * UTILS
     */

    private BigDecimal round(double sum) {
        return round(new BigDecimal(sum));
    }

    private BigDecimal round(BigDecimal sum) {
        return sum.setScale(2, ROUND_HALF_DOWN);
    }

    /*
     * CELL FORMATTING
     */

    private PdfPCell headerCell(String text) {
        return marginCell(text, ALIGN_LEFT);
    }

    private PdfPCell headerCellRight(String text) {
        return marginCell(text, ALIGN_RIGHT);
    }

    private PdfPCell marginCell(String text, int alignment) {
        PdfPCell cell = cell(text, alignment, DEFAULT_FONT, 3);
        cell.setPaddingTop(5);
        cell.setPaddingBottom(5);
        return cell;
    }

    private PdfPCell cell(String text) {
        return cell(text, ALIGN_LEFT, NO_BORDER);
    }

    private PdfPCell cellRight(String text) {
        return cell(text, ALIGN_RIGHT, NO_BORDER);
    }

    private PdfPCell cell(String text, int border) {
        return cell(text, ALIGN_LEFT, border);
    }

    private PdfPCell cell(String text, int alignment, int border) {
        return cell(text, alignment, DEFAULT_FONT, border);
    }

    private PdfPCell fineCell(String text, int colSpan) {
        PdfPCell cell = cell(text, ALIGN_LEFT, SMALL_FONT, NO_BORDER);
        cell.setColspan(colSpan);
        return cell;
    }

    private PdfPCell cell(String text, int alignment, Font font, int border) {
        PdfPCell cell = new PdfPCell();
        cell.setHorizontalAlignment(alignment);
        cell.setPhrase(new Phrase(text, font));
        cell.setBorder(border);
        return cell;
    }

    /*
     * ROW FORMATTING
     */

    private void addEmptyRow(PdfPTable table) {
        PdfPCell cell = cell(" ");
        cell.setColspan(table.getNumberOfColumns());
        table.addCell(cell);
    }

    private void addFooterRow(PdfPTable table, String text, BigDecimal sum, Font font, int outerBorder, int innerBorder) {
        table.addCell(cell(" ", outerBorder));
        table.addCell(cell(" ", outerBorder));
        table.addCell(cell(" ", outerBorder));
        table.addCell(cell(" ", outerBorder));
        PdfPCell textCell = cell(text, ALIGN_LEFT, font, outerBorder + innerBorder);
        textCell.setColspan(2);
        table.addCell(textCell);
        table.addCell(cell(sum.toPlainString() + " €", ALIGN_RIGHT, font, outerBorder + innerBorder));
    }

}
