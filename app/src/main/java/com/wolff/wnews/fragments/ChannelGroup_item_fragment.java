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

    EditText edGroupItem_Name;
    TextInputLayout edGroup_Name_layout;

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
        edGroupItem_Name.addTextChangedListener(textChangedListener);
        edGroup_Name_layout = (TextInputLayout)view.findViewById(R.id.edTask_Name_layout);
        edGroupItem_Name.setOnFocusChangeListener(onFocusChanged_listener);
        super.onCreateView(inflater,container,savedInstanceState);
        return view;
    }

    public void setOptionsMenuVisibility(){
        if(mOptionsMenu!=null){
            MenuItem it_save = mOptionsMenu.findItem(R.id.action_item_save);
            MenuItem it_del = mOptionsMenu.findItem(R.id.action_item_delete);
            it_save.setVisible(mIsDataChanged);
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


    public void saveItem() {
        updateItemFields();
        if (!isFillingOk()){
            edGroup_Name_layout.setErrorEnabled(false);
            return;
        }
        if(mIsNewItem){
            DataLab.get(getContext()).channelGroup_add(mGroupItem);
        }else {
            DataLab.get(getContext()).channelGroup_update(mGroupItem);
        }
        getActivity().finish();
    }

    public boolean isFillingOk() {
        boolean isOk=true;
        if(mGroupItem.getName().isEmpty()) {
            isOk = false;
        }
        if(!isOk){
            return false;
        }
        return true;
    }

    public void deleteItem() {
        DataLab.get(getContext()).channelGroup_delete(mGroupItem);
        getActivity().finish();
    }

    public void updateItemFields() {
        mGroupItem.setName(edGroupItem_Name.getText().toString());
     }
/*    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            switch (requestCode){
                case DIALOG_REQUEST_PICTURE:
                    int idPict = data.getIntExtra(Picture_list_dialog.TAG_PICTURE_ID,-1);
                    mAccountItem.setIdPicture(idPict);
                    imAccountItem_Picture.setImageResource(idPict);
                    mIsDataChanged=true;
                    setOptionsMenuVisibility();
                    break;
                case DIALOG_REQUEST_CURRENCY:
                    WCurrency currency = (WCurrency) data.getSerializableExtra(TAG_CURRENCY_ID);
                    if(currency!=null) {
                        mAccountItem.setCurrency(currency);
                        imAccountItem_Currency.setText(currency.getName());
                        mIsDataChanged = true;
                        setOptionsMenuVisibility();
                    }else {
                        Log.e("RESULT"," NO CURRENCY");
                    }
                    break;
            }
        }
    }
    */
    public TextWatcher textChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mIsDataChanged=true;
            setOptionsMenuVisibility();
            Log.e("textChangedListener 1","afterTextChanged 1");
        }
    };
    View.OnFocusChangeListener onFocusChanged_listener = new View.OnFocusChangeListener(){

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            Log.e("FOCUS",""+v.getId()+" = "+hasFocus);
            switch (v.getId()){
                case R.id.edGroupItem_Name:
                    if(edGroupItem_Name.length()<2){
                        edGroup_Name_layout.setErrorEnabled(true);
                        //edOperation_Name_layout.setError("Не заполнено "+getResources().getString(R.string.operation_name_label)+" (min 2 символа)");
                        edGroup_Name_layout.setError("Не заполнено имя "+" (min 2 символа)");
                    }else {
                        edGroup_Name_layout.setErrorEnabled(false);
                    }
                    break;
            }
        }
    };
/*    View.OnClickListener onClick_Currency_listener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            DialogFragment dlgPict = new Currency_list_dialog();
            dlgPict.setTargetFragment(Account_item_fragment.this,DIALOG_REQUEST_CURRENCY);
            dlgPict.show(getFragmentManager(),dlgPict.getClass().getName());

        }
    };*/
}
