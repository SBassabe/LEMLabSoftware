package com.lem.project.server.domain;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Tabellone {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String id;
	
	@Persistent
	private String data;
	@Persistent
	private String operatore;
	@Persistent
	private String macchina;
	@Persistent
	private String commento;
	@Persistent
	private String descrizione;
	@Persistent
	private String codice;
	 
	public Tabellone() {}
	public String getId() {
		return id;
	}
		
	public void setId(String id) {
		this.id = id;
	}
		
	public String getData() {
		return data;
	}
		
	public void setData(String data) {
		this.data = data;
	}
	
	public String getOperatore() {
		return operatore;
	}
		
	public void setOperatore(String operatore) {
		this.operatore = operatore;
	}
		
	public String getMacchina() {
		return macchina;
	}
		
	public void setMacchina(String macchina) {
		this.macchina = macchina;
	}
	
	public String getCommento() {
		return commento;
	}
		
	public void setCommento(String commento) {
		this.commento = commento;
	}
		
	public String getDescrizione() {
		return descrizione;
	}
		
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
		
	public String getCodice() {
		return codice;
	}
		
	public void setCodice(String codice) {
		this.codice = codice;
	}
}
