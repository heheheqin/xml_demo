package com.dream.will.xml;

import org.jsoup.nodes.Element;

/**
 * Author：Will on 2016/12/3 09:30
 * Mail：heheheqin.will@gmail.com
 */

public class CommetBean {
    public  String userImage;
    public  String userName;
    public  String content;

    public CommetBean(Element element) {
        userImage =element.getElementsByClass("avatars").get(0).select("img").attr("src");
        userName = element.getElementsByClass("avatars").get(0).select("img").attr("alt");
        content = element.getElementsByClass("replay").get(0).select("span").text();
    }
}
