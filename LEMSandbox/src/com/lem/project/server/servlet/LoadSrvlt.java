package com.lem.project.server.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.GetRequest;
import com.google.appengine.api.search.GetResponse;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.lem.project.data.Load2010x2012;
import com.lem.project.data.Load2010x2012static;
import com.lem.project.server.AppInit;
import com.lem.project.server.DocumentHelper;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchException;

public class LoadSrvlt extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		System.out.println("IntoServlet");
		resp.setContentType("text/plain");
	    resp.getWriter().println("Hello Load ....");
		String loadDoc = req.getParameter("loadDoc");
		String loadEntity = req.getParameter("loadEntity");
		String clearIndex = req.getParameter("clearIndex");
		String queryStuff = req.getParameter("queryStuff");
		
		// Load SearchAPI documents
		if (loadDoc != null) {			
			
			if ("queue".compareTo(loadDoc) == 0) {
				
				Queue queue = QueueFactory.getDefaultQueue();
				int listSize = Load2010x2012static.lst2011.size();
				int bucketSize = 100;
				
				//just do 10
				//listSize = 10;
				int max;
				
				for (int i=1; i < listSize;) {
					
			    	TaskOptions topt = TaskOptions.Builder.withUrl("/lemsandbox/load");
			    	topt.param("loadDoc", "notnull");
			    	topt.param("year","2011");
			    	topt.param("min",i+"");
			    	max=i+bucketSize;
			    	max=max>listSize?listSize:max;
			    	topt.param("max",max+"");
			    	System.out.println("adding to queue: min->"+ i +", max->" + max);
			    	queue.add(topt);
			    	i=max;
			    	
				}
			
			} else {
				
				String min = req.getParameter("min");
				String max = req.getParameter("max");
				
				DocumentHelper dh = new DocumentHelper();
				dh.setYear("2011");
				dh.setMin(new Integer(min));
				dh.setMax(new Integer(max));
				dh.setList(Load2010x2012static.lst2011.subList(dh.getMin(), dh.getMax()));
				dh.processDoc();
				dh.putIntoIndex();
				
			}
			
		// Load EntityKinds	
		} else if (loadEntity != null) {
			
			if ("queue".compareTo(loadEntity) == 0) {
				
				Queue queue = QueueFactory.getDefaultQueue();
		    	TaskOptions topt = TaskOptions.Builder.withUrl("/lemsandbox/load");
		    	topt.param("loadEntity", "notnull");
		    	queue.add(topt);
				
			} else {
				
				AppInit ai = new AppInit();
		    	System.out.println(ai.populateDataStoreForTest());
		    	
			}
			
		} else if (clearIndex != null) {
			
			if ("queue".compareTo(clearIndex) == 0) {
				
				System.out.println("Into clearIndex=queue");
				/*
				IndexSpec indexSpec = IndexSpec.newBuilder().setName("IndexName2").build(); 
				Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
				GetRequest gr = GetRequest.newBuilder().setReturningIdsOnly(true).setLimit(1000).build();
	
				Queue queue = QueueFactory.getDefaultQueue();
		    	TaskOptions topt = TaskOptions.Builder.withUrl("/lemsandbox/load");
				
				int size = index.getRange(gr).getResults().size();
				System.out.println("size -> " + size);
				
				if (size > 1) {
					int buckets = 1;
					if (size/100 > 1) {
						buckets = buckets+(size/100);
					} 
					
					System.out.println("buckets -> " + buckets);
					for (int i=0; i < buckets; i++) {
						System.out.println("add to queue");
				    	topt.param("clearIndex", "notnull");
				    	queue.add(topt);
					}
				} else {
					System.out.println("size = 0, do nothing!!");
				}
				*/
				Queue queue = QueueFactory.getDefaultQueue();
		    	TaskOptions topt = TaskOptions.Builder.withUrl("/lemsandbox/load");
		    	topt.param("clearIndex", "notnull");
		    	queue.add(topt);
				
			} else {
				
				System.out.println("Into clearIndex()");
				/*IndexSpec indexSpec = IndexSpec.newBuilder().setName("IndexName2").build(); 
				Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
				GetRequest gr = GetRequest.newBuilder().setReturningIdsOnly(true).build();
				Iterator<Document> idIt = index.getRange(gr).iterator();
				int i = 0;
				while (idIt.hasNext()) {
					System.out.println("iteration -> " + ++i);
					index.delete(idIt.next().getId());
				}
				*/
				try {
					IndexSpec indexSpec = IndexSpec.newBuilder().setName("IndexName2").build(); 
					Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
					while (true) {
						List<String> docIds = new ArrayList<String>();
						GetRequest request = GetRequest.newBuilder().setReturningIdsOnly(true).build();
						GetResponse<Document> respnc = index.getRange(request);
						if (respnc.getResults().isEmpty()) {
							break;
						}
						for (Document doc : respnc) {
							System.out.println("adding DocId -> " + doc.getId());
							docIds.add(doc.getId());
						}
						index.delete(docIds);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
		} else if (queryStuff != null) {
			
			try {
			    
				//queryStuff = "macchina > ~TIMSON";
				System.out.println("queryStuff -> " + queryStuff);
				String queryString = queryStuff;
				IndexSpec indexSpec = IndexSpec.newBuilder().setName("IndexName2").build(); 
				Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
				
				index.search(queryString);
			    Results<ScoredDocument> results = index.search(queryString);

			    resp.getWriter().println("items found -> " + results.getNumberFound());
			    resp.getWriter().println("items returned -> " + results.getNumberReturned());
			    
			    // Iterate over the documents in the results
			    for (ScoredDocument document : results) {

			    	resp.getWriter().println("getId() -> " + document.getId() +  ",  macchina: " + document.getOnlyField("macchina").getText());
			        // handle results
			    }
			    
			} catch (SearchException e) {
			   e.printStackTrace();
			}
			
		} else {
			
		  resp.getWriter().println("usage: /lemsandbox/load?loadDoc=queue");
		  resp.getWriter().println("usage: /lemsandbox/load?loadEntity=queue");
		  resp.getWriter().println("usage: /lemsandbox/load?clearIndex=queue");
		  resp.getWriter().println("usage: /lemsandbox/load?queryStuff=<string>");
		  
		}
	}
}
