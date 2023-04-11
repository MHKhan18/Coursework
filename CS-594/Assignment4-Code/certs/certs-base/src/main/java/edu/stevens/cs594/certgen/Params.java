package edu.stevens.cs594.certgen;

import java.io.File;
import java.security.spec.RSAKeyGenParameterSpec;

public class Params {

    /*
     * Properties in passwords files.
     */
    public static final String CA_ROOT_KEYSTORE_PASSWORD = "caroot.keystore.password";
    public static final String CA_ROOT_KEY_PASSWORD = "caroot.key.password";
    public static final String CA_ONLINE_KEYSTORE_PASSWORD = "ca.keystore.password";
    public static final String CA_ONLINE_KEY_PASSWORD = "ca.key.password";
    public static final String SERVER_TRUSTSTORE_PASSWORD = "server.truststore.password";
    public static final String SERVER_KEYSTORE_PASSWORD = "server.keystore.password";
    public static final String SERVER_KEY_PASSWORD = "server.key.password";

    /*
     * Properties in distinguished names files.
     */
    public static final String CA_ROOT = "ca.root";
    public static final String CA_ONLINE = "ca.online";
    public static final String SERVER_CERT = "server.cert";

    /*
     * Keystore aliases
     */
    // CA root (in the offline keystore)
    public static final String CA_ROOT_ALIAS = "ca-root";

    // For current server cert (in the server keystore)
    public static final String SERVER_CERT_ALIAS = "s1as";
    // Online CA for app client certs (in the online CA keystore).
    public static final String CA_ONLINE_CERT_ALIAS = "ca-online";

    // Client cert provided to app (alias in client keystore).
    public static final String CLIENT_CERT_ALIAS = "client-cert";

    /*
     * Default durations.
     */
    public static final int ONE_YEAR = 365 * 24;
    public static final long SERVER_CERT_DURATION = ONE_YEAR;
    public static final long CLIENT_CERT_DURATION = ONE_YEAR;
    public static final long CA_ONLINE_CERT_DURATION = 5 * ONE_YEAR;
    public static final long CA_ROOT_DURATION = 10 * ONE_YEAR;

    /*
     * Keystore types.
     */
    // For root CA key.
    public static final String CA_ROOT_KEYSTORE_TYPE = "PKCS12";

    // Online keystores and truststores
    public static final String CA_ONLINE_KEYSTORE_TYPE = "PKCS12";
    public static final String SERVER_KEYSTORE_TYPE = "PKCS12";
    public static final String SERVER_TRUSTSTORE_TYPE = "PKCS12";
    public static final String CLIENT_KEYSTORE_TYPE = "PKCS12";

    /*
     * The certificate manager makes some assumptions about the organization of the space where keystores and
     * truststores are managed.
     *
     * Directories:
     */
    public static final String OFFLINE_DIR = "certs-offline";
    public static final String ONLINE_DIR = "certs-online";
    public static final String BACKUP_DIR = "certs-backup";
    public static final String BACKUP_ONLINE_DIR = BACKUP_DIR + File.separatorChar + ONLINE_DIR;
    public static final String BACKUP_OFFLINE_DIR = BACKUP_DIR + File.separatorChar + OFFLINE_DIR;
    /**
     * Files:
     */
    public static final String CA_ROOT_KEYSTORE_FILENAME = "caroot.p12";
    public static final String CA_ONLINE_KEYSTORE_FILENAME = "ca.p12";
    public static final String SERVER_KEYSTORE_FILENAME = "keystore.p12";
    public static final String SERVER_TRUSTSTORE_FILENAME = "cacerts.p12";
    public static final String PASSWORDS_FILENAME = "passwords.properties";
    public static final String NAMES_FILENAME = "names.properties";

    /*
     * Some crypto parameters.
     */
    public static final int ASYMMETRIC_KEY_LENGTH = 2048;
    // Recommended for X509 & default in BC

}
