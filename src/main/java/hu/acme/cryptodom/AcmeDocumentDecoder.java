package hu.acme.cryptodom;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class AcmeDocumentDecoder {
    
    public AcmeDocument decode(InputStream resourceAsStream) throws DocumentDecodeException {

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            Document document = builder.parse(resourceAsStream);
            document.getDocumentElement().normalize();
            
            return new AcmeDocumentParser(document).parse();

        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new DocumentDecodeException(e);
        }
    }

}
