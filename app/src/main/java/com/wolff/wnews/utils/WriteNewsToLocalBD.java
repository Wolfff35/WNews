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
public void readNewsFromChannelAndWriteToLocalBD(WChannel channel){
    //Log.e("readNews","begin");
    try {
        //Log.e("readNews","1");
        URL url = new URL(channel.getLink());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
            //Log.e("readNews","1-1");
            InputStream inputStream = connection.getInputStream();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            Element element = document.getDocumentElement();
            NodeList nodeList = element.getElementsByTagName("item");
            if(nodeList.getLength()>0){
                //Log.e("readNews","1-2");
                for(int i=0;i<nodeList.getLength();i++){
                    //Log.e("readNews","1-3-"+i);
                    String _author;
                    String _category;
                    String _comments;
                    String _enclosure;
                    String _source;
                    Element entry = (Element)nodeList.item(i);
                    String _link = entry.getElementsByTagName(DbSchema.BaseColumns.link).item(0).getFirstChild().getNodeValue();
                    String _guid = entry.getElementsByTagName(DbSchema.Table_News.Cols.guid).item(0).getFirstChild().getNodeValue();
                    String _pubDate =  entry.getElementsByTagName(DbSchema.BaseColumns.pubDate).item(0).getFirstChild().getNodeValue();
                    String _title = entry.getElementsByTagName(DbSchema.BaseColumns.title).item(0).getFirstChild().getNodeValue();
                    String _description = entry.getElementsByTagName(DbSchema.BaseColumns.description).item(0).getFirstChild().getNodeValue();
                    try{_author = entry.getElementsByTagName(DbSchema.Table_News.Cols.author).item(0).getFirstChild().getNodeValue();} catch (Exception e){_author="";}
                    try{_category = entry.getElementsByTagName(DbSchema.Table_News.Cols.category).item(0).getFirstChild().getNodeValue();} catch (Exception e){_category="";}
                    try{_comments = entry.getElementsByTagName(DbSchema.Table_News.Cols.comments).item(0).getFirstChild().getNodeValue();} catch (Exception e){_comments="";}
                    //try{_enclosure = entry.getElementsByTagName(DbSchema.Table_News.Cols.enclosure).item(0).getFirstChild().getNodeValue();} catch (Exception e){_enclosure="";}
                    try{
                        NodeList enclosureList = entry.getElementsByTagName(DbSchema.Table_News.Cols.enclosure);
                        if(enclosureList.getLength()>0){
                            _enclosure = enclosureList.item(0).getAttributes().item(0).getNodeValue();
                           // Log.e("ENCLOSURE",""+_enclosure);
                        }else {
                            _enclosure="";
                        }
                    } catch (Exception e){
                        _enclosure="";
                        //Log.e("ERROR",""+e.getLocalizedMessage());
                    }
                    try{_source = entry.getElementsByTagName(DbSchema.Table_News.Cols.source).item(0).getFirstChild().getNodeValue();} catch (Exception e){_source="";}
                    DataLab dataLab = DataLab.get(mContext);
                    if(dataLab.getWNewsByGuid(_guid)==null) {
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
                    }
                }
            }
        }
    } catch (MalformedURLException e) {
        Log.e("readNews","2");
    } catch (IOException e) {
        Log.e("readNews","3");
    } catch (ParserConfigurationException e) {
        Log.e("readNews","4");
    } catch (SAXException e) {
        Log.e("readNews","5");
    }
}
}
