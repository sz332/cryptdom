package hu.acme.cryptodom.dom;

import java.io.InputStream;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class DomDocument {

    private final DocumentTemplate template;

    public DomDocument(DocumentTemplate template) {
        this.template = template;
    }

    public DomDocument(InputStream stream) {
        this(new DocumentTemplate(stream));
    }

    public DomDocument(Document document) {
        this(new DocumentTemplate(document));
    }

    public Node asNode(String expression) throws NodeQueryException {

        try {
            XPathExpression exp = createExpression(expression);
            return (Node) exp.evaluate(template.asDocument(), XPathConstants.NODE);
        } catch (XPathExpressionException | DocumentParseException e) {
            throw new NodeQueryException(e);
        }
    }

    public String asString(String expression) throws NodeQueryException {

        try {
            XPathExpression exp = createExpression(expression);
            return (String) exp.evaluate(template.asDocument(), XPathConstants.STRING);
        } catch (XPathExpressionException | DocumentParseException e) {
            throw new NodeQueryException(e);
        }
    }

    private XPathExpression createExpression(String expression) throws XPathExpressionException, DocumentParseException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        xPath.setNamespaceContext(new NamespaceResolver(template.asDocument()));
        XPathExpression exp = xPath.compile(expression);
        return exp;
    }

}
