package com.lem.project.client;

import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.lem.project.shared.*;
//import com.google.appengine.api.datastore.Text;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class LEMSandbox implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	private void getInterventiList(String txt) {
		
		try {
			greetingService.getInterList(txt,
					new AsyncCallback<List<InterventoDTO>>() {
						public void onFailure(Throwable caught) {
							// Show the RPC error message to the user
							System.out.println("Error here ... RPC");
						}

						public void onSuccess(List<InterventoDTO> result) {
							
							System.out.println("All Good ... result size -> " + result.size());
							// First Record contains stats !!!
							InterventoDTO intDtoStats = result.get(0);
							String stats = intDtoStats.getDescrizione();
							result.remove(0);
							
							int r = 0;
							if (result != null) r = result.size();
							
							Grid g = new Grid(r+1, 4);
							
							//Set header
							g.setText(0, 0, "Data");
							g.getCellFormatter().setWidth(0, 0, "70px");
							g.setText(0, 1, "Operatore");
							g.setText(0, 2, "Macchina");
							g.setText(0, 3, "Descrizione Intervento");
							g.getCellFormatter().setWidth(0, 3, "550px");
							g.getRowFormatter().setStyleName(0, "headerRowStyle");
							
							if (r>0) {
																
								 for (int row = 0; row < result.size(); ++row) {
									 
									 InterventoDTO idto = result.get(row);
									 
									 g.setText(row+1, 0, idto.getData());
									 g.setText(row+1, 1, idto.getOperatore());
									 g.setText(row+1, 2, idto.getMacchina());
									 g.setText(row+1, 3, idto.getDescrizione());
							     }
							}
							
							g.setStyleName("tableStyle");
							RootPanel.get("gridContainer").clear();
							RootPanel.get("gridContainer").add(g);
							
							//if (r > 99) {
								
								Label lb = new Label();
								lb.setText(stats);
								RootPanel.get("errorLabelContainer").clear();
								RootPanel.get("errorLabelContainer").add(lb);

							//}
							
						}
					});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This is the entry point method.
	 */
	@SuppressWarnings("null")
	public void onModuleLoad() {
		
		final Button sendButton = new Button("Search");
		final Button populateButton = new Button("Populate");
		final TextBox nameField = new TextBox();
		nameField.setText("Search Box");
		final Label errorLabel = new Label();

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");
		populateButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("nameFieldContainer").add(nameField);
		RootPanel.get("sendButtonContainer").add(sendButton);
		RootPanel.get("errorLabelContainer").add(errorLabel);
		//RootPanel.get("populateButtonContainer").add(populateButton);

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});

		// Create a handler for the populate button 
		class PopHandler implements ClickHandler {

			@Override
			public void onClick(ClickEvent event) {
				sendPopReqToServer();
			}
			
			private void sendPopReqToServer() {
				//populateButton.setEnabled(false);
				greetingService.popRequest(
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								dialogBox.setText("Remote Procedure Call - Failure");
								serverResponseLabel.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(SERVER_ERROR);
								dialogBox.center();
								closeButton.setFocus(true);
							}

							public void onSuccess(String result) {
								dialogBox.setText("Remote Procedure Call");
								serverResponseLabel.removeStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(result);
								dialogBox.center();
								closeButton.setFocus(true);
							}
						});	
			}
		}
		
		// Add a handler to send populate request to the server
		PopHandler phandler = new PopHandler();
		populateButton.addClickHandler(phandler);
		
		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void sendNameToServer() {
				// First, we validate the input.
				errorLabel.setText("");
				String textToServer = nameField.getText();

				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				getInterventiList(textToServer);
				sendButton.setEnabled(true);
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
		
		// Make a grid....
		//initDB();
	    getInterventiList("");
	
	}
}
