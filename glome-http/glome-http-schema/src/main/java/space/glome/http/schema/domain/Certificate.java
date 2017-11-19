package space.glome.http.schema.domain;

public class Certificate {
	
	/**
	 * Certificate in PEM format
	 */
	private String cert;
	
	/**
	 * Private key in PEM format
	 */
	private String key;
	
	private String passphrase;
	
	public String getCert() {
		return cert;
	}

	public void setCert(String cert) {
		this.cert = cert;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getPassphrase() {
		return passphrase;
	}

	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}
}
