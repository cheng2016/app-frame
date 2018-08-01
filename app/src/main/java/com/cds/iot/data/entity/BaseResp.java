package com.cds.iot.data.entity;

public class BaseResp {

    /**
     * data : {"user_id":21}
     * info : {"code":"200","info":"成功"}
     */

    private DataBean data;
    private BaseInfo info;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public BaseInfo getInfo() {
        return info;
    }

    public void setInfo(BaseInfo info) {
        this.info = info;
    }

    public static class DataBean {
        /**
         * user_id : 21
         */

        private int user_id;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }
    }
}
