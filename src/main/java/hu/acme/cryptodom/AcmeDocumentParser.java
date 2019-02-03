package hu.acme.cryptodom;

import hu.acme.cryptodom.dom.DocumentTemplate;
import hu.acme.cryptodom.dom.DomDocument;
import hu.acme.cryptodom.dom.NodeQueryException;

public class AcmeDocumentParser {

    private final DocumentTemplate template;

    public AcmeDocumentParser(DocumentTemplate template) {
        this.template = template;
    }

    public AcmeDocument parse() throws DocumentDecodeException {

        try {
            DomDocument dom = new DomDocument(template);
            String name = dom.asString("/acme:root/acme:header/acme:name/text()");

            return new AcmeDocument(name, null);

        } catch (NodeQueryException e) {
            throw new DocumentDecodeException(e);
        }
    }

}
