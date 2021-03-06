package ro.uvt.aidc.distributedxml;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.util.jndi.JndiContext;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import ro.uvt.aidc.distributedxml.sax.BookStoreSaxHandler;

import javax.xml.parsers.*;
import java.io.*;

/**
 * Created by adrian on 1/31/16.
 */
public class App {

    public static void main(String[] args) throws Exception {
        homework3(args);
    }

    public static void homework3(String[] args) throws Exception{
        JndiContext jndiContext = new JndiContext();
        jndiContext.bind("paperback", new Processors.PaperbackProcessor());
        jndiContext.bind("webCategory", new Processors.WebCategoryProcessor());
        jndiContext.bind("cheap", new Processors.CheapProcessor());
        jndiContext.bind("romanian", new Processors.RomanianProcessor());
        jndiContext.bind("english", new Processors.EnglishProcessor());
        CamelContext camelContext = new DefaultCamelContext(jndiContext);
        try {
            camelContext.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("direct:start")
                            .split(xpath("//book[@cover = 'paperback']"))
                            .to("bean:paperback")
                            .end()
                            .split(xpath("//book[@category = 'web']"))
                            .to("bean:webCategory")
                            .end()
                            .split(xpath("//book[./price < 5]"))
                            .to("bean:cheap")
                            .end()
                            .split(xpath("//book[./title[@lang = 'en']]"))
                            .to("bean:english")
                            .end()
                            .split(xpath("//book[./title[@lang = 'ro']]"))
                            .to("bean:romanian")
                            .end();
                }
            });

            camelContext.start();
            ProducerTemplate template = camelContext.createProducerTemplate();
            String xml = readStream(new FileInputStream("src/main/resources/document.xml"));
            template.sendBody("direct:start", xml);
            template.sendBody("direct:start", "<order><product>books</product><available>false</available></order>");
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            try{
                camelContext.stop();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void homework2(String[] args) {
        CamelContext context = new DefaultCamelContext();
        try {
            context.addComponent("activemq", ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=false"));
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("activemq:queue:test.queue")
                            .to("validator:file:src/main/resources/document.xsd")
                            .to("stream:out");
                }
            });
            ProducerTemplate template = context.createProducerTemplate();
            context.start();
            String xml = readStream(new FileInputStream("src/main/resources/document.xml"));
            template.sendBody("activemq:test.queue", xml);
            Thread.sleep(2000);

        }catch (Exception e){
            e.printStackTrace();
        } finally {
            try{
                context.stop();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void homework1(String[] args){
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
