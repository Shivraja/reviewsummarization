/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsoup.parser;

import com.jaunt.ResponseException;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import com.jaunt.UserAgent;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Saishibi
 */
public class JsoupParser {

    static String FinalLinks[] = new String[1000];
    static int finalcount = 0;
    static String FlipkartLinks[] = new String[100];
    static int no_of_flipkart_links;
    static String AmazonLinks[] = new String[100];
    static int no_of_amazon_links;
    static int count = 1;
    static UserAgent userAgent = new UserAgent();         //create new userAgent (headless browser)

    static String productName;
    static Scanner scanner;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ResponseException {
        // TODO code application logic here
        System.out.println("Jsoup Started");
        scanner = new Scanner(System.in);
        //productName = scanner.nextLine();
            
        productName = "redmi";
        parseAmazon(productName);
        parseFlipkart(productName);

        /* // String url = "http://www.gsmarena.com/motorola_moto_g_(3rd_gen)-reviews-7247.php";
         // String url ="http://www.flipkart.com/moto-g-3rd-generation/product-reviews/ITME9YSJR7MFRY3N";
         String url = "http://www.flipkart.com/redmi-1s/product-reviews/ITMDZ6ZPUATKGFJP";
         //   String url = "http://www.amazon.com/Samsung-UN105S9-Curved-105-Inch-Ultra/product-reviews/B00L403O8U";
         Document doc = Jsoup.connect(url).get();
         // Elements headLines = doc.select("#mp-itn b a");
         System.out.println(doc.text());
         //System.out.println(doc.getElementById("cm_cr-review_list"));
         //  Element review = doc.getElementById("cm_cr-review_list");
         Elements review = doc.getElementsByClass("review-text");
         System.out.println(review.text());
         for (Node e : review.first().childNodes()) {
         //System.out.println("Child : "+e.childNodeSize());
         if (e.childNodeSize() > 0) {    
         for (Node c : e.childNodes()) {
  
         // System.out.println(c.toString());
         // System.out.println("********************");
         }
         } else {
         System.out.println(e.toString());
         System.out.println("********************");
         }
         }
         */
        //System.out.println(review.text());
        writeLinksToFile();
    }

    public static void parseFlipkart(String productName) throws ResponseException {
        getFlipkartLinks(productName);
        System.out.println(FlipkartLinks.length);
        for (String link : FlipkartLinks) {
            if (link == null) {
                continue;
            }
            System.out.println("Parsing Started for link : " + link);
            parseFlipkartLink(link);
        }
    }

    public static void parseAmazon(String productName) {
        getAmazonLinks(productName);
        System.out.println(AmazonLinks.length);
        for (String link : AmazonLinks) {
            if (link == null) {
                continue;
            }
            System.out.println("Parsing Started for link : " + link);
            parseAmazonLink(link);
        }

    }

    public static void parseFlipkartLink(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements reviews = doc.getElementsByClass("review-text");
            for (Element review : reviews) {
                System.out.println("**********");
                System.out.println(review.text());  
            }
        } catch (IOException ex) {
            Logger.getLogger(JsoupParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void parseAmazonLink(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Element review = doc.getElementById("cm_cr-review_list");
         //   reviews = doc.getElementsByTag("<span class=\"a-size-base review-text\">");
            System.out.println(review.text());
            Elements reviews = review.getAllElements();
            for (Element e : reviews) {
                System.out.println("**********");
                System.out.println(e.text());  
            }
        } catch (IOException ex) {
            Logger.getLogger(JsoupParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void getFlipkartLinks(String product_name) throws ResponseException {
        //Code for Flipkart
        try {
            userAgent.visit("http://google.com");          //visit google
            userAgent.doc.apply(product_name + " reviews flipkart");            //apply form input (starting at first editable field)
            userAgent.doc.submit("Google Search");         //click submit button labelled "Google Search"
            String LinkArray[] = new String[1000];
            int linkcount = 0;
            com.jaunt.Elements links = userAgent.doc.findEvery("<h3 class=r>").findEvery("<a>");  //find search result links
            for (com.jaunt.Element link : links) {
                String s = link.getAt("href");
                if (s.contains("www.flipkart.com")) {
                    String temp1[] = s.split("&");
                    String temp2[] = temp1[0].split("=");
                    LinkArray[linkcount++] = temp2[1];
                }
            }

            for (int i = 0; i < linkcount; i++) {
                if (LinkArray[i].contains("review")) {
                    FlipkartLinks[finalcount] = LinkArray[i];
                    FinalLinks[finalcount++] = LinkArray[i];
                }
            }
            no_of_flipkart_links = finalcount;
            System.out.println("Flipkart Link Count : " + no_of_flipkart_links);
        } catch (Exception e) {
            System.out.println("Exception inside flipkart function");
        }
    }

    public static void getAmazonLinks(String product_name) {
            //Code for Amazon

        try {
            userAgent = new UserAgent();         //create new userAgent (headless browser)
            userAgent.visit("http://google.com");          //visit google
            userAgent.doc.apply(product_name + " reviews amazon");            //apply form input (starting at first editable field)
            userAgent.doc.submit("Google Search");         //click submit button labelled "Google Search"
            String LinkArray[] = new String[1000];
            int linkcount = 0;
            com.jaunt.Elements links = userAgent.doc.findEvery("<h3 class=r>").findEvery("<a>");  //find search result links
            for (com.jaunt.Element link : links) {
                String s = link.getAt("href");
                System.out.println(s);
                if (s.contains("www.amazon")) {
                    String temp1[] = s.split("&");
                    String temp2[] = temp1[0].split("=");
                    LinkArray[linkcount++] = temp2[1];
                }
            }
            int amazon_count = 0;
            for (int i = 0; i < linkcount; i++) {
                if (LinkArray[i].contains("review")) {
                    AmazonLinks[amazon_count++] = LinkArray[i];
                    FinalLinks[finalcount++] = LinkArray[i];
                }
            }
            no_of_amazon_links = amazon_count;
        } catch (Exception e) {
            System.out.println("Exception inside Amazon function");
        }

        System.out.println("Amazon Link Count : " + no_of_amazon_links);
    }

    public static void writeLinksToFile() throws IOException {

        File link_file = new File("links.txt");
        link_file.createNewFile();
        FileWriter writer = new FileWriter(link_file);

        for (int i = 0; i < finalcount; i++) {
            //System.out.println(FinalLinks[i]);
            writer.write("\n" + FinalLinks[i]);
        }
        writer.flush();
        writer.close();
    }
}
