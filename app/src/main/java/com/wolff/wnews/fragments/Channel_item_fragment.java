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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wolff.wnews.R;
import com.wolff.wnews.localdb.DataLab;
import com.wolff.wnews.model.WChannel;
import com.wolff.wnews.model.WChannelGroup;
import com.wolff.wnews.service.GetChannelInfo_Task;
import com.wolff.wnews.utils.DateUtils;
import com.wolff.wnews.utils.WriteChannelToLocalDB;

import java.util.concurrent.ExecutionException;

/**
 * Created by wolff on 13.07.2017.
 */

public class Channel_item_fragment extends Fragment {
    private static final String ARG_CHANNEL_ITEM = "WChannelItem";
    private WChannel mChannelItem;
    private boolean mIsNewItem;
    private boolean mIsDataChanged;
    private boolean mIsLinkChecked;
    private Menu mOptionsMenu;

    EditText edChannelItem_Name;
    EditText edChannelItem_Link;
    TextView tvChannelItem_Title;
    TextView tvChannelItem_Description;
    TextView tvChannelItem_PubDate;
    Button btnGetChannel;
    TextInputLayout edChannel_Name_layout;

    public static Channel_item_fragment newIntance(WChannel item){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CHANNEL_ITEM,item);
        Channel_item_fragment fragment = new Channel_item_fragment();
        fragment.setArguments(args);
        return fragment;

    }

    public Channel_item_fragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mChannelItem = (WChannel) getArguments().getSerializable(ARG_CHANNEL_ITEM);
        if(mChannelItem==null){
            mChannelItem = new WChannel();
            mIsNewItem=true;
            mIsLinkChecked=false;
        }else {
            mIsLinkChecked=true;
            mIsNewItem=false;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.channel_item_fragment, container, false);
        edChannelItem_Name = (EditText)view.findViewById(R.id.edChannelItem_Name);
        edChannel_Name_layout = (TextInputLayout)view.findViewById(R.id.edChannel_Name_layout);
        edChannelItem_Link = (EditText)view.findViewById(R.id.edChannelItem_Link);
        tvChannelItem_Title = (TextView)view.findViewById(R.id.tvChannelItem_Title);
        tvChannelItem_Description = (TextView)view.findViewById(R.id.tvChannelItem_Description);
        tvChannelItem_PubDate = (TextView)view.findViewById(R.id.tvChannelItem_PubDate);
        btnGetChannel = (Button)view.findViewById(R.id.btnGetChannel);

        edChannelItem_Name.setText(mChannelItem.getName());
        edChannelItem_Link.setText(mChannelItem.getLink());
        tvChannelItem_Title.setText(mChannelItem.getTitle());
        tvChannelItem_Description.setText(mChannelItem.getDescription());
        tvChannelItem_PubDate.setText(new DateUtils().dateToString(mChannelItem.getPubDate(),DateUtils.DATE_FORMAT_VID));

        edChannelItem_Name.addTextChangedListener(textChangedListener);
        edChannelItem_Link.addTextChangedListener(textChangedListenerLink);
        btnGetChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnGetChannel.setEnabled(false);
                mIsNewItem = DataLab.get(getContext()).getWChannelByLink(edChannelItem_Link.getText().toString()) == null;
                try {
                    mChannelItem = new GetChannelInfo_Task().execute(edChannelItem_Link.getText().toString()).get();
                    if(mChannelItem!=null) {
                        mIsLinkChecked = true;
                        tvChannelItem_Title.setText(mChannelItem.getTitle());
                        tvChannelItem_Description.setText(mChannelItem.getDescription());
                        tvChannelItem_PubDate.setText(new DateUtils().dateToString(mChannelItem.getPubDate(), DateUtils.DATE_FORMAT_VID));
                        //Log.e("GET CHANNEL", "OK");
                        setOptionsMenuVisibility();
                    }else {
                        mIsLinkChecked=false;
                        //Log.e("GET CHANNEL", "NULL");
                    }
                } catch (InterruptedException e) {
                    mIsLinkChecked=false;
                    //Log.e("GET CHANNEL","ERROR "+e.getLocalizedMessage());
                } catch (ExecutionException e) {
                    mIsLinkChecked=false;
                    //Log.e("GET CHANNEL","ERROR 2 "+e.getLocalizedMessage());
                }
                //Log.e("textChangedListener 1","afterTextChanged 1");
                btnGetChannel.setEnabled(true);
            }
        });
        super.onCreateView(inflater,container,savedInstanceState);
        return view;
    }

    public void setOptionsMenuVisibility(){
        if(mOptionsMenu!=null){
            MenuItem it_save = mOptionsMenu.findItem(R.id.action_item_save);
            MenuItem it_del = mOptionsMenu.findItem(R.id.action_item_delete);
            it_save.setVisible(mIsLinkChecked&&mIsDataChanged&&edChannelItem_Name.getText().length()>1);
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
            return;
        }
        if(mIsNewItem){
            DataLab.get(getContext()).channel_add(mChannelItem);
        }else {
            DataLab.get(getContext()).channel_update(mChannelItem);
        }
        getActivity().finish();
    }

    public boolean isFillingOk() {
        boolean isOk=true;
        if(mChannelItem.getName().length()<2) {
            isOk = false;
        }
        return isOk;
    }

    public void deleteItem() {
        DataLab.get(getContext()).channel_delete(mChannelItem);
        getActivity().finish();
    }

    public void updateItemFields() {
        mChannelItem.setName(edChannelItem_Name.getText().toString());
        //mChannelItem.setTitle(tvChannelItem_Title.getText().toString());
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
    private void checkFieldsFilling(View v){
        switch (v.getId()){
            case R.id.edGroupItem_Name:
                if(edChannelItem_Name.getText().length()<2){
                    edChannel_Name_layout.setErrorEnabled(true);
                    //edOperation_Name_layout.setError("Не заполнено "+getResources().getString(R.string.operation_name_label)+" (min 2 символа)");
                    edChannel_Name_layout.setError("Не заполнено имя "+" (min 2 символа)");
                }else {
                    edChannel_Name_layout.setErrorEnabled(false);
                }
                break;
        }
        setOptionsMenuVisibility();
    }
    public TextWatcher textChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkFieldsFilling(edChannelItem_Name);
        }

        @Override
        public void afterTextChanged(Editable s) {
            mIsDataChanged=true;
            setOptionsMenuVisibility();
            //Log.e("textChangedListener 1","afterTextChanged 1");
        }
    };
    public TextWatcher textChangedListenerLink = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkFieldsFilling(edChannelItem_Name);
        }

        @Override
        public void afterTextChanged(Editable s) {
            //Log.e("afretChanged", "s = " + s.toString());
            mIsDataChanged = true;
            setOptionsMenuVisibility();
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
