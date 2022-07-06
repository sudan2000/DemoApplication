package com.susu.baselibrary.updateversion;

import android.app.Activity;
import android.view.View;

import com.susu.baselibrary.utils.ui.DialogUtils;

/**
 * Author : sudan
 * Time : 2021/7/27
 * Description:
 */
public class SuggestUpdateHolder {
    private Activity mContext;
    private SuggestUpdatePresenter mPresenter;

    public SuggestUpdateHolder(SuggestUpdatePresenter mPresenter, Activity mContext) {
        this.mPresenter = mPresenter;
        this.mContext = mContext;
    }

    public void showDialog() {
        String positiveValue;
        if (mPresenter.hasDownload()) {
            positiveValue = "立即安装";
        } else {
            positiveValue = "立即升级";
        }
        DialogUtils.showCommonDialogCancelable(
                mContext,
                "发现新版本",
                mPresenter.message(),
                positiveValue,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.sureClick();
                    }
                },
                "稍后再说",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.cancelClick();
                    }
                },
                false

        );
    }
}
