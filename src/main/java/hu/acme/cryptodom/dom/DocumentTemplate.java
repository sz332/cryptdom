package hu.acme.cryptodom.dom;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class DocumentTemplate {

    private InputStream stream;
    private Document document;

    public DocumentTemplate(Document document) {
        this.document = document;
    }

    public DocumentTemplate(InputStream stream) {
        this.stream = stream;
    }

    public Document asDocument() throws DocumentParseException {
        if (this.document != null) {
            return document;
        }

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            this.document = builder.parse(stream);
            this.document.getDocumentElement().normalize();

            return document;

        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new DocumentParseException(e);
        }
    }

}
