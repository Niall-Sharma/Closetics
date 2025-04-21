package com.cs309.testing.Presenter;

import android.content.Context;

import com.cs309.testing.Model.IVolleyListener;
import com.cs309.testing.Model.RequestServerForService;
import com.cs309.testing.View.TestingView;

public class MainActivityPresenter implements IVolleyListener {
    private RequestServerForService model;
    private TestingView view;
    private Context context;

    public MainActivityPresenter(TestingView view, Context c) {
        this.view    = view;
        this.context = c;
        this.model   = new RequestServerForService(c, this);
    }

    public void setModel(RequestServerForService m) { this.model = m;}

    /**
     * Updates server which will reverse the string
     * @param str
     */
    public void revStr(String str){
        view.showProgressBar();
        model.reverse(str);
    }

    /**
     * Updates server which will capitalize the string
     * @param str
     */
    public void capStr(String str){
        view.showProgressBar();
        model.capitalize(str);
    }


    @Override
    public void onSuccess(String s) {
        view.hideProgressBar();
        view.updateUserInfoTextView(s);
    }

    @Override
    public void onError(String s) {
        view.hideProgressBar();
        view.updateUserInfoTextView("Error:" + s);
    }
}
