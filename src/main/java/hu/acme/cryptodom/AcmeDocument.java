package hu.acme.cryptodom;

public class AcmeDocument {

    private final String name;
    private final byte[] data;

    public AcmeDocument(String name, byte[] data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public byte[] getData() {
        return data;
    }

}
