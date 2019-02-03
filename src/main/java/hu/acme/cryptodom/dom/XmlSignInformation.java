package hu.acme.cryptodom.dom;

import java.security.KeyStore.PrivateKeyEntry;

import javax.xml.crypto.dsig.keyinfo.KeyInfo;

public class XmlSignInformation {

    PrivateKeyEntry keyEntry;
    KeyInfo keyInfo;

    public XmlSignInformation(PrivateKeyEntry keyEntry, KeyInfo keyInfo) {
        this.keyEntry = keyEntry;
        this.keyInfo = keyInfo;
    }

    public PrivateKeyEntry keyEntry() {
        return keyEntry;
    }

    public KeyInfo keyInfo() {
        return keyInfo;
    }

}
