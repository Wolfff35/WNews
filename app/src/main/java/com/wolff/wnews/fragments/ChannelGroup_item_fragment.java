package com.wolff.wnews.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.wolff.wnews.R;
import com.wolff.wnews.localdb.DataLab;
import com.wolff.wnews.model.WChannelGroup;

/**
 * Created by wolff on 13.07.2017.
 */

public class ChannelGroup_item_fragment extends Fragment {
    private static final String ARG_CHANNELGROUP_ITEM = "WAccItem";
    private WChannelGroup mGroupItem;
    private boolean mIsNewItem;
    private boolean mIsDataChanged;
    private Menu mOptionsMenu;

    private EditText edGroupItem_Name;
    private TextInputLayout edGroup_Name_layout;

    public static ChannelGroup_item_fragment newIntance(WChannelGroup item){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CHANNELGROUP_ITEM,item);
        ChannelGroup_item_fragment fragment = new ChannelGroup_item_fragment();
        fragment.setArguments(args);
        return fragment;

    }

    public ChannelGroup_item_fragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mGroupItem = (WChannelGroup) getArguments().getSerializable(ARG_CHANNELGROUP_ITEM);
        if(mGroupItem==null){
            mGroupItem = new WChannelGroup();
            mIsNewItem=true;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.channelgroup_item_fragment, container, false);
        edGroupItem_Name = (EditText)view.findViewById(R.id.edGroupItem_Name);
        edGroupItem_Name.setText(mGroupItem.getName());
        edGroup_Name_layout = (TextInputLayout)view.findViewById(R.id.edGroup_Name_layout);
        edGroupItem_Name.addTextChangedListener(textChangedListener);
        //edGroupItem_Name.setOnFocusChangeListener(onFocusChanged_listener);
        super.onCreateView(inflater,container,savedInstanceState);
        return view;
    }

    private  void setOptionsMenuVisibility(){
        if(mOptionsMenu!=null){
            MenuItem it_save = mOptionsMenu.findItem(R.id.action_item_save);
            MenuItem it_del = mOptionsMenu.findItem(R.id.action_item_delete);
            it_save.setVisible(mIsDataChanged&&edGroupItem_Name.getText().length()>1);
            it_del.setVisible(!mIsNewItem);
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.mOptionsMenu = menu;
        inflater.inflate(R.menu.menu_item_actions,mOptionsMenu);
        super.onCreateOptionsMenu(mOptionsMenu, inflater);
        setOptionsMenuVisibility();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_item_save: {
                saveItem();
                break;
            }
            case R.id.action_item_delete: {
                deleteItem();
                break;
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private  void saveItem() {
        updateItemFields();
        if (!isFillingOk()){
       //     edGroup_Name_layout.setErrorEnabled(false);
            return;
        }
        if(mIsNewItem){
            DataLab.get(getContext()).channelGroup_add(mGroupItem);
        }else {
            DataLab.get(getContext()).channelGroup_update(mGroupItem);
        }
        getActivity().finish();
    }

    private  boolean isFillingOk() {
        boolean isOk=true;
        if(mGroupItem.getName().length()<2) {
            isOk = false;
        }
        return isOk;
    }

    private  void deleteItem() {
        DataLab.get(getContext()).channelGroup_delete(mGroupItem);
        getActivity().finish();
    }

    private  void updateItemFields() {
        mGroupItem.setName(edGroupItem_Name.getText().toString());
     }

    private  TextWatcher textChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.e("FOCUS","view = "+s);
            if(s.length()<2){
               edGroup_Name_layout.setErrorEnabled(true);
               edGroup_Name_layout.setError("Не заполнено имя "+" (min 2 символа)");
            }else {
                //edGroup_Name_layout.setError("");
                edGroup_Name_layout.setErrorEnabled(false);
            }
            setOptionsMenuVisibility();
            mIsDataChanged=true;
            setOptionsMenuVisibility();
        }
    };
}
