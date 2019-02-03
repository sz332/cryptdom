package hu.acme.cryptodom;

import org.w3c.dom.Document;

import hu.acme.cryptodom.dom.DomDocument;
import hu.acme.cryptodom.dom.NodeQueryException;

public class AcmeDocumentParser {

    private final Document document;

    public AcmeDocumentParser(Document document) {
        this.document = document;
    }

    public AcmeDocument parse() throws DocumentDecodeException {

        try {
            DomDocument dom = new DomDocument(document);
            
            String name = dom.asString("/acme:root/acme:header/acme:name/text()");

            return new AcmeDocument(name, null);

        } catch (NodeQueryException e) {
            throw new DocumentDecodeException(e);
        }
    }

}
