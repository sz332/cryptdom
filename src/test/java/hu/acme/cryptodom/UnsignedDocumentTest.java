package hu.acme.cryptodom;

import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import hu.acme.cryptodom.dom.DocumentTemplate;
import hu.acme.cryptodom.dom.UnsignedDocument;
import hu.acme.cryptodom.keystore.KeyRepository;

// https://www.oracle.com/technetwork/articles/javase/dig-signature-api-140772.html
// https://www.w3.org/TR/xmldsig-core/
// https://docs.oracle.com/javase/8/docs/api/javax/xml/crypto/dom/package-frame.html
// http://www.bouncycastle.org/wiki/display/JA1/X.509+Public+Key+Certificate+and+Certification+Request+Generation
// https://www.ibm.com/support/knowledgecenter/en/SSYKE2_7.0.0/com.ibm.java.security.component.70.doc/security-component/keytoolDocs/x500dnames.html
public class UnsignedDocumentTest {

    private static final String ALIAS = "alias";
    private static final String PASSWORD = "changeme";
    
    @Test
    public void testSign() {
        InMemoryKeyStore keyStore = new InMemoryKeyStore(ALIAS, PASSWORD);
        KeyRepository repo = new KeyRepository(keyStore.asKeyStore(), ALIAS, PASSWORD);

        InputStream stream = UnsignedDocumentTest.class.getResourceAsStream("test.xml");
        
        UnsignedDocument signedDocument = new UnsignedDocument(new DocumentTemplate(stream));
        
        String result = signedDocument.asSigned(repo);

        Assert.assertNotNull(result);
    }

}
