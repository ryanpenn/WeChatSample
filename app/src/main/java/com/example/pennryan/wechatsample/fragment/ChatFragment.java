package com.example.pennryan.wechatsample.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pennryan.wechatsample.R;
import com.example.pennryan.wechatsample.slideview.ListViewCompat;
import com.example.pennryan.wechatsample.slideview.SlideView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pennryan on 15/7/9.
 */
public class ChatFragment extends Fragment
        implements AdapterView.OnItemClickListener, View.OnClickListener, SlideView.OnSlideListener {

    private ListViewCompat mListView;
    private List<MessageItem> mMessageItems = new ArrayList<MessageItem>();
    private SlideView mLastSlideViewWithStatusOn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, dm);
        params.setMargins(margin, margin, margin, margin);

        mListView = new ListViewCompat(getActivity());
        mListView.setLayoutParams(params);
        mListView.setBackgroundColor(Color.parseColor("#fff4f7f9"));
        mListView.setCacheColorHint(Color.parseColor("#00000000"));
        mListView.setDivider(new ColorDrawable(Color.parseColor("#dddbdb")));
        mListView.setDividerHeight(1);
        mListView.setDrawSelectorOnTop(false);
        mListView.setSelector(android.R.color.transparent);
        mListView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        initView();

        return mListView;
    }

    public void initView() {
        //mListView = (ListViewCompat) getActivity().findViewById(R.id.list);

        for (int i = 0; i < 20; i++) {
            MessageItem item = new MessageItem();
            if (i % 3 == 0) {
                item.iconRes = R.mipmap.default_qq_avatar;
                item.title = "腾讯新闻";
                item.msg = "青岛爆炸满月：大量鱼虾死亡";
                item.time = "晚上18:18";
            } else {
                item.iconRes = R.mipmap.wechat_icon;
                item.title = "微信团队";
                item.msg = "欢迎你使用微信";
                item.time = "12月18日";
            }
            mMessageItems.add(item);
        }
        mListView.setAdapter(new SlideAdapter());
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onSlide(View view, int status) {
        if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
            mLastSlideViewWithStatusOn.shrink();
        }

        if (status == SLIDE_STATUS_ON) {
            mLastSlideViewWithStatusOn = (SlideView) view;
        }
    }

    private class SlideAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        SlideAdapter() {
            super();
            mInflater = getActivity().getLayoutInflater();
        }

        @Override
        public int getCount() {
            return mMessageItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mMessageItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            SlideView slideView = (SlideView) convertView;
            if (slideView == null) {
                View itemView = mInflater.inflate(R.layout.list_item, null);

                slideView = new SlideView(getActivity());
                slideView.setContentView(itemView);

                holder = new ViewHolder(slideView);
                slideView.setOnSlideListener(ChatFragment.this);
                slideView.setTag(holder);
            } else {
                holder = (ViewHolder) slideView.getTag();
            }
            MessageItem item = mMessageItems.get(position);
            item.slideView = slideView;
            item.slideView.shrink();

            holder.icon.setImageResource(item.iconRes);
            holder.title.setText(item.title);
            holder.msg.setText(item.msg);
            holder.time.setText(item.time);
            holder.deleteHolder.setOnClickListener(ChatFragment.this);

            return slideView;
        }

    }

    public class MessageItem {
        public int iconRes;
        public String title;
        public String msg;
        public String time;
        public SlideView slideView;
    }

    private static class ViewHolder {
        public ImageView icon;
        public TextView title;
        public TextView msg;
        public TextView time;
        public ViewGroup deleteHolder;

        ViewHolder(View view) {
            icon = (ImageView) view.findViewById(R.id.icon);
            title = (TextView) view.findViewById(R.id.title);
            msg = (TextView) view.findViewById(R.id.msg);
            time = (TextView) view.findViewById(R.id.time);
            deleteHolder = (ViewGroup)view.findViewById(R.id.holder);
        }
    }
}
