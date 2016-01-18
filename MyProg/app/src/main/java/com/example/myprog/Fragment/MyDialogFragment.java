package com.example.myprog.Fragment;

/**
 * Created by Христинка on 22.12.2015.
 */

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.myprog.R;
import com.example.myprog.list.Cur;
import com.example.myprog.list.Org;

import java.util.List;

public class MyDialogFragment extends DialogFragment implements View.OnClickListener {

    final String LOG_TAG = "myLogs";
    private List<Cur> curs;
    private Org org;
    ImageView imageShare;
    LinearLayout mLinearLayout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.detail_share_dialog, null);
        imageShare = (ImageView)v.findViewById(R.id.image);
        v.findViewById(R.id.buttonShare_DF).setOnClickListener(this);
        return v;
    }

    public void onClick(View v) {
        Log.d(LOG_TAG, "Dialog 1: " + ((Button) v).getText());
        dismiss();
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(LOG_TAG, "Dialog 1: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(LOG_TAG, "Dialog 1: onCancel");
    }



}
