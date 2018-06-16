package com.charan.websites.sciencedirect;

import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;

public class ExtractJournalMetadataSD {
    public String getJournalMetadata(String journalUrl) {
        // "https://www.sciencedirect.com/science/article/pii/S0889854518300093"
        String journalMetadata = "";
        try {
            Document doc = Jsoup.connect(journalUrl).get();
//            System.out.println(doc.title());
//            journalMetadata = journalMetadata.concat(doc.title() + ",");
            Elements newsHeadlines = doc.select("script[data-iso-key]");
            for (Element headline : newsHeadlines) {
//            headline.text();
//            System.out.println(headline.attr("title") + " : " +headline.absUrl("href"));
//            System.out.println(headline.child(0).text());
                List<Node> childNodes = headline.childNodes();
                for (Node node : childNodes) {
//                    System.out.println(((DataNode) node).getWholeData());
                    String data = ((DataNode) node).getWholeData();
                    // jsonObject -> "author-group"
                    JSONObject jsonObject= JsonPath.read(data, "$.authors.content[0]");
//                    System.out.println();
                    JSONArray jsonArray = (JSONArray) jsonObject.get("$$");
//                    String name = "";
//                    String email = "";
                    // extract AuthorName and AuthorEmail looping on all the authors
                    LinkedList<String> list = getAuthorNameEmail(jsonArray);
                    list.addLast(doc.title());
                    journalMetadata = generateRow(list);
//                    journalMetadata = doc.title();
//                    if (journalMetadata.indexOf(" - ScienceDirect") != -1) {
//                        journalMetadata = journalMetadata.substring(0, journalMetadata.indexOf(" - ScienceDirect"));
//                    }
////                    journalMetadata = journalMetadata;
//                    journalMetadata = journalMetadata + "," + sNameEmail;
//                JSONObject jsonObject = new JSONObject(data);
//                x.authors.content[0].$$
                }
                //            log("%s\n\t%s",
//                    headline.attr("title"), headline.absUrl("href"));
            }

//            System.out.println(journalMetadata);
        } catch (Exception e) {
            System.out.println("Exception [" + e.getMessage() + "] processing journals for \"" + journalUrl + "\"; Ignoring");
        }
        return journalMetadata;
    }

    private LinkedList<String> getAuthorNameEmail(JSONArray jsonArray) {
        String name = "";
        String email = "";
        for (int i=0;i<jsonArray.size();i++) {
            // jsonObject1 -> "author"
            JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
//                        System.out.println();
            String s = (String) jsonObject1.get("#name");
            if (s.equals("author")) {
                JSONArray jsonArray1 = (JSONArray) jsonObject1.get("$$");
                for (int j=0;j<jsonArray1.size();j++) {
                    JSONObject jsonObject2 = (JSONObject) jsonArray1.get(j);
                    if (jsonObject2.get("#name").equals("given-name")) {
                        name = name + jsonObject2.get("_");
                    } else if(jsonObject2.get("#name").equals("surname")) {
                        name = name + " " + jsonObject2.get("_");
                    } else if(jsonObject2.get("#name").equals("e-address")) {
                        email = (String) jsonObject2.get("_");
                    } else {

                    }
                }
                if (email.length() != 0) {
                    break;
                } else {
                    name = "";
                }
            }
        }
        LinkedList<String> list = new LinkedList<>();
        list.add(name);
        list.add(email);
//        return name + "," + email + "\n";
        return list;
    }

    private String generateRow(LinkedList<String> list) {
        String title = list.getLast();
        if (title.indexOf(" - ScienceDirect") != -1) {
            title = title.substring(0, title.indexOf(" - ScienceDirect"));
        }
        title = title.replaceAll(",", "");
        list.set(list.size()-1, title);
        String row = String.join(",", list);
        row = row + "\n";
//        System.out.println(row);
        return row;
    }

}
