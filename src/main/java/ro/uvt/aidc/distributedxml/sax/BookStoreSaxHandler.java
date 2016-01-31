package ro.uvt.aidc.distributedxml.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by adrian on 1/31/16.
 */
public class BookStoreSaxHandler extends DefaultHandler {
    boolean title = false;
    boolean author = false;
    boolean year = false;
    boolean price = false;

    public void startElement(String uri, String localName,String qName,
                             Attributes attributes) throws SAXException {

        System.out.println("Start Element :" + qName);

        if (qName.equalsIgnoreCase("TITLE")) {
            title = true;
        }

        if (qName.equalsIgnoreCase("AUTHOR")) {
            author = true;
        }

        if (qName.equalsIgnoreCase("YEAR")) {
            year = true;
        }

        if (qName.equalsIgnoreCase("PRICE")) {
            price = true;
        }

    }

    public void endElement(String uri, String localName,
                           String qName) throws SAXException {

        System.out.println("End Element :" + qName);

    }

    public void characters(char ch[], int start, int length) throws SAXException {

        if (title) {
            System.out.println("Title : " + new String(ch, start, length));
            title = false;
        }

        if (author) {
            System.out.println("Author : " + new String(ch, start, length));
            author = false;
        }

        if (year) {
            System.out.println("Year : " + new String(ch, start, length));
            year = false;
        }

        if (price) {
            System.out.println("Price : " + new String(ch, start, length));
            price = false;
        }

    }
}
