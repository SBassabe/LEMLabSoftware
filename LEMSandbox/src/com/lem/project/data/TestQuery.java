package com.lem.project.data;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.urlfetch.FetchOptions;
import com.lem.project.server.domain.Intervento;


public class TestQuery {
	
	public static void main (String[] args) {
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		//Filter heightMinFilter = new FilterPredicate("height", FilterOperator.GREATER_THAN_OR_EQUAL, minHeight);
		//Filter heightMaxFilter = new FilterPredicate("height", FilterOperator.LESS_THAN_OR_EQUAL, maxHeight);

		//Use CompositeFilter to combine multiple filters
		//Filter heightRangeFilter = CompositeFilterOperator.and(heightMinFilter, heightMaxFilter);

		// Use class Query to assemble a query
		//Query q = new Query("Person").setFilter(heightRangeFilter);
		Query q = new Query("Intervento"); 
		
		// Use PreparedQuery interface to retrieve results
		PreparedQuery pq = datastore.prepare(q);
	
		System.out.println("count-> " + pq.countEntities());
		
		/*for (Entity result : pq.asIterable()) {
		  result.
		  String firstName = (String) result.getProperty("firstName");
		  String lastName = (String) result.getProperty("lastName");
		  Long height = (Long) result.getProperty("height");
	
		  System.out.println(firstName + " " + lastName + ", " + height + " inches tall");
		}*/
	}

}
