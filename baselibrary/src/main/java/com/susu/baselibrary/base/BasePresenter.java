package com.susu.baselibrary.base;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by junli on 9/8/16.
 */
public class BasePresenter {

//    public interface IPageView<T> {
//        public void onPageLoadSuccess(final PageOffset pageLoadInfo, PageListVO<T> data);
//        public void onPageLoadFail(final PageOffset pageLoadInfo, RetrofitException exception);
//    }

    List<Call> mFutureCalls = new ArrayList<>();

    protected void addCalls(Call call){
        mFutureCalls.add(call);
    }

    public void resume(){

    }

    public void pause(){

    }

    protected void enqueue(Call call, Callback callback) {
        call.enqueue(callback);
        addCalls(call);
    }


    public void cancel(Call call) {
        if (call != null) {
            call.cancel();
            mFutureCalls.remove(call);
        }
    }

    public void destroy(){
        cancelAllCalls();
    }

    private void cancelAllCalls() {
        for(Call call : mFutureCalls){
            call.cancel();
        }
        mFutureCalls.clear();
    }


}
