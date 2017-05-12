package com.nshane.dualdialog.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.nshane.dualdialog.R;
import com.nshane.dualdialog.adapter.ThemeItemAdapter;
import com.nshane.dualdialog.bean.ThemeInfo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btn_res;
    private Button btn_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        btn_res = (Button) findViewById(R.id.btn_res);
        btn_adapter = (Button) findViewById(R.id.btn_res2);


        btn_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });


        btn_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showThemeDialog();
            }
        });

    }

    private void showThemeDialog() {
        List<ThemeInfo> themeList = initThemeData();

        ThemeItemAdapter adapter = new ThemeItemAdapter(this,
                themeList);
        AlertDialog.Builder builder
                = new AlertDialog.Builder(this);
        builder.setTitle("Select Theme")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                        }
                    }
                })
                .show();

    }


    private int mSelectionRes = 0; // res chosen by default


    private void showDialog() {
        String select[] = new String[]{"MEMO", "BAIDU", "TAOBAO"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Website");
        builder.setSingleChoiceItems(select, mSelectionRes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSelectionRes = which;  // redefine chosen item
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private List<ThemeInfo> initThemeData() {
        List<ThemeInfo> list = new ArrayList<>();
        ThemeInfo info_1 =
                new ThemeInfo(getResources().getString(R.string.theme_pink), R.drawable.background);
        ThemeInfo info_2 =
                new ThemeInfo(getResources().getString(R.string.theme_blue), R.drawable.bg_blue);
        ThemeInfo info_3 =
                new ThemeInfo(getResources().getString(R.string.theme_green), R.drawable.bg_green);

        list.add(info_1);
        list.add(info_2);
        list.add(info_3);
        return list;
    }


}
