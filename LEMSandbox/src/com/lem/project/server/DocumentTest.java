package com.lem.project.server;

//Document
import java.util.Date;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
//Index
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.StatusCode;

public class DocumentTest {
	
	private Document doc;
	
	public DocumentTest(){
		this.createDocument();
	}
	
	private void createDocument() {
		
		System.out.println("Into createDocument");
		//User currentUser = UserServiceFactory.getUserService().getCurrentUser();
		String myDocId = "PA6-5000";
		doc = Document.newBuilder()
		    .setId(myDocId) // Setting the document identifer is optional. If omitted, the search service will create an identifier.
		    .addField(Field.newBuilder().setName("content").setText("the rain in spain"))
		    .addField(Field.newBuilder().setName("email")
		    .setText("currentUser.getEmail()"))
		    .addField(Field.newBuilder().setName("domain")
		    .setAtom("currentUser.getAuthDomain()"))
		    .addField(Field.newBuilder().setName("published").setDate(new Date()))
		    .build();
	}
	
	public void putIntoIndex(String indexName) {
		
		System.out.println("Into putIntoIndex()");
		IndexSpec indexSpec = IndexSpec.newBuilder().setName(indexName).build(); 
		Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
		    
		try {
		        index.put(doc);
		} catch (PutException e) {
		  if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode())) {
		    // retry putting the document
		  }
		}
	}
	
	public static void main(String[] args) {
		System.out.println("Into Main");
		DocumentTest dt = new DocumentTest();
		dt.putIntoIndex("IndexName");
	}
}
