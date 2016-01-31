package ro.uvt.aidc.distributedxml;

/**
 * Created by adrian on 1/31/16.
 */
public class Processors {


    public static class WebCategoryProcessor{
        public String process(String payload) {
            System.out.println("Web books: \n " + payload);
            return payload;
        }
    }

    public static class PaperbackProcessor {

        public String process(String payload) {
            System.out.println("Paperback books: \n " + payload);
            return payload;
        }
    }

    public static class CheapProcessor {

        public String process(String payload) {
            System.out.println("Cheap books: \n " + payload);
            return payload;
        }
    }

    public static class RomanianProcessor {

        public String process(String payload) {
            System.out.println("Romanian books: \n " + payload);
            return payload;
        }
    }

    public static class EnglishProcessor {

        public String process(String payload) {
            System.out.println("English books: \n " + payload);
            return payload;
        }
    }
}
