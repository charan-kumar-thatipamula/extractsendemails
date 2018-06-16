package com.charan.httprequest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendRequest {
	private final String USER_AGENT = "Mozilla/5.0";
	public static void main(String[] args) {
		SendRequest http = new SendRequest();
		String url = "http://journals.plos.org/plosone/dynamicSearch?resultsPerPage=60&q=author_affiliate%3A%22Dermatology+%22&sortOrder=DATE_NEWEST_FIRST&page=1";
//		System.out.println("Testing 1 - Send Http GET request");
		try {
			http.sendGet(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		System.out.println("\nTesting 2 - Send Http POST request");
//		http.sendPost();

	}
	public String sendGet(String url) throws Exception {

//		String url = ;
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
//		System.out.println("\nSending 'GET' request to URL : " + url);
//		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine + "\n");
		}
		in.close();

		//print result
//		System.out.println(response.toString());
		return response.toString();
	}
}
