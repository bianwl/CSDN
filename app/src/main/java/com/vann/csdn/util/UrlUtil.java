package com.vann.csdn.util;

/**
 * author： bwl on 2016-04-11.
 * email: bxl049@163.com
 */
public class UrlUtil {

    public static final String NEWS_LIST_URL_YEJIE = "http://news.csdn.net/news/";// 业界
    public static final String NEWS_LIST_URL_YIDONG = "http://mobile.csdn.net/mobile/";// 移动开放
    public static final String NEWS_LIST_URL_YUJISUAN = "http://cloud.csdn.net/cloud/";// 云计算
    public static final String NEWS_LIST_URL_YANFA = "http://sd.csdn.net/sd/";// 软件研发

    /**
     * 根据文章类型和当前页码生成url
     * @param newsType
     * @param curPage
     * @return
     */
    public static String getUrl(int newsType, int curPage) {
        String url = "";
        switch (newsType) {
            case Constant.NEWS_TYPE_YEJIE:
                url = NEWS_LIST_URL_YEJIE;
                break;
            case Constant.NEWS_TYPE_YIDONG:
                url = NEWS_LIST_URL_YIDONG;
                break;
            case Constant.NEWS_TYPE_YUNJISUAN:
                url = NEWS_LIST_URL_YUJISUAN;
                break;
            case Constant.NEWS_TYPE_YANFA:
                url = NEWS_LIST_URL_YANFA;
                break;

            default:
                break;
        }
        url+=curPage;
        return url;
    }

}
