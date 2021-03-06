package com.uplink.carins.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.uplink.carins.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chingment on 2017/12/22.
 */

public class CustomChooseListDialog extends Dialog {
    private View layoutRes;// 布局文件
    private Context context;
    private List<String> listChooseVals;

    private TextView txtTitle;
    private TextView txtTriggerView;
    private ListView listView;
    private CustomChooseListDiaglogListAdpater adapter;

    public TextView getTxtTtile(){
        return this.txtTitle;
    }

    public TextView getTriggerView(){
        return this.txtTriggerView;
    }

    public CustomChooseListDiaglogListAdpater getAdapter(){
        return this.adapter;
    }

    public ListView getListView(){
        return this.listView;
    }

    public void setTriggerView(TextView txtTriggerView){
        this.txtTriggerView=txtTriggerView;
    }


    public CustomChooseListDialog(Context context, String tips) {
        super(context,R.style.dialog_style);
        this.context = context;
        this.layoutRes = LayoutInflater.from(context).inflate(R.layout.dialog_chooselist, null);

        LayoutInflater inflater = LayoutInflater.from(context);
        txtTitle=(TextView)this.layoutRes.findViewById(R.id.dialog_title);
        listView=(ListView)this.layoutRes.findViewById(R.id.dialog_chooselist);


        List<String> items=new ArrayList<>();
        //items.add("1");
        //items.add("2");
        adapter = new CustomChooseListDiaglogListAdpater(inflater, items);
        listView.setAdapter(adapter);


        txtTitle.setText(tips);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (backListener != null) {
            backListener.onBackClick();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.setContentView(layoutRes);
    }

    public CustomEditTextDialog.OnDialogBackKeyDown backListener;

    public void setOnBackListener(CustomEditTextDialog.OnDialogBackKeyDown backListener) {
        this.backListener = backListener;
    }

    public interface OnDialogBackKeyDown{
        void onBackClick();
    }
}
