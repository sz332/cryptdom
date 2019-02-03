package hu.acme.cryptodom.dom;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class ValidDocument {

    private final DocumentTemplate template;

    public ValidDocument(DocumentTemplate template) {
        this.template = template;
    }

    public Document asDocument() throws DocumentParseException {

        try {
            Document document = template.asDocument();

            XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

            NodeList nl = document.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
            
            if (nl.getLength() == 0) {
                throw new ValidationException("Cannot find Signature element");
            }

            // Create a DOMValidateContext and specify a KeySelector
            // and document context.
            DOMValidateContext valContext = new DOMValidateContext(new X509KeySelector(), nl.item(0));

            // Unmarshal the XMLSignature.
            XMLSignature signature = fac.unmarshalXMLSignature(valContext);

            if (!signature.validate(valContext)) {
                throw new DocumentParseException(new Exception("Document was not valid!"));
            }

            return document;
        } catch (ValidationException | MarshalException | XMLSignatureException e) {
            throw new DocumentParseException(e);
        }
    }

}
