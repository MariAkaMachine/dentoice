package com.mariakamachine.dentoice.service;

import com.mariakamachine.dentoice.data.entity.InvoiceEntity;
import com.mariakamachine.dentoice.model.FileResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static java.lang.String.format;

@Service
public class MailService {

    private final InvoiceService invoiceService;
    private final JavaMailSender mailSender;

    @Autowired
    public MailService(InvoiceService invoiceService, JavaMailSender mailSender) {
        this.invoiceService = invoiceService;
        this.mailSender = mailSender;
    }

    public void sendXmlMessage(long id) throws MessagingException {
        sendMessage(invoiceService.getById(id), invoiceService.getXmlById(id), "XML");
    }


    public void sendPdfMessage(long id) throws MessagingException {
        sendMessage(invoiceService.getById(id), invoiceService.getPdfById(id), "PDF");
    }

    private void sendMessage(InvoiceEntity invoice, FileResource fileResource, String type) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(invoice.getDentist().getEmail());
        helper.setCc("udo-edith@t-online.de");
        helper.setSubject(format("%s-Rechnung - %s", type, invoice.getPatient()));
        helper.setText(format("Hallo,\n\n" +
                        "im Anhang dieser Email befindet sich die %s-Rechnung mit der Nummer \"%s\" für den/die Patient/in %s.\n\n" +
                        "Mit freundlichen Grüßen,\n" +
                        "Dentaltechnik Udo Baumann\n\n" +
                        "07073 / 919 719\n" +
                        "Waldkapellenweg 7\n" +
                        "72108 Rottenburg a.N.",
                type, invoice.getXmlNumber(), invoice.getPatient()));
        helper.addAttachment(fileResource.getFileName(), fileResource.getResource());
        mailSender.send(message);
    }

}
