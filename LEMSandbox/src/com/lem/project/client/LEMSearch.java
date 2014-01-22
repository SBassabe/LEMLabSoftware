package com.lem.project.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.lem.project.shared.InterventoDTO;

public class LEMSearch implements EntryPoint {
	
	interface MyUiBinder extends UiBinder<DockLayoutPanel, LEMSearch> {}
	
	private static LEMSearch singelton;
	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	private RootLayoutPanel root;
	 
	@UiField TextBox dateFromField;
	@UiField TextBox dateToField;
	@UiField TextBox operField;
	@UiField TextBox macchinaField;
	@UiField TextBox descrizioneField;
	@UiField Grid gridObj;
	@UiField Label trovatiField;
	@UiField Label mostratiField;

	@UiField Button searchBtn;
	
	@UiHandler("searchBtn")
	void handleClick(ClickEvent e) {
		
		if (!areDatesOk()) {
			Window.alert("The dates are wrong. Please use format yyyy-mm-dd");  
		} else {
			
			String searchString = buildQuery();
			if ( !searchString.isEmpty()) { 
				getInterventiList(searchString);
			} else {
				Window.alert("You must enter some text for me to search something !!!!");
			};
		}
	}
	
	private String buildQuery() {
	
		String ret = "";
		String andSep = " AND ";
		String[] strArr = new String[5];
		for (String s : strArr) s="";
		
		StringBuffer sb = new StringBuffer();
		
		if (dateFromField.getText().trim().length() > 0) {
			strArr[1] = "data >= " + dateFromField.getText().toLowerCase();
		}
		if (dateToField.getText().trim().length() > 0) {
			strArr[2] = "data <= " + dateToField.getText().toLowerCase();
		}
		if (operField.getText().trim().length() > 0) {
			strArr[3] = "operatore = " + operField.getText().toLowerCase();
		}
		if (macchinaField.getText().trim().length() > 0) {
			strArr[4] = "macchina = " + macchinaField.getText().toLowerCase();
		}
		if (descrizioneField.getText().trim().length() > 0) {
			strArr[5] = "descrizione = " + descrizioneField.getText().toLowerCase();
		}
		
		for (String s : strArr) {
			if (s != null && s.length() > 0) sb.append(s + andSep);
		}
		
		if (sb.length() > 1) ret = sb.substring(0, sb.length() - andSep.length());
		
		return ret;
	}
	
	private boolean areDatesOk() {
		
		// Both fields empty - OK
		if (dateFromField.getText().isEmpty() && dateToField.getText().isEmpty()) return true;
		
		// Only one field compiled - KO
		if (!dateFromField.getText().isEmpty() && dateToField.getText().isEmpty() ||
		    !dateToField.getText().isEmpty() && dateFromField.getText().isEmpty()) return false;	
			
	    // At least one of the compiled fields does not have the correct format KO		
		if (!dateFromField.getText().matches("^\\d{4}-\\d{2}-\\d{2}$") || 
			!dateToField.getText().matches("^\\d{4}-\\d{2}-\\d{2}$")) {
			return false;
		} else {
			return true;
		}
	}
	
	public static LEMSearch get() {
		return singelton;
	}
	   
	@Override
	public void onModuleLoad() {
		
		System.out.println("into onModuleLoad");
		
		singelton = this;
		DockLayoutPanel outer = uiBinder.createAndBindUi(this);
		root = RootLayoutPanel.get();
		root.add(outer);
		
		trovatiField.setText("0");
		mostratiField.setText("0");
	}
	
	//-----------------------------------------------------------------------
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
							System.out.println("stats -> " + stats); 
							trovatiField.setText(stats.split("@")[0]);
							mostratiField.setText(stats.split("@")[1]);
							
							result.remove(0);
							
							int r = 0;
							if (result != null) r = result.size();
							gridObj.resize(r+1, 4);
							
							if (r>0) {
																
								 for (int row = 0; row < result.size(); ++row) {
									 
									 InterventoDTO idto = result.get(row);
									 
									 gridObj.setText(row+1, 0, idto.getData());
									 gridObj.setText(row+1, 1, idto.getOperatore());
									 gridObj.setText(row+1, 2, idto.getMacchina());
									 gridObj.setText(row+1, 3, idto.getDescrizione());
							     }
							}
						}
					});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//-----------------------------------------------------------------------	
	
}