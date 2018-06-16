package com.charan.factory;

import com.charan.util.ResponseHandler;
import com.charan.util.journalsplos.JsonResponseHandlerJP;

public class ResponseHandlerFactory {
    public ResponseHandler getResponseHandler(String url) {
        if (url.indexOf("journals.plos.org") != -1) {
            return new JsonResponseHandlerJP();
        }
        return null;
    }
}