package com.wolff.wnews.utils;

import android.app.Activity;
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by wolff on 02.08.2017.
 */

public class ImportChannels {

    public void getChannelsFromOPML(Context context, String fileName) {
        //InputStream inputStream = connection.getInputStream();
        File opmlfile = new File(fileName);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(opmlfile);
        Element element = document.getDocumentElement();
        NodeList nodeList = element.getElementsByTagName("outline");
        if (nodeList.getLength() > 0) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                  Element entry = (Element) nodeList.item(i);
                Node type = entry.getAttributes().getNamedItem("type");
                if(type!=null) {
                    if (type.getNodeValue().equalsIgnoreCase("rss")) {
                        String _link = entry.getAttributes().getNamedItem("xmlUrl").getNodeValue();
                        String _description = entry.getAttributes().getNamedItem("text").getNodeValue();
                        String _title = entry.getAttributes().getNamedItem("title").getNodeValue();

                        //Log.e("CHANNEL", "text -  " + entry.getAttributes().getNamedItem("text").getNodeValue());
                        //Log.e("CHANNEL", "title - " + entry.getAttributes().getNamedItem("title").getNodeValue());
                        //Log.e("CHANNEL", "xmlUrl -  " + entry.getAttributes().getNamedItem("xmlUrl").getNodeValue());
                        //Log.e("CHANNEL", "htmlUrl -  " + entry.getAttributes().getNamedItem("htmlUrl").getNodeValue());
                        //Log.e("CHANNEL", " ================================================================");
                        DataLab dataLab = DataLab.get(context);
                        WChannel currChannel = dataLab.getWChannelByLink(_link);
                        if (currChannel == null) {
                            //Log.e("readNews", "1-4");
                            WChannel channel = new WChannel();
                            //news.setIdChannel(channel.getId());
                            channel.setLink(_link);
                            channel.setDescription(_description);
                            channel.setName(_title);
                            //channel.setPubDate(new Date());
                            channel.setTitle(_title);
                            dataLab.channel_add(channel);
                        }

                    }
                }

            }
        }else {
            //Log.e("readNews","ПУСТОЙ СПИСОК!!");
        }
        } catch (ParserConfigurationException e) {
            Log.e("ERROR 1",""+e.getLocalizedMessage());
            //e.printStackTrace();
        } catch (SAXException e) {
            Log.e("ERROR 2",""+e.getLocalizedMessage());
            //e.printStackTrace();
        } catch (IOException e) {
            Log.e("ERROR 3",""+e.getLocalizedMessage());
            //e.printStackTrace();
        }

    }
}
