package com.vann.csdn.biz;

import android.util.Log;

import com.vann.csdn.bean.CommonException;
import com.vann.csdn.bean.NewsDetail;
import com.vann.csdn.bean.NewsItem;
import com.vann.csdn.util.DataUtil;
import com.vann.csdn.util.UrlUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * author： bwl on 2016-04-11.
 * email: bxl049@163.com
 */
public class NewsItemBiz {

    public List<NewsItem> getNewsItems(int newsType, int curPage)
            throws CommonException {
        List<NewsItem> newsItems = new ArrayList<>();
        String url = UrlUtil.getUrl(newsType, curPage);
        String htmlStr = DataUtil.doGet(url);
        NewsItem item = null;
        Document doc = Jsoup.parse(htmlStr);
        Elements units = doc.getElementsByClass("unit");
        for (int i = 0; i < units.size(); i++) {
            item = new NewsItem();
            item.setNewsType(newsType);
            Element unit = units.get(i);
            Element h1 = unit.getElementsByTag("h1").get(0);
            Element ha = h1.child(0);
            item.setTitle(h1.text());
            item.setLink(ha.attr("href"));
            Element h4 = unit.getElementsByTag("h4").get(0);
            Element ago = h4.getElementsByClass("ago").get(0);
            item.setDate(ago.text());
            Element dl_ele = unit.getElementsByTag("dl").get(0);
            Element dt_ele = dl_ele.child(0);
            try {
                // 可能没有图片
                Element img_ele = dt_ele.child(0);
                String imgLink = img_ele.child(0).attr("src");
                item.setImgLink(imgLink);
            } catch (IndexOutOfBoundsException e) {
            }

            Element dd_ele = dl_ele.child(1);
            item.setContent(dd_ele.text());
            newsItems.add(item);
        }

        return newsItems;
    }

    public NewsDetail getNewsDetial(String html) {
        NewsDetail news = new NewsDetail();
        Document doc = Jsoup.parse(html);
//        //获取文字的评论链接
//        Element comments = doc.select("td.comm").select("a").first();
//        String commentLink = "http://m.csdn.net/" + comments.attr("href");
//        news.setCommentsLink(commentLink);
//        Log.i("clj", "comments link =" + commentLink);
        // 获得文章中的第一个detail
        Element detail = doc.select("div.wrapper").first();
        //获取title
        Element title = detail.select("h1").first();
        news.setTitle(title.text());
        //infor
        Element infor = detail.select("div.infor").first();
        news.setInfor(infor.text());
        Elements elements = detail.select(".text p");
        StringBuffer buffer = new StringBuffer();
        for (Element element : elements) {
            element.select("img").attr("width", "100%").attr("style", "");
            buffer.append("<p>");
            buffer.append(element.html());
            buffer.append("</p>");
            Log.i("clj", buffer.toString());
        }
        news.setTexts(buffer.toString());
        return news;
    }
}
