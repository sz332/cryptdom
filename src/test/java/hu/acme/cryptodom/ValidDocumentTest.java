package hu.acme.cryptodom;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;

import hu.acme.cryptodom.dom.DocumentParseException;
import hu.acme.cryptodom.dom.DocumentTemplate;
import hu.acme.cryptodom.dom.ValidDocument;

public class ValidDocumentTest {

    @Test
    public void testDocumentValidity() throws DocumentParseException {

        ValidDocument validDocument = new ValidDocument(
                                        new DocumentTemplate(
                                                ValidDocumentTest.class.getResourceAsStream("signed.xml")
                                                )
                                        );
        
        Document document = validDocument.asDocument();
        Assert.assertNotNull(document);
    }

}
