package hu.acme.cryptodom.dom;

public class DocumentParseException extends Exception{

    private static final long serialVersionUID = -2517257953494008587L;

    public DocumentParseException(Exception e) {
        super(e);
    }
    
    public DocumentParseException(String s) {
        super(s);
    }

}
