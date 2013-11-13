package com.lem.project.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class InterventoDTO implements Serializable {

  private String id;
  private String data;
  private String operatore;
  private String macchina;
  //private List<String> descrizione;
  private String descrizione;
  //private List<String> search;

  public InterventoDTO() {}

  public InterventoDTO(String data, String operatore, String macchina, String descrizione) {
    this();
    setBasicInfo(data, operatore, macchina, descrizione); 
  }

  public void setBasicInfo(String data, String operatore, String macchina, String descrizione) {
    this.data = data;
    this.operatore = operatore;
    this.macchina = macchina;
    this.descrizione = descrizione;
  }

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

  public String getDescrizione() {
	return descrizione;
  }

  public void setDescrizione(String descrizione) {
	 this.descrizione = descrizione;
  }

}
