package hu.acme.cryptodom;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import sun.security.tools.keytool.CertAndKeyGen;
import sun.security.x509.X500Name;

public class InMemoryKeyStore {

    public static final String KEY_TYPE_RSA = "RSA";
    public static final String SIG_ALG_SHA_RSA = "SHA1WithRSA";
    public static final int KEY_SIZE = 1024;
    public static final long CERT_VALIDITY = 365 * 24 * 3600L;
    public static final String ALIAS_PRIVATE = "prite";
    public static final String ALIAS_CERT = "cert";

    private final String password;
    private final String keyName;

    public InMemoryKeyStore(String keyName, String password) {
        this.keyName = keyName;
        this.password = password;
    }

    public KeyStore asKeyStore() {

        try {
            CertAndKeyGen keyGen = new CertAndKeyGen(KEY_TYPE_RSA, SIG_ALG_SHA_RSA);
            keyGen.generate(KEY_SIZE);

            KeyStore ks = emptyStore();
            if (ks == null) {
                return null;
            }
            
            X509Certificate certificate = keyGen.getSelfCertificate(new X500Name("cn=acme"), CERT_VALIDITY);
            ks.setCertificateEntry(ALIAS_CERT, certificate);
            ks.setKeyEntry(keyName, keyGen.getPrivateKey(), password.toCharArray(), new Certificate[] { certificate });
            
            return ks;

        } catch (KeyStoreException | NoSuchAlgorithmException | NoSuchProviderException | CertificateException | SignatureException
                | InvalidKeyException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private KeyStore emptyStore() {
        try {
            KeyStore ks = KeyStore.getInstance("JKS");

            // Loading creates the store, can't do anything with it until it's loaded
            ks.load(null, password.toCharArray());
            return ks;
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
