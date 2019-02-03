package hu.acme.cryptodom.dom;

public class ValidationException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 3705693135802109625L;

    public ValidationException(Exception e) {
        super(e);
    }

    public ValidationException(String s) {
        super(s);
    }

}
