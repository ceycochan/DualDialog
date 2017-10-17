package com.nshane.dualdialog.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nshane.dualdialog.R;
import com.nshane.dualdialog.adapter.ThemeItemAdapter;
import com.nshane.dualdialog.bean.ThemeInfo;
import com.nshane.dualdialog.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btn_res;
    private Button btn_adapter;
    private Button btn_multi;
    private TextView tv_show;
    private Button btn_base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        btn_res = (Button) findViewById(R.id.btn_res);
        btn_adapter = (Button) findViewById(R.id.btn_res2);
        btn_multi = (Button) findViewById(R.id.btn_res3);
        btn_base = (Button) findViewById(R.id.btn_base_dialog);
        tv_show = (TextView) findViewById(R.id.tv_show);

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


        btn_multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initMultiDialog();
            }
        });

        btn_base.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDialog();
            }
        });

    }

    private void initMultiDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择你的爱好");
        final String[] hobbies = getResources().getStringArray(R.array.dialog_items2);
        final boolean[] checkedItems = new boolean[hobbies.length];
        builder.setMultiChoiceItems(R.array.dialog_items2, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                checkedItems[which] = isChecked;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < checkedItems.length; i++) {
                    if (checkedItems[i]) {
                        sb.append(hobbies[i]).append(" ");
                    }
                }
                tv_show.setText(sb.toString());
            }
        });
        builder.create().show();
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


    private int mSelectionRes = -1; // res chosen by default  //-1: null chosen when 1st time chose


    private void showDialog() {
//        String select[] = new String[]{"MEMO", "BAIDU", "TAOBAO"};
        String select[] = getResources().getStringArray(R.array.dialog_items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Website")
                .setSingleChoiceItems(select, mSelectionRes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelectionRes = which;  // redefine chosen item, and loging
                        Intent intent = new Intent();
                        if (which == 3) {
                            /**
                             *   for general android devices using
                             */
//                            intent.setAction("android.settings.SETTINGS");
//                            startActivity(intent);

                            //for leanback only
                            Utils.startAPP(getApplicationContext(), "com.android.tv.settings");
//
                            Toast.makeText(MainActivity.this, "haha", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
        builder.create().show();
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


    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView tv = new TextView(this);
        tv.setText("Title");
        tv.setTextSize(22);
        tv.setPadding(30, 20, 10, 10);
        tv.setTextColor(Color.parseColor("#E22018"));
        builder.setCustomTitle(tv).setMessage("Title的颜色已经被改变")
                .setPositiveButton("牛逼", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "确认了", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "取消了", Toast.LENGTH_SHORT).show();
            }
        }).create().show();

    }


}
