package com.wolff.wnews.utils;

import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import com.wolff.wnews.R;
import com.wolff.wnews.localdb.DataLab;
import com.wolff.wnews.model.WChannel;
import com.wolff.wnews.model.WChannelGroup;

import java.util.ArrayList;

/**
 * Created by wolff on 12.07.2017.
 */

public class CreateMenu {
 //      <item android:title="@string/menu_nav_group_news"
//    android:id="@+id/nav_group_news"/>


    public void createMenu(Context context,Menu menu){
        menu.clear();
        Log.e("CREATE MENU","Ok");
        menu.add(Menu.NONE,0,Menu.NONE,"ALL NEWS");
        DataLab dataLab = DataLab.get(context);
        ArrayList<WChannelGroup> mChammelGroupList = dataLab.getWChannelGroupsList();
        //ArrayList<WChannel> mChannelList = dataLab.getWChannelsList();
        ArrayList<WChannel> mChannelList = dataLab.getMenuItems();
      /*  for(WChannelGroup group:mChammelGroupList){
            Log.e("MENU_CREATE","GROUP = "+group.getName()+"id = "+group.getId());
            //menu.addSubMenu(Menu.NONE,(int)group.getId()+1000,Menu.NONE,group.getName()+" g="+group.getId());
            SubMenu sub = menu.addSubMenu(Menu.NONE,(int)group.getId(),Menu.NONE,group.getName()+" g="+group.getId());
            //sub.setGroupVisible((int)group.getId(),true);
            for(WChannel channel:mChannelList){
                if(group.getId()==channel.getIdGroup()) {
                    Log.e("MENU_CREATE","item = "+channel.getName()+" GROUP ID = "+channel.getIdGroup());
                    //menu.add((int) group.getId() + 1000, (int) channel.getId(), Menu.NONE, channel.getName()+" i="+channel.getId());
                    sub.add(Menu.NONE, (int) channel.getId(), Menu.NONE, channel.getName()+" i="+channel.getId());
                }
            }

        }*/
        //без групп
        for(WChannel channel:mChannelList){
           // if(channel.getIdGroup()==0) {
                menu.add(Menu.NONE, (int) channel.getId(), Menu.NONE, channel.getName()+" - "+channel.getMenu_items_all()+"("+(channel.getMenu_items_all()-channel.getMenu_items_read())+")");
           // }
        }
    }
}
