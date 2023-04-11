package edu.stevens.cs594.certgen.online;

import edu.stevens.cs594.certgen.AppBase;
import edu.stevens.cs594.certgen.Command;
import edu.stevens.cs594.certgen.Option;
import edu.stevens.cs594.certgen.Params;
import edu.stevens.cs594.crypto.CAUtils;
import edu.stevens.cs594.crypto.PrivateCredential;
import edu.stevens.cs594.driver.Driver;
import edu.stevens.cs594.util.FileUtils;
import edu.stevens.cs594.util.Reporter;
import edu.stevens.cs594.util.StringUtils;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.RFC4519Style;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;


public class App extends AppBase {

	private static final Logger logger = Logger.getLogger(App.class.getCanonicalName());

	/*
	 * Passwords for CA keystores and truststores.
	 */
	protected char[] keystorePasswordOnlineCA;
	protected char[] keyPasswordOnlineCA;
	protected char[] keystorePasswordServer;
	protected char[] keyPasswordServer;

	/*
	 * Files for online CA keystores and server keystore, that will be online.
	 * These keystores are initialized by the offline cert manager.
	 */

	protected File keystoreOnlineCAFile;
	protected File keystoreServerFile;

	/*
	 * Distinguished names for CA certificates.
	 */
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

		File onlineDir = new File(baseDir, Params.ONLINE_DIR);
		FileUtils.ensureFolder(onlineDir);

		File backupOnlineDir = new File(baseDir, Params.BACKUP_ONLINE_DIR);
		FileUtils.ensureFolder(backupOnlineDir);

		// TODO Initialize the online CA keystore file and server keystore file (see offline App).


	}

	/*
	 * Load passwords for key stores, keys and truststores
	 */
	protected boolean loadPasswords(File passwordFile) {
		try {
			Properties properties = new Properties();
			Reader in = FileUtils.openInputCharFile(passwordFile);
			properties.load(in);
			in.close();

			String password = properties.getProperty(Params.CA_ONLINE_KEYSTORE_PASSWORD);
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

	/*
	 * Load distinguished names for X509 certs
	 */
	protected boolean loadNames(File namesFile) {
		try {
			Properties properties = new Properties();
			Reader in = FileUtils.openInputCharFile(namesFile);
			properties.load(in);
			in.close();

			String name = properties.getProperty(Params.SERVER_CERT);
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
		case GENERATE_CLIENT_CERT:
			genClientCert(options);
			break;
		default:
			throw new IllegalArgumentException("Unrecognized command: " + command.name());
		}
	}

	private static final String GENERATE_CLIENT_CERT_HELP = HelpMessage("  %s --%s csr-file --%s cert-file \n" +
					"\t\t[--%s dns-name] [--%s duration]",
			Command.GENERATE_CLIENT_CERT, Option.CLIENT_CSR_FILE, Option.CERT_FILE, Option.DNS_NAME, Option.DURATION);

	private static final String SHOW_CERTIFICATES_HELP = HelpMessage("  %s", Command.SHOW_CERTIFICATES);

	public void displayHelp() {
		say("");
		say("Online Commands:");
		say(GENERATE_CLIENT_CERT_HELP);
		say(SHOW_CERTIFICATES_HELP);
		say("");
		flush();
	}
	
	/**
	 * Input a CSR from a client (received as a PEM file for the cert manager).
	 */
	public static PKCS10CertificationRequest internCSR(String pem) throws GeneralSecurityException {
		try {
			ByteArrayInputStream pemStream = new ByteArrayInputStream(pem.getBytes(StringUtils.CHARSET));
			Reader pemReader = new BufferedReader(new InputStreamReader(pemStream));
			PEMParser pemParser = new PEMParser(pemReader);

			Object parsedObj = pemParser.readObject();
			pemParser.close();

			if (parsedObj instanceof PKCS10CertificationRequest) {
				return (PKCS10CertificationRequest) parsedObj;
			} else {
				throw new GeneralSecurityException("Expected certification request: " + parsedObj);
			}
		} catch (IOException e) {
			throw new GeneralSecurityException("Security exception", e);
		}
	}
	
	public static PKCS10CertificationRequest internCSR(File pemFile) throws GeneralSecurityException {
		try {
			return internCSR(readString(pemFile));
		} catch (IOException e) {
			throw new GeneralSecurityException("Security exception", e);
		}
	}
	
	/**
	 * Generate a client cert from a CSR
	 */
	protected void genClientCert(Map<Option,String> options) throws Exception {
		String clientCsrFile = options.get(Option.CLIENT_CSR_FILE);
		if (clientCsrFile == null) {
			reporter.error("Missing file name for client CSR.");
			return;
		}
		String certFile = options.get(Option.CERT_FILE);
		if (certFile == null) {
			reporter.error("Missing file name for client certificate.");
			return;
		}

		String clientDns = options.get(Option.DNS_NAME);  // May be null
		
		long duration = getDuration(options, Params.CLIENT_CERT_DURATION);

		long certId = getRandom().nextLong();
		
		PKCS10CertificationRequest request = internCSR(new File(clientCsrFile));
		
		KeyStore keystoreApp = load(keystoreOnlineCAFile, keystorePasswordOnlineCA, Params.CA_ONLINE_KEYSTORE_TYPE);

		PrivateCredential ca = getCredential(keystoreApp, Params.CA_ONLINE_CERT_ALIAS, keyPasswordOnlineCA);
	
		X509Certificate cert = null;
		
		// TODO generate client cert from CSR using online CA key, write to certFile

		
	}
	
	/**
	 * Display information about all credentials.
	 */
	protected void showCerts(Map<Option, String> options) throws Exception {
		say("Showing credentials that are used online.");
		say("");

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
