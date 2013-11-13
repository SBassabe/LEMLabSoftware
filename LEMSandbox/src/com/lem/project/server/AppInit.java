package com.lem.project.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Text;
import com.lem.project.server.domain.Intervento;
import com.lem.project.server.domain.SearchItem;
import com.lem.project.data.Loader;

public class AppInit {

  /* Intervento
   *  - data
   *  - operatore
   *  - macchina
   *  - descrizione
   * */
	
  private static final String[] sampleOperatori = new String[] { "Jean",
      "Billy", "Jacques", "Zoe", "Bella", "Napoleon", "Dona", "Daniel", "Grassino", "SBassabe"};

  private static final String[] sampleMacchina = new String[] { "Fiat", "Ford",
      "Opel", "VW", "Ferrari", "JohnDeer", "Honda", "Mercedes", "Fiat", "Lotus" };

  private static final String[] sampleDesc = new String[] {
      "cambio spinterogeno", "tagliando annuale ruota marcia", "coppa olio modello Daniel",
      "bulone perso in ruota anteriore", "bullone perso sotto coppa tergecristallo", "autista scemo chiamato billy",
      "gnocca incrostata in baule chiamata zoe", "coppa liquido para brezza", "modella in mini-gona che ruota", "zoe zebra e' zoccola oliata" };

  public String populateDataStoreForTest() {
  
	System.out.println("populateDataStoreForTest() ...  started  ...");   
	String ret = "";
	int numInter=0;
	int numSItems=0;
	
	PersistenceManager pm1 = PMF.get().getPersistenceManager();
	numInter = isEntitySizeGtZero(pm1); 
	if (numInter > 0) return "Inserts not performed '" + numInter + "' found in DataStore";
    
    System.out.println("populateDataStoreForTest: Entity empty, loading stuff now ...");
    
    try {
    	
      Loader lfo = new Loader();
      //lfo.loadArray2001();
      //lfo.loadArray2011();
      Intervento inter = null;
      
      Iterator<Intervento> it = lfo.getList().iterator();
      int i=0;
      while (it.hasNext()) {
    	  
    	  inter = it.next();
    	  System.out.println("making persistent item -> " + ++i);
    	  pm1.makePersistent(inter);
    	  numSItems = numSItems+addSearchItems(inter, pm1);
    	  ++numInter;
      }
      
      ret = "Populate Stats -> : Intervento='"+numInter+"', SearchItems='"+numSItems+"'";
      
    } // end try
    catch (Exception e) {
      e.printStackTrace();
      ret = "Error in populate ... see logs.";
    } finally {
      pm1.close();
      System.out.println("populateDataStoreForTest() ... finished ...");
    }
    return ret;
  }
  
  public String populateDataStoreForTestOld() {
	  
		System.out.println("populateDataStoreForTest() ...  started  ...");   
		String ret = "";
		int numRec=0;
		
		PersistenceManager pm1 = PMF.get().getPersistenceManager();
		numRec = isEntitySizeGtZero(pm1); 
		if (numRec > 0) return "Inserts not performed '" + numRec + "' found in DataStore";
	    
	    System.out.println("populateDataStoreForTest: Entity empty, loading stuff now ...");
	    Intervento inter = null;
	    
	    try {
	 
	      //List<Intervento> lst = new ArrayList<Intervento>();	 
	      for (int i = 0; i < sampleOperatori.length; ++i) {
	        
	    	List<String> srchLst = new ArrayList<String>();  
	    	inter = new Intervento();
	        inter.setBasicInfo("OCT-0"+i, sampleOperatori[i], sampleMacchina[i], sampleDesc[i]);
	         
//	        srchLst.add(inter.getData().toLowerCase());
//	        srchLst.add(inter.getDescrizione().toLowerCase());
//	        srchLst.add(inter.getMacchina().toLowerCase());
//	        srchLst.add(inter.getOperatore().toLowerCase());
//	        inter.setSearch(srchLst);
	        //lst.add(inter);
	        pm1.makePersistent(inter);
	        
	        addSearchItems(inter, pm1);
	        numRec++;
	        System.out.println("populateDataStoreForTest: adding ..." + i );
	      }
	      ret = "DataStore populated with '" + numRec + "' records.";
	      //pm1.makePersistentAll(lst);

	    } // end try
	    catch (Exception e) {
	      e.printStackTrace();
	      ret = "Error in populate ... see logs.";
	    } finally {
	      pm1.close();
	      System.out.println("populateDataStoreForTest() ... finished ...");
	    }
	    return ret;
	  }
  
  private int addSearchItems(Intervento inter, PersistenceManager pm) {
	
	  //System.out.println("into addSearchItems ...");
	  String[] tmp;
	  String delim = " ";
	  int j=0;
	  Set<String> wrdSet = new HashSet<String>();
	  // Data
	  if (inter.getData() != null) {
		  tmp = inter.getData().split(delim);
		  for(int i=0; i < tmp.length ; i++) {
			  wrdSet.add(tmp[i].toLowerCase());
		  }
	  }
	  
	  // Operatore
	  if (inter.getOperatore() != null) {
		  tmp = inter.getOperatore().split(delim);
		  for(int i=0; i < tmp.length ; i++) {
			  wrdSet.add(tmp[i].toLowerCase());
		  }
	  }
	  
	  // Macchina
	  if (inter.getMacchina() != null) {
		  tmp = inter.getMacchina().split(delim);
		  for(int i=0; i < tmp.length ; i++) {
			  wrdSet.add(tmp[i].toLowerCase());
		  }
	  }
	  
	  // Descrizione
	  if (inter.getDescrizione() != null) {
		  tmp = inter.getDescrizione().split(delim);
		  for(int i=0; i < tmp.length ; i++) {
			  wrdSet.add(tmp[i].toLowerCase());
		  }
	  }

	  Iterator<String> itStr = wrdSet.iterator();
	  while (itStr.hasNext()) {
		  SearchItem si = new SearchItem();
		  si.setWord(itStr.next());
		  si.setParentId(inter.getId());
		  System.out.println("addSearchItems ... persisting object " + ++j);
		  pm.makePersistent(si);
	  }
	  return j;
  }
  
  private int isEntitySizeGtZero(PersistenceManager pm) {
	  
	  System.out.println("isEntitySizeGtZero : started");
	  int size = 0;
	  Query query = null;
	  
	  try {
		  
		query = pm.newQuery(Intervento.class);
		@SuppressWarnings("unchecked")
		List<Intervento> rs = (List<Intervento>) query.execute();
	    
		if (rs != null) size = rs.size();
		
	  } finally {
		System.out.println("isEntitySizeGtZero : size is = " + size);
		query.closeAll();
	  }
	  return size;
  }
}
