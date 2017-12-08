package space.glome.http.schema.domain;

public class PemCertificate extends Certificate {

	/**
	 * Certificate in PEM format
	 */
	private String cert;

	/**
	 * Private key in PEM format (pkcs8)
	 */
	private String key;

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

}
