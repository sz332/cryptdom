package hu.acme.cryptodom.dom;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class DomDocument {

    private final Document document;

    public DomDocument(Document document) {
        this.document = document;
    }

    public Node asNode(String expression) throws NodeQueryException {

        try {
            XPathExpression exp = createExpression(expression);
            return (Node) exp.evaluate(document, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            throw new NodeQueryException(e);
        }
    }

    public String asString(String expression) throws NodeQueryException {

        try {
            XPathExpression exp = createExpression(expression);
            return (String) exp.evaluate(document, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            throw new NodeQueryException(e);
        }
    }

    private XPathExpression createExpression(String expression) throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        xPath.setNamespaceContext(new NamespaceResolver(document));
        XPathExpression exp = xPath.compile(expression);
        return exp;
    }

}
