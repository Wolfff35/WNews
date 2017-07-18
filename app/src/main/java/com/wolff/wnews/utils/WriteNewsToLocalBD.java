package com.wolff.wnews.utils;

import android.content.Context;
import android.util.Log;

import com.wolff.wnews.localdb.DataLab;
import com.wolff.wnews.localdb.DbSchema;
import com.wolff.wnews.model.WChannel;
import com.wolff.wnews.model.WNews;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by wolff on 10.07.2017.
 */

public class WriteNewsToLocalBD {
    //читаем новости из инета, и пишем в локал ДБ
    private Context mContext;

    public WriteNewsToLocalBD(Context context){
        mContext=context;
    }

//--------------------------------------------------------
public void readNewsFromChannelAndWriteToLocalBD(WChannel channel) {
   // Log.e("readNews", "begin");
    try {
        //Log.e("readNews", "1 " + channel.getLink());
        URL url = new URL(channel.getLink());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", "Test");
        connection.setRequestProperty("Connection", "close");
        connection.setConnectTimeout(1000);
        connection.connect();
        //Log.e("readNews", "1 code = " + connection.getResponseCode());
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            //Log.e("readNews", "1-1");
            InputStream inputStream = connection.getInputStream();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            Element element = document.getDocumentElement();
            NodeList nodeList = element.getElementsByTagName("item");
            if (nodeList.getLength() > 0) {
                //Log.e("readNews", "1-2 length = "+nodeList.getLength());
                for (int i = 0; i < nodeList.getLength(); i++) {
                    //Log.e("readNews", "1-3-" + i);
                    String _author;
                    String _category;
                    String _comments;
                    String _enclosure;
                    String _source;
                    Element entry = (Element) nodeList.item(i);
                    String _link = entry.getElementsByTagName(DbSchema.BaseColumns.LINK).item(0).getFirstChild().getNodeValue();
                    String _guid = entry.getElementsByTagName(DbSchema.Table_News.Cols.GUID).item(0).getFirstChild().getNodeValue();
                    //Log.e("GUID",""+_guid);
                    String _pubDate = entry.getElementsByTagName(DbSchema.BaseColumns.PUB_DATE).item(0).getFirstChild().getNodeValue();
                    String _title = entry.getElementsByTagName(DbSchema.BaseColumns.TITLE).item(0).getFirstChild().getNodeValue();
                    String _description = entry.getElementsByTagName(DbSchema.BaseColumns.DESCRIPTION).item(0).getFirstChild().getNodeValue();
                    try {
                        _author = entry.getElementsByTagName(DbSchema.Table_News.Cols.AUTHOR).item(0).getFirstChild().getNodeValue();
                    } catch (Exception e) {
                        _author = "";
                    }
                    try {
                        _category = entry.getElementsByTagName(DbSchema.Table_News.Cols.CATEGORY).item(0).getFirstChild().getNodeValue();
                    } catch (Exception e) {
                        _category = "";
                    }
                    try {
                        _comments = entry.getElementsByTagName(DbSchema.Table_News.Cols.COMMENTS).item(0).getFirstChild().getNodeValue();
                    } catch (Exception e) {
                        _comments = "";
                    }
                    try {
                        NodeList enclosureList = entry.getElementsByTagName(DbSchema.Table_News.Cols.ENCLOSURE);
                        if (enclosureList.getLength() > 0) {
                            _enclosure = enclosureList.item(0).getAttributes().item(0).getNodeValue();
                            //Log.e("ENCLOSURE", "" + _enclosure);
                        } else {
                            _enclosure = "";
                        }
                    } catch (Exception e) {
                        _enclosure = "";
                        //Log.e("ERROR", "" + e.getLocalizedMessage());
                    }
                    try {
                        _source = entry.getElementsByTagName(DbSchema.Table_News.Cols.SOURCE).item(0).getFirstChild().getNodeValue();
                    } catch (Exception e) {
                        _source = "";
                    }
                    DataLab dataLab = DataLab.get(mContext);
                    WNews currNews = dataLab.getWNewsByGuid(_guid);
                    if (currNews == null) {
                        //Log.e("readNews", "1-4");
                        WNews news = new WNews();
                        news.setIdChannel(channel.getId());
                        news.setLink(_link);
                        news.setSource(_source);
                        news.setEnclosure(_enclosure);
                        news.setComments(_comments);
                        news.setAuthor(_author);
                        news.setCategory(_category);
                        news.setDescription(_description);
                        news.setGuid(_guid);
                        news.setName(_title);
                        news.setPubDate(new Date(_pubDate));
                        news.setTitle(_title);
                        dataLab.news_add(news);
                    }else {
                        //Log.e("readNews", "--Already exist!!"+i+" "+_pubDate+" ; "+_guid);

                        Date newDate = new Date(_pubDate);
                        if(new MySettings().UPDATE_NEWS_PUBDATE&&newDate!=null) {
                            if ((newDate.getTime() - currNews.getPubDate().getTime() > 10000)) {
                                Date oldDate = currNews.getPubDate();
                                currNews.setPubDate(new Date(_pubDate));
                                dataLab.news_update(currNews);
                                DateUtils dateUtils = new DateUtils();
                                Log.e("UPDATE NEWS", "old date: " + dateUtils.dateToString(oldDate, dateUtils.DATE_FORMAT_VID_FULL) + "; new date: " + dateUtils.dateToString(currNews.getPubDate(), dateUtils.DATE_FORMAT_VID_FULL));
                            }
                        }


                    }
                }
            }
            inputStream.close();
        } else {
            Log.e("readNews","ERRORRRRRR "+connection.getResponseCode());
        }
    } catch (MalformedURLException e) {
        Log.e("readNews", "2");
    } catch (IOException e) {
        Log.e("readNews", "3 "+e.getLocalizedMessage());
    } catch (ParserConfigurationException e) {
        Log.e("readNews", "4");
    } catch (SAXException e) {
        Log.e("readNews", "5");
    }finally {
        //Log.e("readNews","6=============================================================================");
    }
}

}
