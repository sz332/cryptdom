package hu.acme.cryptodom.dom;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;

import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;

public class KeyRepository {

    private final KeyStore ks;
    private final String   alias;
    private final String   keyStorePassword;

    public KeyRepository(KeyStore ks, String alias, String keyStorePassword) {
        this.ks = ks;
        this.alias = alias;
        this.keyStorePassword = keyStorePassword;
    }

    public XmlSignInformation information(XMLSignatureFactory fac) throws KeyStoreException,
            NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException,
            UnrecoverableEntryException {

        PrivateKeyEntry keyEntry =
                (PrivateKeyEntry) ks.getEntry(alias, new PasswordProtection(keyStorePassword.toCharArray()));

        X509Certificate cert = (X509Certificate) keyEntry.getCertificate();

        // Create the KeyInfo containing the X509Data.
        KeyInfoFactory kif = fac.getKeyInfoFactory();
        X509Data xd = kif.newX509Data(Arrays.asList(cert.getSubjectX500Principal().getName(), cert));
        KeyInfo ki = kif.newKeyInfo(Collections.singletonList(xd));

        return new XmlSignInformation(keyEntry, ki);
    }

}
