package pl.zzpj.cryptography.web.restapi.models;

public class TextEncryptionParams {
	private String key;
	private String text;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
