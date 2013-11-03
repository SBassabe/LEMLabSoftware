package com.lem.project.server.domain;

import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.listener.LoadCallback;
import com.lem.project.shared.InterventoDTO;


@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Intervento implements LoadCallback {

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
	  private String descrizione;
	  @Persistent
	  private List<String> search;
	
	  public Intervento() {}
	
	  public Intervento(InterventoDTO interventoDTO) {
	    this();
	    this.setBasicInfo(interventoDTO.getData(), interventoDTO.getOperatore(), interventoDTO.getMacchina(), interventoDTO.getDescrizione());
	  }
	
	  public Intervento(InterventoDTO interventoDTO, PersistenceManager pm) {
		  this();
	      this.setBasicInfo(interventoDTO.getData(), 
	    		            interventoDTO.getOperatore(), 
	    		            interventoDTO.getMacchina(), 
	    		            interventoDTO.getDescrizione());
	      pm.makePersistent(this);
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
	  
	  public void jdoPostLoad() {}

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
		
		public void setId(String id) {
			this.id = id;
		}
		
		public List<String> getSearch() {
			return search;
		}
		
		public void setSearch(List<String> search) {
			this.search = search;
		}
  
}
