package hu.acme.cryptodom.dom;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Collections;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class SignedDocument {

    private final Document document;
    private final SignKeyStore signKeyStore;

    public SignedDocument(Document document, SignKeyStore signKeyStore) {
        this.document = document;
        this.signKeyStore = signKeyStore;
    }

    public String asSigned() {
        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

        try {
            // Create a DOMSignContext and specify the RSA PrivateKey and
            // location of the resulting XMLSignature's parent element.

            XmlSignInformation signInfo = signKeyStore.asInformation(fac);

            Reference ref = fac.newReference("", fac.newDigestMethod(DigestMethod.SHA1, null),
                    Collections.singletonList(fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)), null, null);
            
            DOMSignContext dsc = new DOMSignContext(signInfo.keyEntry().getPrivateKey(), document.getDocumentElement());

            // Create the XMLSignature, but don't sign it yet.
            SignedInfo si = fac.newSignedInfo(
                    fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
                    fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(ref));

            XMLSignature signature = fac.newXMLSignature(si, signInfo.keyInfo());

            // Marshal, generate, and sign the enveloped signature.
            signature.sign(dsc);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer();
            trans.transform(new DOMSource(document), new StreamResult(bos));

            return new String(bos.toByteArray(), StandardCharsets.UTF_8);

        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | KeyStoreException | CertificateException | IOException
                | UnrecoverableEntryException | MarshalException | XMLSignatureException | TransformerException e) {
            e.printStackTrace();
            return null;
        }

    }



}
