package com.example.anh.itenki.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.anh.itenki.R;

/**
 * Created by anh on 2017/12/19.
 */

public class AlarmNoteDialogFragment extends DialogFragment {
    private String content;
    private MediaPlayer mp;

    public AlarmNoteDialogFragment(String content) {
        this.content = content;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /** Turn Screen On and Unlock the keypad when this alert dialog is displayed */
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View alarmDialog = inflater.inflate(R.layout.custom_alarm_dialog, null);
        TextView txtContent = (TextView)alarmDialog.findViewById(R.id.txtAlarmContent);
        txtContent.setText(content);

        Uri notify = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mp = MediaPlayer.create(getContext(), notify);
        mp.start();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("");
        builder.setView(alarmDialog);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mp.stop();
                dialogInterface.dismiss();
            }
        });

        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mp.stop();
        getActivity().finish();
    }
}
