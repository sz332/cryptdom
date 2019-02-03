package hu.acme.cryptodom.dom;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;

public class SignKeyStore {

    private final KeyStore ks;
    private final String keyName;
    private final String keyStorePassword;

    public SignKeyStore(KeyStore ks, String keyName, String keyStorePassword) {
        this.ks = ks;
        this.keyName = keyName;
        this.keyStorePassword = keyStorePassword;
    }

    public XmlSignInformation asInformation(XMLSignatureFactory fac) throws KeyStoreException,
            NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableEntryException {
        KeyStore.PrivateKeyEntry keyEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(keyName,
                new KeyStore.PasswordProtection(keyStorePassword.toCharArray()));

        X509Certificate cert = (X509Certificate) keyEntry.getCertificate();

        // Create the KeyInfo containing the X509Data.
        KeyInfoFactory kif = fac.getKeyInfoFactory();
        List x509Content = new ArrayList();
        x509Content.add(cert.getSubjectX500Principal().getName());
        x509Content.add(cert);
        X509Data xd = kif.newX509Data(x509Content);
        KeyInfo ki = kif.newKeyInfo(Collections.singletonList(xd));

        return new XmlSignInformation(keyEntry, ki);
    }
  
}
