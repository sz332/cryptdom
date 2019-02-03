package hu.acme.cryptodom.dom;

import java.security.Key;
import java.security.KeyException;
import java.security.PublicKey;
import java.util.Iterator;
import java.util.List;

import javax.xml.crypto.AlgorithmMethod;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.KeySelectorException;
import javax.xml.crypto.KeySelectorResult;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyValue;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class ValidDocument {

    private final DocumentTemplate template;

    public ValidDocument(DocumentTemplate template) {
        this.template = template;
    }

    public Document asDocument() throws DocumentParseException {

        try {
            Document document = template.asDocument();

            DomDocument dom = new DomDocument(document);
            Node signatureNode = dom.asNode(XMLSignature.XMLNS, "Signature");

            if (signatureNode == null) {
                throw new DocumentParseException("Signature not found");
            }

            // Create a DOMValidateContext and specify a KeySelector
            // and document context.
            DOMValidateContext valContext = new DOMValidateContext(new X509KeySelector(), signatureNode);
            //DOMValidateContext valContext = new DOMValidateContext(new KeyValueKeySelector(), signatureNode);

            // Unmarshal the XMLSignature.
            XMLSignature signature = XMLSignatureFactory.getInstance("DOM").unmarshalXMLSignature(valContext);

            if (!signature.validate(valContext)) {
                
                boolean sv = signature.getSignatureValue().validate(valContext);
                
                if (sv == false) {
                    Iterator<?> i = signature.getSignedInfo().getReferences().iterator();
                    for (int j=0; i.hasNext(); j++) {
                        boolean refValid = ((Reference) i.next()).validate(valContext);
                        System.out.println("ref["+j+"] validity status: " + refValid);
                    }
                }
                
                throw new DocumentParseException(new Exception("Document was not valid!"));
            }

            return document;
        } catch (MarshalException | XMLSignatureException | NodeQueryException e) {
            throw new DocumentParseException(e);
        }
    }
    
    private static class KeyValueKeySelector extends KeySelector {
        public KeySelectorResult select(KeyInfo keyInfo,
                                        KeySelector.Purpose purpose,
                                        AlgorithmMethod method,
                                        XMLCryptoContext context)
            throws KeySelectorException {
            if (keyInfo == null) {
                throw new KeySelectorException("Null KeyInfo object!");
            }
            SignatureMethod sm = (SignatureMethod) method;
            List list = keyInfo.getContent();

            for (int i = 0; i < list.size(); i++) {
                XMLStructure xmlStructure = (XMLStructure) list.get(i);
                if (xmlStructure instanceof KeyValue) {
                    PublicKey pk = null;
                    try {
                        pk = ((KeyValue)xmlStructure).getPublicKey();
                    } catch (KeyException ke) {
                        throw new KeySelectorException(ke);
                    }
                    // make sure algorithm is compatible with method
                    if (algEquals(sm.getAlgorithm(), pk.getAlgorithm())) {
                        return new SimpleKeySelectorResult(pk);
                    }
                }
            }
            throw new KeySelectorException("No KeyValue element found!");
        }

        //@@@FIXME: this should also work for key types other than DSA/RSA
        static boolean algEquals(String algURI, String algName) {
            if (algName.equalsIgnoreCase("DSA") &&
                algURI.equalsIgnoreCase(SignatureMethod.DSA_SHA1)) {
                return true;
            } else if (algName.equalsIgnoreCase("RSA") &&
                       algURI.equalsIgnoreCase(SignatureMethod.RSA_SHA1)) {
                return true;
            } else {
                return false;
            }
        }
    }

    private static class SimpleKeySelectorResult implements KeySelectorResult {
        private PublicKey pk;
        SimpleKeySelectorResult(PublicKey pk) {
            this.pk = pk;
        }

        public Key getKey() { return pk; }
    }

}
