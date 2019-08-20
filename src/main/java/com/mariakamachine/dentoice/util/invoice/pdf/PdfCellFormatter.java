package com.mariakamachine.dentoice.util.invoice.pdf;


import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.HorizontalAlignment;

import static com.itextpdf.layout.borders.Border.NO_BORDER;
import static com.itextpdf.layout.property.HorizontalAlignment.LEFT;
import static com.itextpdf.layout.property.HorizontalAlignment.RIGHT;


public class PdfCellFormatter {

//    static final Font DEFAULT_FONT = getFont(COURIER, 9);
//    static final Font BOLD_FONT = getFont(COURIER_BOLD, 9);
//    private static final Font SMALL_FONT = getFont(COURIER, 7);

    static Cell headerCell(String text) {
        return marginCell(text, LEFT);
    }

    static Cell headerCellRight(String text) {
        return marginCell(text, RIGHT);
    }

    static Cell marginCell(String text, HorizontalAlignment alignment) {
        Cell cell = cell(text, alignment, null, null);
//        Cell cell = cell(text, alignment, DEFAULT_FONT, 3);
        cell.setPaddingTop(5);
        cell.setPaddingBottom(5);
        return cell;
    }

    static Cell cell(String text) {
        return cell(text, LEFT, NO_BORDER);
//        return cell(text, LEFT, NO_BORDER);
    }

    static Cell cellRight(String text) {
        return cell(text, RIGHT, NO_BORDER);
//        return cell(text, RIGHT, NO_BORDER);
    }

    static Cell cell(String text, Border border) {
        return cell(text, LEFT, border);
    }

    static Cell cell(String text, HorizontalAlignment alignment, Border border) {
        return cell(text, alignment, null, border);
//        return cell(text, alignment, DEFAULT_FONT, border);
    }

    static Cell fineCell(String text, int colSpan) {
        return new Cell()
                .setHorizontalAlignment(LEFT)
                .add(new Paragraph(text))
                .setFontSize(8f)
                .setItalic()
                .setBorder(NO_BORDER);
        //        Cell cell = cell(text, LEFT, null, 0);
//        Cell cell = cell(text, ALIGN_LEFT, SMALL_FONT, NO_BORDER);
//        cell.setColspan(colSpan);
//        return cell;
    }

    static Cell cell(String text, HorizontalAlignment alignment, PdfFont font, Border border) {
        return new Cell()
                .setHorizontalAlignment(alignment)
                .add(new Paragraph(text))
//                .setFont(font)
                .setBorder(border);
    }

    /*
     * ROW FORMATTING
     */

//    static void addEmptyRow(Table table) {
//        Cell cell = cell(" ");
//        cell.setColspan(table.getNumberOfColumns());
//        table.addCell(cell);
//    }
//
//    static void addFooterRow(PdfPTable table, String text, BigDecimal sum, Font font, int outerBorder, int innerBorder) {
//        Cell blankCell = cell(" ", outerBorder);
//        blankCell.setColspan(4);
//        table.addCell(blankCell);
//        Cell textCell = cell(text, ALIGN_LEFT, font, outerBorder + innerBorder);
//        textCell.setColspan(2);
//        table.addCell(textCell);
//        table.addCell(cell(sum.toPlainString() + " €", ALIGN_RIGHT, font, outerBorder + innerBorder));
//    }

}
