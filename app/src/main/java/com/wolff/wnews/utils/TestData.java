package com.wolff.wnews.utils;

import android.content.Context;

import com.wolff.wnews.localdb.DataLab;
import com.wolff.wnews.model.WChannel;
import com.wolff.wnews.model.WChannelGroup;

/**
 * Created by wolff on 11.07.2017.
 */

public class TestData {
public void fillTestData(Context context){
    DataLab dataLab = DataLab.get(context);
    WChannelGroup group = new WChannelGroup();
    group.setName("Group NEWS");
   // dataLab.channelGroup_add(group);
    //item.setLink("http://news.liga.net/news/rss.xml");
    //item.setLink("https://www.rbc.ua/static/rss/all.rus.rss.xml");
    //item.setLink("http://k.img.com.ua/rss/ru/economics.xml");

    WChannel channel = new WChannel();
   // channel.setIdGroup(1);
   // channel.setName("Facty.UA");
   // channel.setLink("http://fakty.ua/rss_feed/ukraina");
   // if(dataLab.getWChannelByLink(channel.getLink())==null) {
   //     dataLab.channel_add(channel);
   // }

    channel.setIdGroup(1);
    channel.setName("Цензор");
    channel.setLink("https://censor.net.ua/includes/news_ru.xml");
    if(dataLab.getWChannelByLink(channel.getLink())==null) {
        dataLab.channel_add(channel);
    }
    channel.setIdGroup(1);
    channel.setName("Liga");
    channel.setLink("http://news.liga.net/news/rss.xml");
    if(dataLab.getWChannelByLink(channel.getLink())==null) {
        dataLab.channel_add(channel);
    }
    channel.setIdGroup(1);
    channel.setName("RBC");
    channel.setLink("https://www.rbc.ua/static/rss/all.rus.rss.xml");
    if(dataLab.getWChannelByLink(channel.getLink())==null) {
        dataLab.channel_add(channel);
    }
    channel.setIdGroup(1);
    channel.setName("Korrespondent");
    channel.setLink("http://k.img.com.ua/rss/ru/economics.xml");
    if(dataLab.getWChannelByLink(channel.getLink())==null) {
        dataLab.channel_add(channel);
    }

    }
}
