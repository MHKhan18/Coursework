package edu.stevens.cs594.certgen;

/**
 * Command line arguments (options and option arguments)
 */
public enum Command {

    /*
     * Admin commands
     */
    HELP("help"),
    SHOW_CERTIFICATES("showcerts"),
    /*
     * Commands for offline keystore
     */
    GENERATE_CA_ROOT("gencaroot"),
    EXPORT_CA_ROOT_CERT("exportcaroot"),
    /*
     * Commands for online server keystores
     */
    GENERATE_SERVER_SSL_CERT("genservercert"),
    GENERATE_CA_ONLINE_CERT("genonlinecacert"),
    EXPORT_CA_ONLINE_CERT("exportonlinecacert"),
    /*
     * Commands for online client (will be moved to the server)
     */
    GENERATE_CLIENT_CERT("genclientcert"),
    GENERATE_CLIENT_ROOT("genclientroot"),
    GENERATE_CSR("genclientcsr"),
    IMPORT_CLIENT_CERT("importclientcert");

    private String value;

    private Command(String v) {
        value = v;
    }

    public String value() {
        return value;
    }
}
