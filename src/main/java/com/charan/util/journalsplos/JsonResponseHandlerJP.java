package com.charan.util.journalsplos;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.charan.util.ResponseHandler;

public class JsonResponseHandlerJP implements ResponseHandler{

	public String[] handleResponse(String res) {
//		String[] results = new String[];
		List<String> list = new LinkedList<String>();
		try {
			JSONObject jsonObj = new JSONObject(res);
			JSONObject resultsObj = jsonObj.getJSONObject("searchResults");
			JSONArray resultsArray = resultsObj.getJSONArray("docs");
			for (int i=0;i<resultsArray.length();i++) {
				String link = resultsArray.getJSONObject(i).getString("link");
//				System.out.println(link);
				list.add(link);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list.toArray(new String[list.size()]);
	}

}
