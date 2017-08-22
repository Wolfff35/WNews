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
 * Created by wolff on 18.07.2017.
 */

public class WriteChannelToLocalDB {
    //private Context mContext;

    public WriteChannelToLocalDB(){
       /// mContext=context;
    }

    //--------------------------------------------------------
    public WChannel readChannelFromInet(String link) {
        // Log.e("readNews", "begin");
        try {
            //Log.e("readNews", "1 " + channel.getLink());
            URL url = new URL(link);
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
                NodeList nodeList = element.getElementsByTagName("channel");
                if (nodeList.getLength() > 0) {
                        Element entry = (Element) nodeList.item(0);
                        if(entry!=null) {
                            String _imagelink;
                            String _pubDate;
                            String _title;
                            String _description;

                            try{
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
                                NodeList enclosureList = entry.getElementsByTagName(DbSchema.Table_Channel.Cols.IMAGE);
                                if (enclosureList.getLength() > 0) {
                                    _imagelink = enclosureList.item(0).getAttributes().item(0).getNodeValue();
                                    //Log.e("ENCLOSURE", "" + _enclosure);
                                } else {
                                    _imagelink = "";
                                }
                            } catch (Exception e) {
                                _imagelink = "";
                            }
                            WChannel channel = new WChannel();
                            channel.setLink(link);
                            channel.setImage(_imagelink);
                            channel.setDescription(_description);
                            channel.setName(_title);
                            channel.setPubDate(new Date(_pubDate));
                            channel.setTitle(_title);
                            inputStream.close();
                            return channel;
                        }else {
                            return null;
                        }
                }
            } else {
                Log.e("readChannel","ERRORRRRRR "+connection.getResponseCode());
                return null;
            }
        } catch (MalformedURLException e) {
            Log.e("readChannel", "2");
            return null;
        } catch (IOException e) {
            Log.e("readChannel", "3 "+e.getLocalizedMessage());
            return null;
        } catch (ParserConfigurationException e) {
            Log.e("readChannel", "4");
            return null;
        } catch (SAXException e) {
            Log.e("readChannel", "5");
            return null;
        }finally {

            //Log.e("readNews","6=============================================================================");
        }
        return null;
    }

}
