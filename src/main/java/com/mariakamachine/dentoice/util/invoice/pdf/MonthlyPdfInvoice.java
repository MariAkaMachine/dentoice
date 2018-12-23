package com.mariakamachine.dentoice.util.invoice.pdf;

import com.itextpdf.text.pdf.PdfPTable;
import com.mariakamachine.dentoice.config.properties.InvoiceProperties;
import com.mariakamachine.dentoice.data.entity.InvoiceEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

@Slf4j
public class MonthlyPdfInvoice {

    public List<PdfPTable> generateTables(InvoiceProperties invoiceProperties, List<InvoiceEntity> invoices) {


        return Collections.emptyList();
    }

}
