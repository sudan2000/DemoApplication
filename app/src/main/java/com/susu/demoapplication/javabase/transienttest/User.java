package com.susu.demoapplication.javabase.transienttest;

import com.susu.demoapplication.javabase.staticandinner.Father;
import com.susu.demoapplication.javabase.staticandinner.Son;

import java.io.Serializable;

/**
 * Author : sudan
 * Time : 2021/12/9
 * Description:
 */
public class User implements Serializable {

    private String userName;

    private transient String password;

    private transient Son son;

    private Father father;

    private transient static String mUserSex;

    public String getUserSex() {
        return mUserSex;
    }

    public void setUserSex(String userSex) {
        mUserSex = userSex;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Son getSon() {
        return son;
    }

    public void setSon(Son son) {
        this.son = son;
    }

    public Father getFather() {
        return father;
    }

    public void setFather(Father father) {
        this.father = father;
    }
}
