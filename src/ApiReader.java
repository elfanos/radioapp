import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Read the xml file using saxparser method
 * @author Rasmus Dahlkvist
 */

public class ApiReader {
    List<Object> xmlObject;
    URL urlStream;
    URL test;
    private String update = "temp";

    private ApiCall getApiXml;
    /**
     * Constructor XmlReader
     * Initialize the url and create an
     * new url. Check the connection as well
     *
     * @param apiCall contains information important for
     *                using the SR api
     */
    public ApiReader(ApiCall apiCall) {
        try {
            if(apiCall.getSaxDirections().equals("scheduledepisode")){
                urlStream = new URL(apiCall.getApiUrl()
                        +"&pagination=false&date="+apiCall.dateToday());
            }else {
                urlStream = new URL(apiCall.getApiUrl());
            }
            this.getApiXml = apiCall;

        } catch (MalformedURLException e) {
            System.out.println("Cannot connect to the web api");
        }
        this.getApiXml = apiCall;
    }

    /**
     * Read xml using sax parser make an
     * inputstream of the url and then use handler
     * to retrieve information
     *
     * @return xmlObject a list of object that is gathered
     *          from the apis xml.
     */
    public List<Object> readXml() {
        //Create a empty link of users initially
        xmlObject = new ArrayList<Object>();
        try {
            //Create default handler instance
            XmlHandler handler = new XmlHandler(getApiXml);

            //Create parser from factory
            XMLReader parser = XMLReaderFactory.createXMLReader();

            //Register handler with parser
            parser.setContentHandler(handler);

            //Create an input source from the XML input stream
            //InputSource source = new InputSource(in);

            //Creat an input source from the URL xml
            InputStream temp = urlStream.openStream();
            InputSource source = new InputSource(temp);

            //Xml file
            /*InputStream in = new FileInputStream(
                    new File(getApiXml.getStoredXml()));
            InputSource source = new InputSource(in);*/

            //parse the document
            parser.parse(source);

            //populate the parsed users list in above
            // created empty list; You can return from here also.
            xmlObject = handler.getXmlInfo();
            this.setUpdate(handler.getUpdate());

        } catch (SAXException e) {
            System.out.println("No connection to the apie");
            //e.printStackTrace();
        } catch (IOException e) {
            System.out.println("No connection to the apie");
            //e.printStackTrace();
        } finally {

        }
       if(getApiXml.getSaxDirections().equals("scheduledepisode")) {
            if (!getApiXml.getTwelveBefore()) {
                this.handleXmlFile(getApiXml.dateYesterDay());
            }
            if (getApiXml.getTwelveAfter()) {
                this.handleXmlFile(getApiXml.dateTomorrow());
            }
        }
        return xmlObject;
    }

    /**
     * More or like readXml but add more information
     * to the xmlObject, so the schedule show information
     * 12 hours back and 12 hours before.
     *
     * @param url to the api as string
     * */
    public void handleXmlFile(String url){
        try {
            try {
                test = new URL(getApiXml.
                        getApiUrl() + "&pagination=false&date="+url);
            } catch (MalformedURLException e) {
                System.out.println("Cannot connect to the web api");
            }
            XmlHandler handler2 = new XmlHandler(getApiXml);

            //Create parser from factory
            XMLReader parser = XMLReaderFactory.createXMLReader();

            //Register handler with parser
            parser.setContentHandler(handler2);

            //Create an input source from the XML input stream
            //InputSource source = new InputSource(in);

            //Creat an input source from the URL xml
            InputStream temp = test.openStream();
            InputSource source = new InputSource(temp);

            //Xml file
            /*InputStream in = new FileInputStream(
                    new File(getApiXml.getStoredXml()));
            InputSource source = new InputSource(in);*/

            //parse the document
            parser.parse(source);
            xmlObject.addAll(handler2.getXmlInfo());
        }catch (SAXException e) {
            System.out.println("No connection to the apie");
            //e.printStackTrace();
        } catch (IOException e) {
            System.out.println("No connection to the apie");
            //e.printStackTrace();
        } finally {

        }
    }

    /**
     * Set update
     *
     * @param update a string that is used to check if
     *               it is updated
     */
    public void setUpdate(String update) {
        this.update = update;
    }

}

