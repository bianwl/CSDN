package com.vann.csdn.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vann.csdn.R;
import com.vann.csdn.bean.NewsItem;
import com.vann.csdn.widget.ProgressWheel;

import java.util.ArrayList;
import java.util.List;

/**
 * author： bwl on 2016-04-11.
 * email: bxl049@163.com
 */
public class NewsAdapter extends RecyclerView.Adapter {
    private final int TYPE_NORMAL = 0;
    private final int TYPE_FOOT = 1;
    private Context mContext;
    private List<NewsItem> mDatas  = new ArrayList<>();
    private OnItemClickLitener mOnItemClickLitener;
    private boolean isLoading;
    private String mError = null;


    public NewsAdapter(Context context) {
        this.mContext = context;
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }
    public void addDatas(List<NewsItem> datas){
        mDatas.addAll(datas);
    }

    public void setDatas(List<NewsItem> datas){
        mDatas.clear();
        mDatas.addAll(datas);
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOT;
        }
        return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.news_listview_item, parent, false);
            return new ItemViewHolder(view);
        }
        if (viewType == TYPE_FOOT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.news_item_footer, parent, false);
            return new FootHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).title.setText(mDatas.get(position).getTitle());
            ((ItemViewHolder) holder).content.setText(mDatas.get(position).getContent());
            ((ItemViewHolder) holder).date.setText(mDatas.get(position).getDate());
            if (mDatas.get(position).getImgLink() != null) {
                ((ItemViewHolder) holder).icon.setVisibility(View.VISIBLE);
                Picasso.with(mContext).load(mDatas.get(position).getImgLink()).placeholder(R.mipmap.news_default_icon)
                        .error(R.mipmap.news_default_icon).into(((ItemViewHolder) holder).icon);
            }else{
                ((ItemViewHolder) holder).icon.setVisibility(View.GONE);
            }
            // 如果设置了回调，则设置点击事件
            if (mOnItemClickLitener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickLitener.onItemClick(holder.itemView, pos);
                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                        return false;
                    }
                });
            }

        }
        if (holder instanceof FootHolder) {
            ((FootHolder) holder).foot.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            if (mError != null) {
                ((FootHolder) holder).progressBar.setVisibility(View.GONE);
                ((FootHolder) holder).message.setText(mError);
            } else {
                ((FootHolder) holder).progressBar.setVisibility(View.VISIBLE);
                ((FootHolder) holder).message.setText("加载中....");
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size()+1;
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;
        TextView content;
        TextView date;

        public ItemViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.id_newsItem_icon);
            title = (TextView) itemView.findViewById(R.id.id_newsItem_title);
            content = (TextView) itemView.findViewById(R.id.id_newItem_content);
            date = (TextView) itemView.findViewById(R.id.id_newsItem_date);
        }
    }

    class FootHolder extends RecyclerView.ViewHolder {
        LinearLayout foot;
        ProgressWheel progressBar;
        TextView message;

        public FootHolder(View itemView) {
            super(itemView);
            foot = (LinearLayout) itemView.findViewById(R.id.item_news_foot);
            progressBar = (ProgressWheel) itemView.findViewById(R.id.item_news_progressbar);
            message = (TextView) itemView.findViewById(R.id.item_news_message);
        }
    }


    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);

    }

    public List<NewsItem> getDatas() {
        return mDatas;
    }
    public String getmError() {
        return mError;
    }

    public void setmError(String mError) {
        this.mError = mError;
    }
}
