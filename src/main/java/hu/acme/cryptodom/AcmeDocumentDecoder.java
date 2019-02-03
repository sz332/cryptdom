package hu.acme.cryptodom;

import java.io.InputStream;

import hu.acme.cryptodom.dom.DocumentTemplate;

public class AcmeDocumentDecoder {

    public AcmeDocument decode(InputStream resourceAsStream) throws DocumentDecodeException {
        DocumentTemplate template = new DocumentTemplate(resourceAsStream);
        return new AcmeDocumentParser(template).parse();
    }

}
