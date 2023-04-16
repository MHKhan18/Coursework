package edu.stevens.cs594.certgen.client;

import edu.stevens.cs594.certgen.AppBase;
import edu.stevens.cs594.certgen.Command;
import edu.stevens.cs594.certgen.Option;
import edu.stevens.cs594.certgen.Params;
import edu.stevens.cs594.crypto.CAUtils;
import edu.stevens.cs594.crypto.PrivateCredential;
import edu.stevens.cs594.driver.Driver;
import edu.stevens.cs594.util.Reporter;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.logging.Logger;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;


public class App extends AppBase {

	private static final Logger logger = Logger.getLogger(App.class.getCanonicalName());

	protected void initCA(File base, File passwordFile, File namesFile) throws IOException {
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
		case GENERATE_CLIENT_ROOT:
			genClientRoot(options);
			break;
		case GENERATE_CSR:
			genClientCSR(options);
			break;
		case IMPORT_CLIENT_CERT:
			importClientCert(options);
			break;
		default:
			throw new IllegalArgumentException("Unrecognized command: " + command.name());
		}
	}

	private static final String GENERATE_CLIENT_ROOT_HELP =
			HelpMessage("  %s --%s dist-name --%s keystore \n" +
							"\t\t--%s keystore-password --%s key-password \n" +
							"\t\t--%s duration",
					Command.GENERATE_CLIENT_ROOT, Option.CLIENT_DN, Option.CLIENT_KEY_STORE,
					Option.CLIENT_STORE_PASSWORD, Option.CLIENT_KEY_PASSWORD, Option.DURATION);

	private static final String GENERATE_CSR_HELP =
			HelpMessage("  %s --%s csr-file --%s keystore \n" +
							"\t\t--%s keystore-password --%s key-password \n" +
							"\t\t[--%s dns-name]",
					Command.GENERATE_CSR, Option.CLIENT_CSR_FILE, Option.CLIENT_KEY_STORE,
					Option.CLIENT_STORE_PASSWORD, Option.CLIENT_KEY_PASSWORD, Option.DNS_NAME);

	private static final String IMPORT_CLIENT_CERT_HELP =
			HelpMessage("  %s --%s cert-file --%s keystore \n" +
							"\t\t--%s keystore-password --%s key-password",
					Command.IMPORT_CLIENT_CERT, Option.CERT_FILE, Option.CLIENT_KEY_STORE,
					Option.CLIENT_STORE_PASSWORD, Option.CLIENT_KEY_PASSWORD);

	private static final String SHOW_CERTIFICATES_HELP = HelpMessage("  %s --%s keystore \n" +
							"\t\t--%s keystore-password --%s key-password", Command.SHOW_CERTIFICATES,
					Option.CLIENT_KEY_STORE, Option.CLIENT_STORE_PASSWORD, Option.CLIENT_KEY_PASSWORD);


	protected void displayHelp() {
		say("");
		say("Commands for client:");
		say(GENERATE_CLIENT_ROOT_HELP);
		say(GENERATE_CSR_HELP);
		say(IMPORT_CLIENT_CERT_HELP);
		say(SHOW_CERTIFICATES_HELP);
		say("");
		flush();
	}
	

	/**
	 * Generate initial v1 self-signed cert for a client.
	 */
	protected void genClientRoot(Map<Option,String> options) throws Exception {
		String clientName = options.get(Option.CLIENT_DN);
		if (clientName == null) {
			reporter.error("Missing client distinguished name.");
			return;
		}
		String clientKeystoreFilename = options.get(Option.CLIENT_KEY_STORE);
		if (clientKeystoreFilename == null) {
			reporter.error("Missing client key store.");
			return;
		}
		File clientKeystoreFile = new File(clientKeystoreFilename);
		String duration = options.get(Option.DURATION);
		if (duration == null) {
			reporter.error("Must specify a duration for a client certificate.");
		}
		String clientKeystorePassword = options.get(Option.CLIENT_STORE_PASSWORD);
		if (clientKeystorePassword == null) {
			reporter.error("Missing client key store password.");
			return;
		}
		String clientKeyPassword = options.get(Option.CLIENT_KEY_PASSWORD);
		if (clientKeyPassword == null) {
			reporter.error("Missing client key password.");
			return;
		}
		
		long id = getRandomLong();
		X500Name clientDn = new X500Name(clientName);
		KeyPair keyPair = generateKeyPair();
		
		// Create self-signed v1 cert and save in client keystore
		X509Certificate cert = CAUtils.createClientRootCert(id, clientDn, keyPair, Long.parseLong(duration));
		X509Certificate[] chain = { cert };
		KeyStore clientStore = load(clientKeystoreFile, clientKeystorePassword.toCharArray(), Params.CLIENT_KEYSTORE_TYPE);
		clientStore.setKeyEntry(Params.CLIENT_CERT_ALIAS, keyPair.getPrivate(), clientKeyPassword.toCharArray(), chain);
		save(clientKeystoreFile, clientKeystorePassword.toCharArray(), clientKeyPassword, clientStore);
	}

	/**
	 * Generate client CSR signed by their protected key
	 */
	protected void genClientCSR(Map<Option,String> options) throws Exception {
		String clientKeystoreFilename = options.get(Option.CLIENT_KEY_STORE);
		if (clientKeystoreFilename == null) {
			reporter.error("Missing client key store.");
			return;
		}
		File clientKeystoreFile = new File(clientKeystoreFilename);
		String clientKeystorePassword = options.get(Option.CLIENT_STORE_PASSWORD);
		if (clientKeystorePassword == null) {
			reporter.error("Missing client key store password.");
			return;
		}
		String clientKeyPassword = options.get(Option.CLIENT_KEY_PASSWORD);
		if (clientKeyPassword == null) {
			reporter.error("Missing client key password.");
			return;
		}
		String clientCsrFile = options.get(Option.CLIENT_CSR_FILE);
		if (clientCsrFile == null) {
			reporter.error("Missing file name for client CSR.");
			return;
		}
		
		// May be null
		String clientDns = options.get(Option.DNS_NAME);
		
		KeyStore clientStore = load(clientKeystoreFile, clientKeystorePassword.toCharArray(), Params.CLIENT_KEYSTORE_TYPE);
		try {
			PKCS10CertificationRequest csr = null;

			// TODO generate a CSR signed by the client's private key
			PrivateCredential cred = getCredential(clientStore, Params.CLIENT_CERT_ALIAS, clientKeyPassword.toCharArray());
			X500Name subject = CAUtils.toX500Name(cred.getCertificate()[0].getSubjectX500Principal());
			KeyPair keyPair = new KeyPair(fromPrivateKey(cred.getPrivateKey()), cred.getPrivateKey());
			csr = CAUtils.createCSR(subject, keyPair, clientDns);
			
			extern(csr, new File(clientCsrFile));
		} catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
			throw new GeneralSecurityException("Security exception", e);
		}
	}
	
	/**
	 * Import a client cert generated by a CSR
	 */
	protected void importClientCert(Map<Option,String> options) throws Exception {
		String clientKeystoreFilename = options.get(Option.CLIENT_KEY_STORE);
		if (clientKeystoreFilename == null) {
			reporter.error("Missing client key store.");
			return;
		}
		File clientKeystoreFile = new File(clientKeystoreFilename);
		String clientKeystorePassword = options.get(Option.CLIENT_STORE_PASSWORD);
		if (clientKeystorePassword == null) {
			reporter.error("Missing client key store password.");
			return;
		}
		String clientKeyPassword = options.get(Option.CLIENT_KEY_PASSWORD);
		if (clientKeyPassword == null) {
			reporter.error("Missing client key password.");
			return;
		}
		String clientCertFile = options.get(Option.CERT_FILE);
		if (clientCertFile == null) {
			reporter.error("Missing file name for client CSR.");
			return;
		}
		
		KeyStore clientStore = load(clientKeystoreFile, clientKeystorePassword.toCharArray(), Params.CLIENT_KEYSTORE_TYPE);
		// TODO import the cert from clientCertFile and store it in the clientStore
		try {
			PrivateCredential cred = getCredential(clientStore, Params.CLIENT_CERT_ALIAS, clientKeyPassword.toCharArray());
			X509Certificate[] chain = cred.getCertificate();
			clientStore.setKeyEntry(Params.CLIENT_CERT_ALIAS, cred.getPrivateKey(), clientKeyPassword.toCharArray(), chain);
			save(clientKeystoreFile, clientKeystorePassword.toCharArray(), clientKeyPassword, clientStore);
		} catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
			throw new GeneralSecurityException("Security exception", e);
		}

	}
	
	/**
	 * Display information about all private keys.
	 */
	protected void showCerts(Map<Option, String> options) throws Exception {

		String clientKeystoreFilename = options.get(Option.CLIENT_KEY_STORE);
		if (clientKeystoreFilename == null) {
			reporter.error("Missing keystore file name.");
			return;
		}
		String clientKeystorePassword = options.get(Option.CLIENT_STORE_PASSWORD);
		if (clientKeystorePassword == null) {
			reporter.error("Missing client key store password.");
			return;
		}
		String clientKeyPassword = options.get(Option.CLIENT_KEY_PASSWORD);
		if (clientKeyPassword == null) {
			reporter.error("Missing client key password.");
			return;
		}
		File clientKeystoreFile = new File(clientKeystoreFilename);
		if (clientKeystoreFile.exists()) {
			KeyStore clientStore = load(clientKeystoreFile, clientKeystorePassword.toCharArray(), Params.CLIENT_KEYSTORE_TYPE);
			PrivateCredential clientCert = getCredential(clientStore, Params.CLIENT_CERT_ALIAS, clientKeyPassword.toCharArray());
			showCredentialInfo("Client Credential:", clientCert);
		} else {
			reporter.error("No such client keystore: "+clientKeystoreFilename);
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
