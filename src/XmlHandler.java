
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.lang.reflect.*;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * Created by Rasmus on 11/06/17.
 * Handler class sax parser to read and get data from
 * the xml
 * @author Rasmus Dahlkvist
 */
public class XmlHandler extends DefaultHandler {

    private List<Object> listOfObjects = new ArrayList<>();

    private Stack elementStack = new Stack();

    private Stack<Object> objectStack = new Stack<>();

    private String update = "hola";

    private ApiCall getXmlNodes;

    private ApiHandler apiHandler;

    private Class<?> apiModel = null;

    private String channelName = null;


    public XmlHandler(ApiCall apiCall){
        this.getXmlNodes = apiCall;
        apiHandler = new ApiHandler(apiCall);
        apiModel = this.getXmlNodes.getApiModel();
        //System.out.println("lol" + this.getXmlNodes.getApiModel());
    }
    /**
     * Checks the start element of the xmlfile
     *
     * @param uri to the position of the xml/ like sr api for this program
     * @param localName check the node namespace of the xml
     * @param qName check the full name of the node.
     * @param attributes inside a node in the xml
     */
    @SuppressWarnings("unchecked")
    @Override
    public void startElement(String uri, String localName,
                             String qName,
                             Attributes attributes) throws SAXException {

        this.elementStack.push(qName);
        if (qName.equals("LMSData")) {
            update = attributes.getValue("LastUpdate");

        }
        if (qName.equals(getXmlNodes.getSaxDirections())) {
            try {
                channelName = attributes.getValue(1);
                Class<?> t = Class.forName(apiModel.getName());
                this.objectStack.push(t.newInstance());

            }catch (ClassNotFoundException e){

                System.out.println("no class" + e);
            }catch (InstantiationException e){
                System.out.println("No instance" + e);
            }catch (IllegalAccessException e){
                System.out.println("Cant access");
            }
        }
    }

    /**
     * Check the endElement in the xmlfile
     *
     * @param uri to the position of the xml/ like sr api for this program
     * @param localName check the node namespace of the xml
     * @param qName check the full name of the node.
     */
    @Override
    public void endElement(String uri, String localName,
                           String qName) throws SAXException {
        //Remove last added  element
        this.elementStack.pop();

        //User instance has been constructed so pop it
        // from object stack and push in userList
        if (qName.equals(getXmlNodes.getSaxDirections())) {
            Object object = this.objectStack.pop();
            if(object instanceof Schedule){
                if(((Schedule) object).getEpisode() != null){
                    this.listOfObjects.add(object);
                }
            }else {
                this.listOfObjects.add(object);
            }

        }

    }

    /**
     * Read through the xml file from
     * start to finnish, and retrive
     * values from the stack which sends
     * the values to the apiHandler.
     *
     * @param ch  all the chars from the xml.
     * @param start  the start position in the xml
     * @param length  how big portion of the xml that is
     *                going to be read by the sax parser
     * @throws SAXException
     */
    @Override
    public void characters(char ch[], int start, int length)
            throws SAXException {
        String element = new String(ch, start, length);

        if (element.length() == 0) {
            return;
        }
        if (!objectStack.isEmpty()) {
            if (this.checkIfStackIsNull(this.getCurrentValue())) {
                apiHandler.insertValueInApiModel(element,this.getCurrentValue(),
                        currentElement(), apiModel, channelName);
            }
        }

    }

    /**
     * Get the current element in the stack
     *
     * @return current element in stack as a string
     */
    private String currentElement() {
        return (String) this.elementStack.peek();
    }

    /**
     * @return a boolean which the if an object is null
     */
    private Boolean checkIfStackIsNull(Object object){
        if(object != null){ return true; }
        else return false;
    }

    /**
     * @return stack.peek() the top value in the stack
     * */
    private Object getCurrentValue() {
        return this.objectStack.peek();
    }

    /**
     * @return list of objects
     */
    public List<Object> getXmlInfo() {
        return listOfObjects;
    }

    /**
     * @return update when the xml-file was last retrieved
     */
    public String getUpdate() {
        return update;
    }


}
