package com.lem.project.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.lem.project.shared.*;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback) throws IllegalArgumentException;
	void getInterList(String txt, AsyncCallback<List<InterventoDTO>> callback) throws Exception;
	void popRequest(AsyncCallback<String> asyncCallback) throws IllegalArgumentException;
}
