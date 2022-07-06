package com.susu.baselibrary.location

/**
 * Author : sudan
 * Time : 2021/11/23
 * Description: 百度定位常量
 */

object LocationConstants {

    const val BAIDU_APP_KEY = "C2Cc4W5q5Fk2XRWrVPPHh1MqBXgmAyPm"

    const val BAIDU_TRACK_SERVICE_ID = 230490 // 鹰眼服务service_id

    const val TAG = "BaiduTraceSDK_V3"

    const val REQUEST_CODE = 1

    const val RESULT_CODE = 1

    const val DEFAULT_RADIUS_THRESHOLD = 0

    const val PAGE_SIZE = 5000

    /**
     * 轨迹分析查询间隔时间（1分钟）
     */
    const val ANALYSIS_QUERY_INTERVAL = 60

    /**
     * 停留点默认停留时间（1分钟）
     */
    const val STAY_TIME = 60

    /**
     * 启动停留时间
     */
    const val SPLASH_TIME = 3000

    /**
     * 默认采集周期
     */
    const val DEFAULT_GATHER_INTERVAL = 5

    /**
     * 默认打包周期
     */
    const val DEFAULT_PACK_INTERVAL = 30

    /**
     * 实时定位间隔(单位:秒)
     */
    const val LOC_INTERVAL = 10

    /**
     * 最后一次定位信息
     */
    const val LAST_LOCATION = "last_location"

    /**
     * 权限说明标示Key
     */
    const val PERMISSIONS_DESC_KEY = "permission_desc"



}