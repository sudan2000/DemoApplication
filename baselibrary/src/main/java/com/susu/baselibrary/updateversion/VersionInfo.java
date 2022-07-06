package com.susu.baselibrary.updateversion;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Author : sudan
 * Time : 2021/7/27
 * Description:
 */
public class VersionInfo implements Serializable {
    private static final long serialVersionUID = -1766013568161897130L;

    public static final int LEVEL_NONE = 0;
    public static final int LEVEL_MUST = 1;
    public static final int LEVEL_SUGGEST = 2;


    public String downUrl;
    public String desc;
    public int versionCode;
    public String messageDigest;
    public int cueLevel;
    public String versionName;
    public boolean hasDownload;

    public VersionInfo(JSONObject object) throws Exception {
        super();
        downUrl = object.optString("downUrl", "");
        desc = object.optString("desc", "");
        versionCode = object.optInt("versionCode", 0);
        messageDigest = object.optString("messageDigest", "");
        cueLevel = object.optInt("cueLevel", LEVEL_NONE);
        versionName = object.optString("versionName", "");
    }


}
