package com.vann.csdn.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.vann.csdn.R;
import com.vann.csdn.bean.CommonException;
import com.vann.csdn.bean.HtmlFrame;
import com.vann.csdn.bean.NewsDetail;
import com.vann.csdn.biz.NewsItemBiz;
import com.vann.csdn.util.DataUtil;

/**
 * author： bwl on 2016-04-11.
 * email: bxl049@163.com
 */
public class NewsInfoActivity extends BaseActivity {

    private ImageView mBack;
    private SwipeRefreshLayout mRefresh;
    private WebView mWeb;
    private String link;
    private WebSettings mWebSettings;
    private NewsItemBiz newsItemBiz;
    private TextView mTag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_info);
        initView();
        link = getIntent().getStringExtra("link");
        newsItemBiz = new NewsItemBiz();
        mRefresh.post(new Runnable() {
            @Override
            public void run() {
                mRefresh.setRefreshing(true);
            }
        });
        new LoadDataTask().execute();
    }

    private void initView() {
        mRefresh = (SwipeRefreshLayout) findViewById(R.id.id_newsinfo_refresh);
        mWeb = (WebView) findViewById(R.id.id_newsinfo_webview);
        mTag = (TextView) findViewById(R.id.id_loadfailed);
        mBack = (ImageView) findViewById(R.id.id_imb_back);
        mWebSettings = mWeb.getSettings();
        mWebSettings.setSupportZoom(true);
        mWeb.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mRefresh.setRefreshing(false);
            }
        });
        mRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary,
                R.color.colorPrimary, R.color.colorPrimary);
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new LoadDataTask().execute();
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

   class LoadDataTask extends AsyncTask<Void,Void,String>{
       @Override
       protected String doInBackground(Void... params) {
           String html = null;
           try {
               html = DataUtil.doGet(link);
           } catch (CommonException e) {
               e.printStackTrace();
           }
           return html;
       }

       @Override
       protected void onPostExecute(String s) {
           if(!TextUtils.isEmpty(s)){
               mTag.setVisibility(View.GONE);
               NewsDetail mNews = newsItemBiz.getNewsDetial(s);
               StringBuffer stringBuffer = new StringBuffer();
               stringBuffer.append(formatHtml(HtmlFrame.FRAME, mNews.getTitle(), mNews.getInfor(), mNews.getTexts()));
               mWeb.loadData(stringBuffer.toString(), "text/html; charset=UTF-8", null);
           }else{
               mTag.setVisibility(View.VISIBLE);
           }
            mRefresh.setRefreshing(false);
       }
   }

    /**
     * 格式化html
     *
     * @param frame 框架
     * @param title 标题
     * @param infor 作者时间
     * @param texts 内容
     * @return
     */
    private String formatHtml(String frame, String title, String infor, String texts) {
        return String.format(frame, title, infor, texts);

    }

    public static void actionStart(Context context,String url){
        Intent intent = new Intent(context,NewsInfoActivity.class);
        intent.putExtra("link",url);
        context.startActivity(intent);
    }
}
