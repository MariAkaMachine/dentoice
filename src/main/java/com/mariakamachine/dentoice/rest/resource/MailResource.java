package com.mariakamachine.dentoice.rest.resource;

import com.mariakamachine.dentoice.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;

@RestController
@RequestMapping("/mail")
@CrossOrigin
@Validated
public class MailResource {

    private final MailService mailService;

    @Autowired
    public MailResource(MailService mailService) {
        this.mailService = mailService;
    }

    @GetMapping(path = "/{id}/xml", produces = APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public void sendXmlMessage(@PathVariable Long id) throws MessagingException {
        mailService.sendXmlMessage(id);
    }

    @GetMapping(path = "/{id}/pdf", produces = APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public void sendPdfMessage(@PathVariable Long id) throws MessagingException {
        mailService.sendPdfMessage(id);
    }

}
