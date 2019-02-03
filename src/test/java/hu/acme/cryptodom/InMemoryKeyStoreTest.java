package hu.acme.cryptodom;

import java.security.KeyStore;
import java.security.KeyStore.Entry;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;

import org.junit.Assert;
import org.junit.Test;

public class InMemoryKeyStoreTest {
    
    @Test
    public void testCertificateIsPresentInStore() throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableEntryException{

        InMemoryKeyStore store = new InMemoryKeyStore("myAlias", "12345");
        
        KeyStore keyStore = store.asKeyStore();
        Assert.assertNotNull(keyStore);
        
        Assert.assertTrue(keyStore.containsAlias("myAlias"));
        
        Entry keyEntry = keyStore.getEntry("myAlias", new PasswordProtection("12345".toCharArray()));
        Assert.assertNotNull(keyEntry);
    }

}
