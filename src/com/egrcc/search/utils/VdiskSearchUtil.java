package com.egrcc.search.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VdiskSearchUtil {
    
    private static final String vdiskUrl = "http://vdisk.weibo.com";

    public static String ParserHtml(String url) {
        String html = "";  
        BufferedReader in = null;
        try {  
            URL realUrl = new URL(url);  
            URLConnection connection = realUrl.openConnection();  
            connection.setRequestProperty("User-agent","Mozilla/5.0");
            connection.connect();  
            in = new BufferedReader(new InputStreamReader(  
                    connection.getInputStream()));  
            String line;  
            while ((line = in.readLine()) != null) {  
                html += line;  
            }
            
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        finally {  
            try {  
                if (in != null) {  
                    in.close();  
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }
        return html;
    }

    public static String getHtml(String[] keywords, int page) {
    	String q = new String();
    	try {
    	    q = URLEncoder.encode(keywords[0], "UTF-8");
            for (int i = 1; i < keywords.length; i++) {
                q = q + "+" + URLEncoder.encode(keywords[i], "UTF-8");
            }
        } catch (Exception e) {
            System.out.println("遭遇了一个异常！");
        }
        String searchUrl = vdiskUrl + "/search/?type=&sortby=default&keyword=" + q + "&filetype=&page=" + Integer.toString(page);
        System.out.println(searchUrl);
        String html = ParserHtml(searchUrl);  
        return html;

    }

    public static ArrayList<String> getResultUrl(String html) {
        ArrayList<String> resultUrl = new ArrayList<String>();
        String re = "<div class=\"sort_name_detail\"><a href=\"(.+?)\" target=\"_blank\" title=\"(.+?)\">";
        Pattern pattern = Pattern.compile(re);  
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            resultUrl.add(matcher.group(1));
            System.out.println(matcher.group(1));

        }
        
        return resultUrl;
    }
    public static ArrayList<String> getResultContent(String html) {
        ArrayList<String> resultContent = new ArrayList<String>();
        String re = "<div class=\"sort_name_detail\"><a href=\"(.+?)\" target=\"_blank\" title=\"(.+?)\">";
        Pattern pattern = Pattern.compile(re);  
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            resultContent.add(matcher.group(2).replace("\n", "").replace(" ", "")
                .replace("\t", "").replace("<b>", "").replace("</b>", ""));
            System.out.println(matcher.group(2));

        }
        
        return resultContent;
    }
    
}
