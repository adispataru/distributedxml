package ro.uvt.aidc.distributedxml;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ro.uvt.aidc.distributedxml.sax.BookStoreSaxHandler;

import javax.xml.parsers.*;
import java.io.*;

/**
 * Created by adrian on 1/31/16.
 */
public class App {

    public static void main(String[] args) {
        try {
            parseWithDom(args);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            parseWithSax(args);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void parseWithSax(String[] args) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();


        BookStoreSaxHandler handler = new BookStoreSaxHandler();
        saxParser.parse("src/main/resources/document.xml", handler);


    }

    public static void parseWithDom(String[] args) throws Exception{

        File fXmlFile = new File("src/main/resources/document.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        doc.getDocumentElement().normalize();
        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

        NodeList nList = doc.getElementsByTagName("book");

        System.out.println("----------------------------");

        for (int temp = 0; temp < nList.getLength(); temp++) {

            Node nNode = nList.item(temp);

            System.out.println("\nCurrent Element :" + nNode.getNodeName());


            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;

                System.out.println("Category : " + eElement.getAttribute("category"));
                System.out.println("Title : " + eElement.getElementsByTagName("title").item(0).getTextContent());

                NodeList authorList = eElement.getElementsByTagName("author");
                System.out.println("Authors : ");
                for(int i = 0; i < authorList.getLength(); i++){
                    System.out.println("\t" + authorList.item(i).getTextContent());
                }

                System.out.println("Year : " + eElement.getElementsByTagName("year").item(0).getTextContent());
                System.out.println("Price : " + eElement.getElementsByTagName("price").item(0).getTextContent());

            }
        }
    }

    public static String readStream(InputStream is) {
        StringBuilder sb = new StringBuilder();
        try {
            Reader r = new InputStreamReader(is, "UTF-8");
            int c = 0;
            while ((c = r.read()) != -1) {
                sb.append((char) c);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
}
