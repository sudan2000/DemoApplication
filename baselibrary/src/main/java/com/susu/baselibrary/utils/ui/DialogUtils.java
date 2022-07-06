package com.susu.baselibrary.utils.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.StyleRes;

import com.susu.baselibrary.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author : sudan
 * Time : 2020/12/9
 * Description:
 */
public class DialogUtils {


    public static void showCommonDialog(Context context, String msg, int posButton, int negButton,
                                        DialogInterface.OnClickListener posListener,
                                        DialogInterface.OnClickListener negListener) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage(msg)
                .setPositiveButton(posButton, posListener)
                .setNegativeButton(negButton, negListener).create();
        dialog.show();
    }

    /**
     * 只有一个按钮的dialog
     */
    public static void showCommonSingleDialog(Activity context, String title, String msg, String buttonTxt, final View.OnClickListener onButtonClick) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context, R.style.BaseCustomDialog);
        final android.app.AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
        alert.show();
        Window window = alert.getWindow();

        window.setContentView(R.layout.base_dialog_common_view_single);

        TextView titleView = window.findViewById(R.id.dialog_common_view_title_text);
        View titleLayout = window.findViewById(R.id.dialog_common_view_title_layout);
        if (title != null && !title.equals("")) {
            titleLayout.setVisibility(View.VISIBLE);
            titleView.setText(title);
        } else {
            titleLayout.setVisibility(View.GONE);
        }

        TextView message = window.findViewById(R.id.dialog_common_view_message_text);
        message.setText(msg);

        final Button leftButton = window.findViewById(R.id.dialog_common_view_button);
        leftButton.setText(buttonTxt);
        leftButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (onButtonClick != null) {
                    onButtonClick.onClick(v);
                }
                alert.cancel();
            }
        });
    }

    /**
     * 两个按钮的dialog
     */
    public static androidx.appcompat.app.AlertDialog showCommonDialogCancelable(
            Activity context,
            String titleString,
            String messageString,
            String buttonLeftString,
            final View.OnClickListener onLeftClick,
            String buttonRightString,
            final View.OnClickListener onRightClick,
            boolean cancelable) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context, R.style.BaseCustomDialog);
        final androidx.appcompat.app.AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(cancelable);
        alert.setCancelable(cancelable);
        alert.show();
        Window window = alert.getWindow();
        window.setContentView(R.layout.base_dialog_common_view_double);

        TextView titleView = window.findViewById(R.id.dialog_common_view_title_text);
        View titleLayout = window.findViewById(R.id.dialog_common_view_title_layout);
        View titleLine = window.findViewById(R.id.dialog_common_view_title_line);
        if (!TextUtils.isEmpty(titleString)) {
            titleLayout.setVisibility(View.VISIBLE);
            titleLine.setVisibility(View.VISIBLE);
            titleView.setText(titleString);
        } else {
            titleLayout.setVisibility(View.GONE);
            titleLine.setVisibility(View.GONE);
        }

        TextView message = window.findViewById(R.id.dialog_common_view_message_text);
        message.setText(messageString);

        Button leftButton = window.findViewById(R.id.dialog_common_view_button_left);
        leftButton.setText(buttonLeftString);
        leftButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onLeftClick != null) {
                    onLeftClick.onClick(v);
                }
                alert.cancel();
            }
        });

        Button rightButton = (Button) window.findViewById(R.id.dialog_common_view_button_right);
        rightButton.setText(buttonRightString);
        rightButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onRightClick != null) {
                    onRightClick.onClick(v);
                }
                alert.cancel();
            }
        });

        return alert;
    }
}
