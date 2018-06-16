package com.charan.util;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonResponseHandler implements ResponseHandler{

	public String[] handleResponse(String res) {
		try {
			JSONObject jsonObj = new JSONObject(res);
//			jsonObj.
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
