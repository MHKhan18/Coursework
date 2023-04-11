package edu.stevens.cs594.certgen.offline;

import edu.stevens.cs594.certgen.AppBase;
import edu.stevens.cs594.certgen.Command;
import edu.stevens.cs594.certgen.Option;
import edu.stevens.cs594.certgen.Params;
import edu.stevens.cs594.crypto.CAUtils;
import edu.stevens.cs594.crypto.PrivateCredential;
import edu.stevens.cs594.driver.Driver;
import edu.stevens.cs594.util.FileUtils;
import edu.stevens.cs594.util.Reporter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.RFC4519Style;


public class App extends AppBase {

	private static final Logger logger = Logger.getLogger(App.class.getCanonicalName());

	/*
	 * Passwords for keystores and truststores.
	 */
	protected char[] keystorePasswordRootCA;
	protected char[] keyPasswordRootCA;
	protected char[] keystorePasswordOnlineCA;
	protected char[] keyPasswordOnlineCA;
	protected char[] truststorePasswordServer;
	protected char[] keystorePasswordServer;
	protected char[] keyPasswordServer;

	/*
	 * Files for CA keystores and truststores
	 */
	protected File keystoreRootCAFile;
	protected File keystoreOnlineCAFile;
	protected File keystoreServerFile;
	protected File truststoreServerFile;

	/*
	 * Distinguished names for CA certificates.
	 */
	protected X500Name caRoot;
	protected X500Name caOnline;
	protected X500Name serverCert;


	@Override
	protected void initCA(File base, File passwordsFile, File namesFile) throws IOException {

		/*
		 * If not passwords file specified, it defaults to PASSWORDS_FILENAME in base directory.
		 */
		if (passwordsFile == null) {
			passwordsFile = new File(baseDir, Params.PASSWORDS_FILENAME);
		}
		if (!passwordsFile.exists()) {
			reporter.say("Missing \""+ Params.PASSWORDS_FILENAME+"\" file or --"+Option.PASSWORD_FILE.value()+" option");
			System.exit(-1);
		}
		loadPasswords(passwordsFile);

		/*
		 * If no names file specified, it defaults to NAMES_FILENAME in base directory.
		 */
		if (namesFile == null) {
			namesFile = new File(baseDir, Params.NAMES_FILENAME);
		}
		if (!namesFile.exists()) {
			reporter.say("Missing \""+ Params.NAMES_FILENAME+"\" file or --"+Option.NAMES_FILE.value()+" option");
			System.exit(-1);
		}
		loadNames(namesFile);

		initBasedir(base, namesFile);

		File offlineDir = new File(baseDir, Params.OFFLINE_DIR);
		FileUtils.ensureFolder(offlineDir);

		File backupOfflineDir = new File(baseDir, Params.BACKUP_OFFLINE_DIR);
		FileUtils.ensureFolder(backupOfflineDir);

		// Initialize the root CA keystore file name.

		keystoreRootCAFile = new File(offlineDir, Params.CA_ROOT_KEYSTORE_FILENAME);

		File onlineDir = new File(baseDir, Params.ONLINE_DIR);
		FileUtils.ensureFolder(onlineDir);

		File backupOnlineDir = new File(baseDir, Params.BACKUP_ONLINE_DIR);
		FileUtils.ensureFolder(backupOnlineDir);

		// TODO Initialize the online CA keystore file name, and server keytstore and truststore file names.


	}

	/**
	 * Load passwords for key stores, keys and truststores
	 */
	protected boolean loadPasswords(File passwordFile) {
		try {
			Properties properties = new Properties();
			Reader in = FileUtils.openInputCharFile(passwordFile);
			properties.load(in);
			in.close();

			String password = properties.getProperty(Params.CA_ROOT_KEYSTORE_PASSWORD);
			if (password == null) {
				say("No offline keystore password provided: " + Params.CA_ROOT_KEYSTORE_PASSWORD);
				return false;
			} else {
				keystorePasswordRootCA = password.toCharArray();
			}
			password = properties.getProperty(Params.CA_ROOT_KEY_PASSWORD);
			if (password == null) {
				say("No offline key password provided: " + Params.CA_ROOT_KEY_PASSWORD);
				return false;
			} else {
				keyPasswordRootCA = password.toCharArray();
			}
			password = properties.getProperty(Params.CA_ONLINE_KEYSTORE_PASSWORD);
			if (password == null) {
				say("No online keystore password provided: " + Params.CA_ONLINE_KEYSTORE_PASSWORD);
				return false;
			} else {
				keystorePasswordOnlineCA = password.toCharArray();
			}
			password = properties.getProperty(Params.CA_ONLINE_KEY_PASSWORD);
			if (password == null) {
				say("No online key password provided: " + Params.CA_ONLINE_KEY_PASSWORD);
				return false;
			} else {
				keyPasswordOnlineCA = password.toCharArray();
			}
			password = properties.getProperty(Params.SERVER_TRUSTSTORE_PASSWORD);
			if (password == null) {
				say("No app server truststore password provided: " + Params.SERVER_TRUSTSTORE_PASSWORD);
				return false;
			} else {
				truststorePasswordServer = password.toCharArray();
			}
			password = properties.getProperty(Params.SERVER_KEYSTORE_PASSWORD);
			if (password == null) {
				say("No app server keystore password provided: " + Params.SERVER_KEYSTORE_PASSWORD);
				return false;
			} else {
				keystorePasswordServer = password.toCharArray();
			}
			password = properties.getProperty(Params.SERVER_KEY_PASSWORD);
			if (password == null) {
				say("No key password provided: " + Params.SERVER_KEY_PASSWORD);
				return false;
			} else {
				keyPasswordServer = password.toCharArray();
			}
		} catch (Exception e) {
			reporter.error("Exception while reading passwords from " + passwordFile.getName(), e);
			return false;
		}
		return true;
	}

	/**
	 * Load distinguished names for X509 certs
	 */
	protected boolean loadNames(File namesFile) {
		try {
			Properties properties = new Properties();
			Reader in = FileUtils.openInputCharFile(namesFile);
			properties.load(in);
			in.close();
			String name = properties.getProperty(Params.CA_ROOT);
			if (name == null) {
				say("Missing distinguished name: " + Params.CA_ROOT);
				return false;
			} else {
				caRoot = new X500Name(RFC4519Style.INSTANCE, name);
			}
			name = properties.getProperty(Params.SERVER_CERT);
			if (name == null) {
				say("Missing distinguished name: " + Params.SERVER_CERT);
				return false;
			} else {
				serverCert = new X500Name(RFC4519Style.INSTANCE, name);
			}
			name = properties.getProperty(Params.CA_ONLINE);
			if (name == null) {
				say("Missing distinguished name: " + Params.CA_ONLINE);
				return false;
			} else {
				// https://stackoverflow.com/a/38234094
				caOnline = new X500Name(RFC4519Style.INSTANCE, name);
			}
		} catch (Exception e) {
			reporter.error("Exception while reading names from " + namesFile.getName(), e);
			return false;
		}
		return true;
	}

	/**
	 * Called by the REPL driver to execute a command line from the REPL.
	 */
	@Override
	public void execute(Command command, Map<Option, String> options) throws Exception {
		if (command == null) {
			displayHelp();
			return;
		}
		switch (command) {
			case HELP:
				displayHelp();
				break;
			case SHOW_CERTIFICATES:
				showCerts(options);
				break;
			case GENERATE_CA_ROOT:
				genCaRoot(options);
				break;
			case EXPORT_CA_ROOT_CERT:
				exportCaRootCert(options);
				break;
			case GENERATE_SERVER_SSL_CERT:
				genServerCert(options);
				break;
			case GENERATE_CA_ONLINE_CERT:
				genOnlineCaCert(options);
				break;
			case EXPORT_CA_ONLINE_CERT:
				exportOnlineCaCert(options);
				break;
			default:
				throw new IllegalArgumentException("Unrecognized command: " + command.name());
		}
	}

	private static final String GENERATE_CA_ROOT_HELP = HelpMessage("  %s [--%s duration]",
			Command.GENERATE_CA_ROOT, Option.DURATION);

	private static final String EXPORT_CA_ROOT_CERT_HELP = HelpMessage("  %s --%s cert-file",
			Command.EXPORT_CA_ROOT_CERT, Option.CERT_FILE);

	private static final String GENERATE_SERVER_CERT_HELP = HelpMessage("  %s --%s dns-name [--%s duration]",
			Command.GENERATE_SERVER_SSL_CERT, Option.DNS_NAME, Option.DURATION);

	private static final String GENERATE_CA_ONLINE_CERT_HELP = HelpMessage("  %s [--%s duration]",
			Command.EXPORT_CA_ONLINE_CERT, Option.DURATION);

	private static final String EXPORT_CA_ONLINE_CERT_HELP = HelpMessage("  %s --%s cert-file",
			Command.EXPORT_CA_ONLINE_CERT, Option.CERT_FILE);

	private static final String SHOW_CERTIFICATES_HELP = HelpMessage("  %s", Command.SHOW_CERTIFICATES);

	protected void displayHelp() {
		say("");
		say("Offline commands:");
		say(GENERATE_CA_ROOT_HELP);
		say(EXPORT_CA_ROOT_CERT_HELP);
		say(GENERATE_SERVER_CERT_HELP);
		say(GENERATE_CA_ONLINE_CERT_HELP);
		say(EXPORT_CA_ONLINE_CERT_HELP);
		say(SHOW_CERTIFICATES_HELP);
		say("");
		flush();
	}
	
	/**
	 * Generate root CA for server CA for server SSL, stored in the offline keystore.
	 */
	protected void genCaRoot(Map<Option,String> options) throws Exception {
		if (keystoreRootCAFile.exists()) {
			reporter.error("Root CA keystore already exists!");
			return;
		}
		
		long duration = getDuration(options, Params.CA_ROOT_DURATION);
		
		KeyStore keystoreRoot = load(keystoreRootCAFile, keystorePasswordRootCA, Params.CA_ROOT_KEYSTORE_TYPE);
		
		KeyPair kp = generateKeyPair();
		
		long certId = getRandom().nextLong();
		
		X509Certificate cert = null;
		
		// TODO generate root CA cert (see CAUtils)


		Certificate[] chain = new Certificate[]{cert};
		
		keystoreRoot.setKeyEntry(Params.CA_ROOT_ALIAS, kp.getPrivate(), keyPasswordRootCA, chain);
		
		updateKeystore(keystoreRootCAFile, keystoreRoot, Params.CA_ROOT_KEYSTORE_TYPE, keystorePasswordRootCA);
	}
	

	/**
	 * Export CA root cert as a PEM file.
	 */
	protected void exportCaRootCert(Map<Option,String> options) throws Exception {
		File certFile = getCertFile(options);
		
		// TODO write CA root cert to a PEM file: load the root keystore, get the root CA private credential, 
		// convert to string (see externCertificate), then write that to the certFile

	}

	/**
	 * Generate key pair for server CA for server SSL, stored in the app server keystore.
	 */
	protected void genServerCert(Map<Option,String> options) throws Exception {
		String serverDNS = options.get(Option.DNS_NAME);
		if (serverDNS == null) {
			reporter.error("Missing server DNS.");
			return;
		}
		long duration = getDuration(options, Params.SERVER_CERT_DURATION);

		KeyStore keystoreRoot = load(keystoreRootCAFile, keystorePasswordRootCA, Params.CA_ROOT_KEYSTORE_TYPE);

		PrivateCredential root = getCredential(keystoreRoot, Params.CA_ROOT_ALIAS, keyPasswordRootCA);

		KeyPair kp = generateKeyPair();

		long certId = getRandom().nextLong();

		X509Certificate cert = null;

		// TODO create server cert and cert chain


		Certificate[] chain = new Certificate[]{cert, root.getCertificate()[0]};
		/*
		 * TODO Save credential in the server keystore (use load and updateKeystore)
		 */

		/*
		 * TODO Save certificate in the server truststore.
		 */
	}

	/**
	 * Generate private key for online CA for client certs, stored in the online CA keystore.
	 */
	protected void genOnlineCaCert(Map<Option,String> options) throws Exception {
		long duration = getDuration(options, Params.CA_ONLINE_CERT_DURATION);

		KeyStore keystoreOffline = load(keystoreRootCAFile, keystorePasswordRootCA, Params.CA_ROOT_KEYSTORE_TYPE);

		PrivateCredential root = getCredential(keystoreOffline, Params.CA_ROOT_ALIAS, keyPasswordRootCA);

		KeyPair kp = generateKeyPair();

		long certId = getRandom().nextLong();

		// TODO create online CA cert


		/*
		 * TODO Save the credentials in the online keystore (use load and updateKeystore)
		 */


		/*
		 * TODO Save the intermediate certificate in the server truststore (for client authentication).
		 */
	}

	/**
	 * Export online CA cert as a PEM file.
	 */
	protected void exportOnlineCaCert(Map<Option,String> options) throws Exception {
		File certFile = getCertFile(options);
		PrivateCredential cred = null;

		// TODO get online CA cert from online CA keystore and extract credential


		writeString(certFile, externCertificate(cred.getCertificate()[0]));
	}



	/**
	 * Display information about all private keys.
	 */
	protected void showCerts(Map<Option, String> options) throws Exception {
		say("Showing credentials that are updated offline.");
		say("");
		if (keystoreRootCAFile.exists()) {
			KeyStore keystoreRootCA = load(keystoreRootCAFile, keystorePasswordRootCA, Params.CA_ROOT_KEYSTORE_TYPE);
			PrivateCredential root = getCredential(keystoreRootCA, Params.CA_ROOT_ALIAS, keyPasswordRootCA);
			showCredentialInfo("CA Root Credential:", root);
		}

		if (keystoreOnlineCAFile.exists()) {
			KeyStore keystoreOnlineCA = load(keystoreOnlineCAFile, keystorePasswordOnlineCA, Params.CA_ONLINE_KEYSTORE_TYPE);
			PrivateCredential onlineCa = getCredential(keystoreOnlineCA, Params.CA_ONLINE_CERT_ALIAS, keyPasswordOnlineCA);
			showCredentialInfo("CA Online Credential:", onlineCa);
		}

		if (keystoreServerFile.exists()) {
			KeyStore keystoreServer = load(keystoreServerFile, keystorePasswordServer, Params.SERVER_KEYSTORE_TYPE);
			PrivateCredential serverSSL = getCredential(keystoreServer, Params.SERVER_CERT_ALIAS, keyPasswordServer);
			showCredentialInfo("Server Credential:", serverSSL);
		}

	}


	public static void main(String[] args) {

		Reporter reporter = Reporter.createReporter();

		App app = new App(reporter);

		Driver<Command,Option> driver = new Driver<Command,Option>(reporter, app);

		try {
			app.execute(driver, args);
		} catch (Exception e) {
			reporter.error(e.getMessage(), logger, e);
		}

	}

	public App(Reporter reporter) {
		super(reporter);
	}

}
