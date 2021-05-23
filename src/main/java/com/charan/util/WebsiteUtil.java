package com.charan.util;

import java.net.URL;
import java.util.Scanner;

import org.jsoup.Jsoup;

public class WebsiteUtil {

	public static String readWebpage1(String urlString) {
		try {
			
			URL url = new URL(urlString);
		      //Retrieving the contents of the specified page
		      Scanner sc = new Scanner(url.openStream());
		      //Instantiating the StringBuffer class to hold the result
		      StringBuffer sb = new StringBuffer();
		      while(sc.hasNext()) {
		         sb.append(sc.next());
		         //System.out.println(sc.next());
		      }
		      //Retrieving the String from the String Buffer object
		      sc.close();
		      return sb.toString();
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
	      return null;
	}
	
	public static String readWebpage(String urlString) {
		try {
			String html = Jsoup.connect(urlString).get().html();
		    return html;
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
	      return null;
	}
}
