package com.susu.demoapplication.test.presenter;

import com.susu.baselibrary.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

public class BeautyNoReportManagerPresenter extends BasePresenter {

    private IView mView;

    public interface IView {
        void onStewardLoadSuccess(BeautyStewardNoReportVO data);

        void onStewardLoadFailed();

        void onSkinDetectCountSuccess(int count);

        void onSkinDetectCountFailed();
    }

    public BeautyNoReportManagerPresenter(IView iView) {
        mView = iView;
    }

    public void getProvinceCode() {
        getStewardNoReport();
    }

    public void getSkinDetectCount() {
        mView.onSkinDetectCountSuccess(1000);
    }


    private void getStewardNoReport() {
        BeautyStewardNoReportVO data = new BeautyStewardNoReportVO();
        data.setProvince("浙江");
        data.setPercent(0.8f);
        data.content = new ArrayList<>();
        data.content.add("12345");
        data.content.add("上山打老虎");
        data.content.add("老虎不在家");
        data.content.add("打到小松鼠");

        mView.onStewardLoadSuccess(data);

    }


    public class BeautyStewardNoReportVO {

        private String province;

        private float percent;

        private List<String> content;

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public float getPercent() {
            return percent;
        }

        public void setPercent(float percent) {
            this.percent = percent;
        }

        public List<String> getContent() {
            return content;
        }

        public void setContent(List<String> content) {
            this.content = content;
        }
    }
}
