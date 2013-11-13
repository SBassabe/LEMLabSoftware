package com.lem.project.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

//import com.lem.project.server.domain.Intervento;
import com.lem.project.client.GreetingService;
import com.lem.project.shared.FieldVerifier;
import com.lem.project.shared.InterventoDTO;
import com.lem.project.server.PMF;
import com.lem.project.server.domain.Intervento;
import com.lem.project.server.domain.SearchItem;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

	public GreetingServiceImpl() {	
	}
	
	public String greetServer(String input) throws IllegalArgumentException {
		
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException(
					"Name must be at least 4 characters long");
		}

		String userAgent = getThreadLocalRequest().getHeader("User-Agent");
		GetStats.queryAndPrintStats();
		
		return "Hello, " + input + "!<br><br>I am running " + input
				+ ".<br><br>It looks like you are using:<br>" + userAgent;
		
	}


	@SuppressWarnings("unchecked")
	public List<InterventoDTO> getInterList(String txt) {
		
		System.out.println("getInterList() -> start ...");
		List<InterventoDTO> lst = new ArrayList<InterventoDTO>();
		if (txt != null && "Stats:".compareTo(txt) == 0) {
			
			showStatOnLog(txt);
			return lst;
		}
		
		// Instatiate Persistance Manager
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = null;
	
		try {
			
			// Prepare intervento list for FrontEnd
			List<Intervento> rs = new ArrayList<Intervento>();
			query = pm.newQuery(Intervento.class);
			query.setRange(0, 100);
			
			// Search input given
			if (txt.length() > 1) {
				
				List<SearchItem> rs1 = new ArrayList<SearchItem>();
				Query query1 = pm.newQuery(SearchItem.class);
				
				txt = txt.toLowerCase();
				String start = txt;
				String end=txt + "\ufffd";
				
			    query1.setFilter("word >= start && word <= end");
			    query1.declareParameters("java.lang.String start, java.lang.String end");
				rs1 = (List<SearchItem>) query1.execute(start, end);
				
				System.out.println("rs1.size() -> " + rs1.size());
				List<String> rs3 = new ArrayList<String>();
				for (SearchItem si : rs1) {
					System.out.println("Adding parentId -> " + si.getParentId());
					rs3.add(si.getParentId());
				}
				
				if (!rs3.isEmpty()) {
						
					query.setFilter("p.contains(id)");
					query.declareParameters("java.util.List p");
					rs = (List<Intervento>) query.execute(rs3);
				}
				
			// No search text in SearchBox	
			} else {
				
				rs = (List<Intervento>) query.execute();

			}
			
			int i = 0;
			System.out.println("getInterList() -> rs.size " + rs.size());
			if (rs.iterator().hasNext()) {
				for (Intervento inter : rs) {
					System.out.println("inter.getDescrizione() -> " + inter.getDescrizione());
					InterventoDTO intDto = new InterventoDTO(inter.getData(),inter.getOperatore(),inter.getMacchina(), inter.getDescrizione());
					lst.add(intDto);
					System.out.println("getInterList() -> retreving ..." + ++i);
				}
			}
			
		} finally {
			System.out.println("getInterList() -> intoFinally ...");
			query.closeAll();
			pm.close();
		}
		
		return lst;
		
	}

	private void showStatOnLog(String txt) {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		// SBassabe -> this does not work argggggasdfasdf!!!
		/*
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		//Entity globalStat = datastore.prepare(new com.google.appengine.api.datastore.Query("__Stat_Total__")).asSingleEntity();
		Entity globalStat = datastore.get
		Long totalBytes = (Long) globalStat.getProperty("bytes");
		Long totalEntities = (Long) globalStat.getProperty("count");
		
		System.out.println("totalBytes ->" + totalBytes);
		*/
		
		List<String> lst = null;
		
		Query query = null;
		query = pm.newQuery("SELECT word FROM " + com.lem.project.server.domain.SearchItem.class.getName());
		
        lst = (List<String>)query.execute();
        System.out.println("lst.size() -> " + lst.size());
        
        Set<String> st = new HashSet<String>();
        st.addAll(lst);
        
        System.out.println("st.size() -> " + st.size());
		
		pm.close();
		
	}

	@Override
	public String popRequest() throws IllegalArgumentException {
		AppInit ai = new AppInit();
		return ai.populateDataStoreForTest();
	}
}
