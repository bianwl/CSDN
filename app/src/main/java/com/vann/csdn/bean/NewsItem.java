package com.vann.csdn.bean;

/**
 * author： bwl on 2016-04-11.
 * email: bxl049@163.com
 */
public class NewsItem {

    private int id;
    //标题
    private String title;
    //链接
    private String link;
    //图片连接
    private String imgLink;
    //内容
    private String Content;
    //发布时间
    private String date;
    //类型
    private int newsType;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public String getImgLink() {
        return imgLink;
    }
    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }
    public String getContent() {
        return Content;
    }
    public void setContent(String content) {
        Content = content;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public int getNewsType() {
        return newsType;
    }
    public void setNewsType(int newsType) {
        this.newsType = newsType;
    }


    @Override
    public String toString() {
        return "newsItem[id="+id+",title="+title+",link="+link+",imgLink="+imgLink
                +",content="+Content+",date="+date+",newsType="+newsType;
    }
}
