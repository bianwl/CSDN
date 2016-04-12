package com.vann.csdn.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vann.csdn.R;
import com.vann.csdn.activity.NewsInfoActivity;
import com.vann.csdn.adapter.NewsAdapter;
import com.vann.csdn.bean.CommonException;
import com.vann.csdn.bean.NewsItem;
import com.vann.csdn.biz.NewsItemBiz;
import com.vann.csdn.db.NewsItemDao;
import com.vann.csdn.util.Constant;
import com.vann.csdn.util.NetUtil;

import java.util.List;


/**
 * author： bwl on 2016-03-25.
 * email: bxl049@163.com
 */
public class MainFragment extends Fragment {

    public static final int LOAD_REFRESH = 0x01;
    public static final int LOAD_MORE = 0x02;
    public static final String TIP_ERROR_NO_NETWORK = "没有网络连接";
    public static final String TIP_ERROR_NO_SERVICE = "服务器错误";

    public static final String NEWS_TYPE = "NEWS_TYPE";

    private Context mContext;
    //默认新闻类型
    private int newsType = Constant.NEWS_TYPE_YANFA;
    //当前页面
    private int curPage = 1;
    //业务处理类
    private NewsItemBiz mNewsItemBiz;

    private SwipeRefreshLayout mSwipeRefresh;
    private RecyclerView mRecycleView;
    private LinearLayoutManager mManager;
    private NewsAdapter mAdapter;

    //是否从服务器下载数据
    private boolean isLoadFromService;
    //与数据库交互
    private NewsItemDao mNewsItemDao;


    public static MainFragment newInstance(int pos) {
        Bundle args = new Bundle();
        args.putInt(NEWS_TYPE, pos);
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        mNewsItemDao = new NewsItemDao(mContext);
        mNewsItemBiz = new NewsItemBiz();
        initView();
        mSwipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                // Runnable为了能够第一次进入页面的时候显示加载进度条
                mSwipeRefresh.setRefreshing(true);
            }
        });
        initData();
        initEvent();
        new DownLoadTask().execute(LOAD_REFRESH);
    }

    private void initView() {
        mSwipeRefresh = (SwipeRefreshLayout) getView().findViewById(R.id.id_swiperefresh);
        mSwipeRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary,
                R.color.colorPrimary, R.color.colorPrimary);
        mRecycleView = (RecyclerView) getView().findViewById(R.id.id_recycleview);
        mManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(mManager);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initEvent() {

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new DownLoadTask().execute(LOAD_REFRESH);
            }
        });
        mRecycleView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int last = mManager.findLastVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE && last + 1 == mAdapter.getItemCount() && mAdapter.getItemCount() > 1) {
                    new DownLoadTask().execute(LOAD_MORE);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        mAdapter.setOnItemClickLitener(new NewsAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                NewsItem item = mAdapter.getDatas().get(position);
                NewsInfoActivity.actionStart(mContext, item.getLink());
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }


    private void initData() {
        Bundle bundle = getArguments();
        newsType = bundle.getInt(NEWS_TYPE, Constant.NEWS_TYPE_YEJIE);
        mAdapter = new NewsAdapter(mContext);
        mRecycleView.setAdapter(mAdapter);
    }

    class DownLoadTask extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... params) {
            switch (params[0]) {
                case LOAD_REFRESH:
                    return refreshData();
                case LOAD_MORE:
                    return loadMoreData();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (null == result) {
                mAdapter.setIsLoading(false);
                mAdapter.setmError(null);
            } else {
                mAdapter.setmError(result);
                mAdapter.setIsLoading(true);
                Snackbar.make(mSwipeRefresh, result, Snackbar.LENGTH_LONG).show();
            }
            mSwipeRefresh.setRefreshing(false);
            mAdapter.notifyDataSetChanged();
        }
    }

    private String refreshData() {
        if (NetUtil.isOnline(mContext)) {
            curPage = 1;
            try {
                List<NewsItem> items = mNewsItemBiz.getNewsItems(newsType, curPage);
                if (!items.isEmpty()) {
                    mAdapter.setDatas(items);
                    mNewsItemDao.refreshData(newsType, items);
                }

                isLoadFromService = true;
            } catch (CommonException e) {
                e.printStackTrace();
                isLoadFromService = false;
                return TIP_ERROR_NO_SERVICE;
            }
        } else {
            List<NewsItem> items = mNewsItemDao.getNewsItems(curPage, newsType);
            if (!items.isEmpty()) {
                mAdapter.setDatas(items);
                isLoadFromService = false;
            }
            return TIP_ERROR_NO_NETWORK;
        }
        return null;
    }

    private String loadMoreData() {
        mAdapter.setIsLoading(true);
        if (isLoadFromService) {
            curPage++;
            try {
                List<NewsItem> items = mNewsItemBiz.getNewsItems(newsType, curPage);
                mAdapter.addDatas(items);
                mNewsItemDao.addNewsItems(items);
            } catch (CommonException e) {
                e.printStackTrace();
                return e.getMessage();
            }

        } else {
            curPage++;
            List<NewsItem> items = mNewsItemDao.getNewsItems(curPage, newsType);
            mAdapter.addDatas(items);
            return TIP_ERROR_NO_NETWORK;
        }
        return null;
    }

}
