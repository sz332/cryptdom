package hu.acme.cryptodom;

import org.junit.Assert;
import org.junit.Test;

public class AcmeDocumentDecoderTest {

    @Test
    public void testDecode() throws DocumentDecodeException {
        AcmeDocumentDecoder decoder = new AcmeDocumentDecoder();
        AcmeDocument result = decoder.decode(AcmeDocumentDecoderTest.class.getResourceAsStream("test.xml"));
        Assert.assertNotNull(result);
        Assert.assertEquals("First document", result.getName());
    }

}
