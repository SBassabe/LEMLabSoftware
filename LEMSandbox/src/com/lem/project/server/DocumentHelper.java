package com.lem.project.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
//Index
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.StatusCode;

public class DocumentHelper {
	
	private Document doc;
	private List<Document> docList =  new ArrayList<Document>();
	private String year;
	private List<String> lst;
	private String indexName = "IndexName2";
	private int min;
	private int max;
	
	public DocumentHelper(){
		System.out.println("Into DocumentHelper constructor...");
	}
	
	public void setYear(String year) {
		this.year = year;
	}
	
	public void setList(List<String> inLst) {
		this.lst = inLst;
	}
	
	public void setMin(int min) {
		this.min = min;
	}
	
	public void setMax(int max) {
		this.max = max;
	}
	
	public int getMin() {
		return this.min;
	}
	
	public int getMax() {
		 return this.max;
	}
	
	/*
	private void loadYear() {
		
		System.out.println("Into loadYear");
		lst = new ArrayList<String>();
		
		if ("2011".compareTo(year) == 0) {
			Load2010x2012.loadData2011(lst);
		}
	}
	*/
	
	public void processDoc() {
		
		System.out.println("Into processDoc()");
		try {
	
			Iterator<String> it = lst.iterator();
			String newStr[] = null;
			String descArr[] = null;
			int descLineSize = 1000;
			int rowNum = 0;
			
			while (it.hasNext()) {
				 
				try {
					
					newStr = it.next().split(";");
					
					// Treatment of description > 500 char
					if (newStr[3].length() >= descLineSize) {
						
						int lsize =  newStr[3].length();
						int arrSize = lsize/descLineSize;
						descArr = new String[arrSize+1];
						
						for (int i = 0; i <= arrSize; i++) {
							
							int s = i*descLineSize;
							try {
								
								descArr[i] = newStr[3].substring(s, s+(descLineSize-1));
							} catch (IndexOutOfBoundsException  e) {
								//System.out.println("into exception idx");
								descArr[i] = newStr[3].substring(s);
							}
						}
						
						descArr[0] = descArr[0]+" ... ";
						
					} else {
						descArr = new String[1];
						descArr[0] = newStr[3];
					}
					
					createDocument(newStr[0], newStr[1], newStr[2], descArr[0], year+""+min+""+max+""+ ++rowNum);
				    
				} catch (Exception e) {
					System.out.println("into exception 1");
					e.printStackTrace();
				}
			}
			
		} catch (Exception e) {
			System.out.println("into exception 2");
			e.printStackTrace();
		}
		
	}
	
	private void createDocument(String data, String operatore, String macchina, String descrizione, String myDocId) {
		
		System.out.println("Into createDocument -> " + myDocId);
		//User currentUser = UserServiceFactory.getUserService().getCurrentUser();
		//String myDocId = "PA6-5000";
		doc = Document.newBuilder()
		    .setId(myDocId) // Setting the document identifer is optional. If omitted, the search service will create an identifier.
		    .addField(Field.newBuilder().setName("data").setText(data))
		    .addField(Field.newBuilder().setName("operatore").setText(operatore))
		    .addField(Field.newBuilder().setName("macchina").setText(macchina))
		    .addField(Field.newBuilder().setName("descrizione").setText(descrizione))
		    .build();
		
		docList.add(doc);

	}
	
	public void putIntoIndex() {
		
		System.out.println("Into putIntoIndex()");
		IndexSpec indexSpec = IndexSpec.newBuilder().setName(indexName).build(); 
		Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
		    
		try {
		        index.put(docList);
		} catch (PutException e) {
		  if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode())) {
		    // retry putting the document
		  }
		}
	}
	
}
