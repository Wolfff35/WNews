package com.wolff.wnews.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.wolff.wnews.localdb.DataLab;
import com.wolff.wnews.localdb.DbSchema;
import com.wolff.wnews.model.WChannel;
import com.wolff.wnews.model.WNews;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    boolean updateNewsPubDate = preferences.getBoolean("updateNewsPubDate",false);
  //  Log.e("readNews", "1 " + channel.getLink());
    try {
  //      Log.e("readNews", "1.1 " + channel.getLink());
        URL url = new URL(channel.getLink());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", "Test");
        connection.setRequestProperty("Connection", "close");
        //connection.setConnectTimeout(1000);
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
                    String _link;
                    String _guid;
                    String _description;
                    String _enclosure;
                    String _pubDate;
                    String _title;
                    Element entry = (Element) nodeList.item(i);
                    try {
                        _link = entry.getElementsByTagName(DbSchema.BaseColumns.LINK).item(0).getFirstChild().getNodeValue();
                    }catch (Exception e){
                        _link="";
                    }
                    try {
                        _guid = entry.getElementsByTagName(DbSchema.Table_News.Cols.GUID).item(0).getFirstChild().getNodeValue();
                    }catch (Exception e){
                        _guid="";
                    }
                    try {
                        _pubDate = entry.getElementsByTagName(DbSchema.BaseColumns.PUB_DATE).item(0).getFirstChild().getNodeValue();
                    }catch (Exception e){
                        _pubDate="";
                    }
                    try {
                        _title = entry.getElementsByTagName(DbSchema.BaseColumns.TITLE).item(0).getFirstChild().getNodeValue();
                    }catch (Exception e){
                        _title="";
                    }
                    try {
                        _description = entry.getElementsByTagName(DbSchema.BaseColumns.DESCRIPTION).item(0).getFirstChild().getNodeValue();
                    }catch (Exception e){
                        _description="";
                    }
                    try {
                        NodeList enclosureList = entry.getElementsByTagName(DbSchema.Table_News.Cols.ENCLOSURE);
                        if (enclosureList.getLength() > 0) {
                            _enclosure = enclosureList.item(0).getAttributes().getNamedItem("url").getNodeValue();
                            String _enclosure1 = enclosureList.item(0).getAttributes().getNamedItem("type").getNodeValue();
                            //Log.e("ENCLOSURE", "" + _enclosure);
                            //Log.e("ENCLOSURE 2", "" + _enclosure1);
                        } else {
                            _enclosure = "";
                        }
                    } catch (Exception e) {
                        _enclosure = "";
                        //Log.e("ERROR", "4" + e.getLocalizedMessage());
                    }
                     DataLab dataLab = DataLab.get(mContext);
                    WNews currNews = dataLab.getWNewsByGuid(_guid);
                    if (currNews == null) {
                        //Log.e("readNews", "1-4");
                        WNews news = new WNews();
                        news.setIdChannel(channel.getId());
                        news.setLink(_link);
                        news.setEnclosure(_enclosure);
                        news.setDescription(_description);
                        news.setGuid(_guid);
                        news.setName(_title);
                        news.setPubDate(new Date(_pubDate));
                        news.setTitle(_title);
                        dataLab.news_add(news);
                    }else {
                        //Log.e("readNews", "--Already exist!!"+i+" "+_pubDate+" ; "+_guid);

                        Date newDate = new Date(_pubDate);
                        if(updateNewsPubDate&&newDate!=null) {
                            if ((newDate.getTime() - currNews.getPubDate().getTime() > 10000)) {
                                Date oldDate = currNews.getPubDate();
                                currNews.setPubDate(new Date(_pubDate));
                                dataLab.news_update(currNews);
                                DateUtils dateUtils = new DateUtils();
                                //Log.e("UPDATE NEWS", "old date: " + dateUtils.dateToString(oldDate, dateUtils.DATE_FORMAT_VID_FULL) + "; new date: " + dateUtils.dateToString(currNews.getPubDate(), dateUtils.DATE_FORMAT_VID_FULL));
                            }
                        }


                    }
                }
            }else {
                //Log.e("readNews","ПУСТОЙ СПИСОК!!");
            }
            inputStream.close();
            //Log.e("readNews","FINISH");
        } else {
 //           Log.e("readNews","ERRORRRRRR "+connection.getResponseCode());
 //           Log.e("readNews","ERRORRRRRR "+connection.getResponseCode());
 //           Log.e("readNews","ERRORRRRRR "+channel.getLink());
        }
    } catch (MalformedURLException e) {
 //       Log.e("readNews", "2");
    } catch (IOException e) {
       // Log.e("readNews", "3 "+e.getLocalizedMessage());
    } catch (ParserConfigurationException e) {
 //       Log.e("readNews", "4");
    } catch (SAXException e) {
  //      Log.e("readNews", "5");
    }finally {
        //Log.e("readNews","6=============================================================================");
    }
}

}
