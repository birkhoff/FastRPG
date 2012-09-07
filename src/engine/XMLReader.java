package engine;

import java.io.File;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

public class XMLReader {
	
	private DocumentBuilder docBuilder;
	
    public XMLReader() {
    	try {
    		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            this.docBuilder = docBuilderFactory.newDocumentBuilder();
        } catch (Exception err) {
        	err.printStackTrace();
        }
    }
    
    public Document getDocument(String target){
    	try {
    		Document doc = docBuilder.parse (new File(target)); // normalize text representation
            doc.getDocumentElement ().normalize ();
            return doc;
    	} catch(Exception e){
    		e.printStackTrace();
    	}
    	//TODO better fallback solution
    	return null;	
    }
}
