package com.lem.project.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.lem.project.data.Loader;
import com.lem.project.server.AppInit;

public class Load extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		System.out.println("IntoServlet");
		resp.setContentType("text/plain");
	    resp.getWriter().println("Hello Load ....");
	    
	    if (req.getParameter("start") == null) {
	    	System.out.println("param start null");
	    	resp.getWriter().println("parameter start Not passed ... ");
	    	Queue queue = QueueFactory.getDefaultQueue();
	    	TaskOptions topt = TaskOptions.Builder.withUrl("/lemsandbox/load");
	    	topt.param("start", "load");
	    	queue.add(topt);
	    } else {
	    	System.out.println("param start NOT null");
	    	resp.getWriter().println("parameter start passed ... starting load");
	    
	    	AppInit ai = new AppInit();
	    	System.out.println(ai.populateDataStoreForTest());
	    }
		//super.doGet(req, resp);
	}

}
