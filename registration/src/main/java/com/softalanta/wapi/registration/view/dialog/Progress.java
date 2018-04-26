package com.softalanta.wapi.registration.view.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.softalanta.wapi.registration.R;

/**
 * Created by chris on 4/8/18.
 */

public class Progress {

    private View view;
    private TextView msg;
    private ProgressBar progressBar;
    private LinearLayout ll;
    private AlertDialog.Builder builder;
    private Dialog dialog;

    public Progress(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.progress, null);
        init(context);
    }

    void init(Context context) {
        msg = (TextView) view.findViewById(R.id.msg);
        progressBar = ((ProgressBar) view.findViewById(R.id.loader));
        ll = ((LinearLayout) view.findViewById(R.id.ll));
        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
    }

    public void defaultProgress(String message) {
        setBackgroundColor(view.getResources().getColor(R.color.wapi_white));
        setProgressColor(view.getResources().getColor(R.color.colorPrimary));
        setMessageColor(view.getResources().getColor(R.color.colorPrimary));
        setMessage(message);
        show();
    }


    public Progress setBackgroundDrawable(Drawable drawable) {
        ll.setBackgroundDrawable(drawable);
        ll.setPadding(30, 30, 30, 30);
        return this;
    }

    public Progress setBackgroundColor(int color) {
        ll.setBackgroundColor(color);
        return this;
    }

    public Progress setProgressColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.setIndeterminateTintList(ColorStateList.valueOf(color));
        }
        return this;
    }

    public Progress setMessage(String message) {
        msg.setText(message);
        return this;
    }

    public Progress setMessageColor(int color) {
        msg.setTextColor(color);
        return this;
    }

    public void show() {
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
