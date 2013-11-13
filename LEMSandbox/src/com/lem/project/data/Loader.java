package com.lem.project.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.appengine.api.datastore.Text;
import com.lem.project.server.domain.Intervento;

public class Loader {

	private static List<String> lStr =  new ArrayList<String>();
	public Loader() {}
	
	public List<Intervento> getList() {
		
		List<Intervento> ret =  new ArrayList<Intervento>();
		//Load2001x2004.loadData2001(lStr);                     //10
		System.out.println("lStr.size() -> " + lStr.size()); 
		//Load2010x2012.loadData2010(lStr);                     //2376    
		System.out.println("lStr.size() -> " + lStr.size());
		Load2010x2012.loadData2011(lStr);                     //845
		System.out.println("lStr.size() -> " + lStr.size());
		//Load2010x2012.loadData2012(lStr);                     //1126
		System.out.println("lStr.size() -> " + lStr.size());
		
		try {
			
			Iterator<String> it = lStr.iterator();
			String newStr[] = null;
			String describe = null;
			String descArr[] = null;
			int descLineSize = 450;
			
			while (it.hasNext()) {
				 
				try {
					
					newStr = it.next().split(";");
					Intervento inter = new Intervento();
					
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
					
					inter.setBasicInfo(newStr[0], newStr[1], newStr[2], descArr[0]);
				    ret.add(inter);
				    
				} catch (Exception e) {
					System.out.println("into exception 1");
					e.printStackTrace();
				}
			}
			
		} catch (Exception e) {
			System.out.println("into exception 2");
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public static void main(String[] args) {
		
		Loader obj = new Loader();
/*	    System.out.println("lStr.size() -> " + lStr.size());
		obj.loadArray2001();
		obj.getList();
		System.out.println("lStr.size() -> " + lStr.size());
		obj.loadArray2011();*/
		List<Intervento> intList = obj.getList();
		System.out.println("lStr.size() -> " + lStr.size());
		for (Intervento inter : intList) {
			String str = inter.getDescrizione();
			System.out.println("desc length -> " + str.length() + " , str -> " + str);
			
		}
		
	}
}
