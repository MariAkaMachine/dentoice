package com.mariakamachine.dentoice.util.invoice.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;

import static java.time.LocalDate.parse;

public class LocalDateXmlAdapter extends XmlAdapter<String, LocalDate> {

    @Override
    public LocalDate unmarshal(String date) {
        return parse(date);
    }

    @Override
    public String marshal(LocalDate localDate) {
        return localDate.toString();
    }

}
