package edu.stevens.cs594.certgen;

public enum Option {
    /*
     * Command-line options:
     */
    BASE_DIR("basedir"),
    PASSWORD_FILE("passwordfile"),
    NAMES_FILE("namesfile"),
    SCRIPT_FILE("scriptfile"),
    /*
     * Arguments:
     */
    CERT_FILE("cert"),
    DNS_NAME("dns"),
    DURATION("duration"),
    CLIENT_DN("dn"),
    CLIENT_CSR_FILE("csr"),
    CLIENT_KEY_STORE("keystore"),
    CLIENT_STORE_PASSWORD("storepass"),
    CLIENT_KEY_PASSWORD("keypass");

    private String value;
    private boolean param;

    private Option(String v) {
        value = v;
        param = true;
    }

    public String value() {
        return value;
    }

    public boolean isParam() {
        return param;
    }
}
