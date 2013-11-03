package com.lem.project.server;

import com.google.appengine.api.datastore.DatastoreAttributes;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;

public class GetStats {
	
	public GetStats() {}
	
	public static void queryAndPrintStats() {
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Entity globalStat = datastore.prepare(new Query("__Stat_Total__")).asSingleEntity();
		DatastoreAttributes da = datastore.getDatastoreAttributes();
		System.out.println("DatastoreType.name() " + da.getDatastoreType().name());
		
		
		Long totalBytes = (Long) globalStat.getProperty("bytes");
		Long totalEntities = (Long) globalStat.getProperty("count");
		
		System.out.println("totalBytes : " + totalBytes);
		System.out.println("totalEntities : " + totalEntities);
		
	}

}
