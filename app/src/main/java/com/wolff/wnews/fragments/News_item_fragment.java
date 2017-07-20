package com.wolff.wnews.fragments;

import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wolff.wnews.R;
import com.wolff.wnews.localdb.DataLab;
import com.wolff.wnews.model.WChannel;
import com.wolff.wnews.model.WNews;
import com.wolff.wnews.service.GetChannelInfo_Task;
import com.wolff.wnews.utils.DateUtils;
import com.wolff.wnews.utils.MySettings;

import java.util.concurrent.ExecutionException;

/**
 * Created by wolff on 13.07.2017.
 */

public class News_item_fragment extends Fragment {
    private static final String ARG_NEWS_ITEM = "WNewsItem";
    private WNews mNewsItem;
    private Menu mOptionsMenu;

    TextView tvNewsItem_Name;
    TextView tvNewsItem_Channel_PubDate;
    TextView tvNewsItem_Describe;
    ImageView ivNewsItem_Picture;
    Point ScreenSize;
    Button btnOpenNews;

    public static News_item_fragment newIntance(WNews item){
        Bundle args = new Bundle();
        args.putSerializable(ARG_NEWS_ITEM,item);
        News_item_fragment fragment = new News_item_fragment();
        fragment.setArguments(args);
        return fragment;

    }

    public News_item_fragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setHasOptionsMenu(true);
        mNewsItem = (WNews) getArguments().getSerializable(ARG_NEWS_ITEM);
        ScreenSize = new Point();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        display.getSize(ScreenSize);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.news_item_fragment, container, false);
        tvNewsItem_Name = (TextView)view.findViewById(R.id.tvNewsItem_Name);
        tvNewsItem_Channel_PubDate = (TextView)view.findViewById(R.id.tvNewsItem_Channel_pubDate);
        tvNewsItem_Describe = (TextView)view.findViewById(R.id.tvNewsItem_Describe);
        ivNewsItem_Picture = (ImageView)view.findViewById(R.id.ivNewsItem_Picture);
        btnOpenNews = (Button)view.findViewById(R.id.btnOpenNews);

        tvNewsItem_Name.setText(mNewsItem.getName());
        DataLab dataLab = DataLab.get(getContext());
        DateUtils dateUtils = new DateUtils();
        WChannel channel = dataLab.findChannelById(mNewsItem.getIdChannel(),dataLab.getWChannelsList());
        tvNewsItem_Channel_PubDate.setText(channel.getTitle()+" / "+dateUtils.calculateInterval(mNewsItem.getPubDate()));
        tvNewsItem_Describe.setText(mNewsItem.getDescription());

        if(!mNewsItem.getEnclosure().isEmpty()) {
            Picasso picasso = Picasso.with(getContext());
            if(new MySettings().SHOW_PICASSO_INDICATOR) {
                picasso.setIndicatorsEnabled(true);
            }
            picasso.load(mNewsItem.getEnclosure())
                    .resize(ScreenSize.x-5,ScreenSize.x-5)
                    //        .placeholder(R.drawable.ic_download_black)
                    //        .error(R.drawable.ic_error_black)
                    .into(ivNewsItem_Picture);
        }
        btnOpenNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri address = Uri.parse(mNewsItem.getLink());
                Intent openLinkIntent = new Intent(Intent.ACTION_VIEW,address);
                startActivity(openLinkIntent);
            }
        });
        super.onCreateView(inflater,container,savedInstanceState);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.mOptionsMenu = menu;
        //inflater.inflate(R.menu.menu_item_actions,mOptionsMenu);
        super.onCreateOptionsMenu(mOptionsMenu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_item_save: {
                break;
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
