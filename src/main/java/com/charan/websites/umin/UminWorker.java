package com.charan.websites.umin;

import com.charan.httprequest.SendRequest;

import java.util.List;

public class UminWorker implements Runnable {
    List<String> journalUrls;
    String url;
    UminWorker(List<String> journalUrls, String url) {
        this.journalUrls = journalUrls;
        this.url = url;
    }
    @Override
    public void run() {
        processSinglePage();
    }

    public void processSinglePage() {
        String urlPrefix = "https://upload.umin.ac.jp/cgi-open-bin/ctr_e/";
        String keyword = "./ctr_view.cgi?recptno=";
        try {
//            Document doc = Jsoup.connect(url).get();
            SendRequest sReq = new SendRequest();
            String response = sReq.sendGet(url);
//            System.out.println(response);
            int sInd = response.indexOf(keyword);
//            if (sInd == -1)
//                return false;
            String tResponse = response;
            while (sInd != -1) {
                tResponse = tResponse.substring(sInd + keyword.length(), tResponse.length());
                int eInd = tResponse.indexOf("\">");
                String id = tResponse.substring(0, eInd);
                String link = keyword + id;
                link = link.replace("./", urlPrefix);
                synchronized (journalUrls) {
                    journalUrls.add(link);
                }
                sInd = tResponse.indexOf(keyword);
//                System.out.println(link);
            }
        } catch (Exception e) {
            System.out.println("Exception for URL: " + url + "ignoring; " + e.getMessage());
        }
//        return true;
    }
}