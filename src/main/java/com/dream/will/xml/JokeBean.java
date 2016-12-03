package com.dream.will.xml;

import android.util.Log;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Author：Will on 2016/12/2 10:11
 * Mail：heheheqin.will@gmail.com
 */

public class JokeBean {
    /**
     * 详情
     */
    public String content;
    /**
     * 图片地址
     */
    public String image;
    /**
     * 链接地址
     */
    public String href;

    /**
     * 用户名字
     */
    public String userName;
    /**
     * 用户 图片
     */
    public String userImage;
    public int type = 0;

    public JokeBean(Element e) {
//   获取用户头像  昵称
        userImage = e.getElementsByClass("author").get(0).select("img").attr("src");
        userName = e.getElementsByClass("author").get(0).select("img").attr("alt");
        //链接地址
        href = e.getElementsByClass("contentHerf").get(0).select("a").attr("href");
        //  获取内容 图片
        this.content = ((Element) e.getElementsByClass("content").get(0)).select("span").text();
//        Log.i("TAG", "JokeBean: contents---------" + contents + ":::name:"
//                + userNames + ":::::image:" + userImages + ":::hrefs:" + hrefs);
        Elements imgs = e.getElementsByClass("thumb");
        if ((imgs != null) && (imgs.size() > 0)) {
            Elements es = ((Element) imgs.get(0)).select("img");
            if ((es != null) && (es.size() > 0)) {
                this.image = ((Element) es.get(0)).attr("src");
                Log.i("TAG", "JokeBean: image---------" + image);
                this.type = 1;
            }
        }


    }
}
