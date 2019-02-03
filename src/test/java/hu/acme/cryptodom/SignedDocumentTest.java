package hu.acme.cryptodom;

import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import hu.acme.cryptodom.dom.DocumentTemplate;
import hu.acme.cryptodom.dom.SignedDocument;
import hu.acme.cryptodom.keystore.KeyRepository;

// https://www.oracle.com/technetwork/articles/javase/dig-signature-api-140772.html
// https://www.w3.org/TR/xmldsig-core/
// https://docs.oracle.com/javase/8/docs/api/javax/xml/crypto/dom/package-frame.html
public class SignedDocumentTest {

    private static final String ALIAS = "alias";
    private static final String PASSWORD = "changeme";
    
    @Test
    public void testSign() {
        InMemoryKeyStore keyStore = new InMemoryKeyStore(ALIAS, PASSWORD);
        InputStream stream = SignedDocumentTest.class.getResourceAsStream("test.xml");
        KeyRepository repo = new KeyRepository(keyStore.asKeyStore(), ALIAS, PASSWORD);

        SignedDocument signedDocument = new SignedDocument(new DocumentTemplate(stream), repo);
        String result = signedDocument.asSigned();

        Assert.assertNotNull(result);
    }

}
