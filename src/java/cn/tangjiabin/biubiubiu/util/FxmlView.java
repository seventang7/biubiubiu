package cn.tangjiabin.biubiubiu.util;

/**
 * 界面文件
 *
 * @author : J.Tang
 * @version : v1.0
 * @email : seven_tjb@163.com
 * @date : 2018-09-27
 **/

public enum  FxmlView {

    /**初始化*/
    INDEX{
        @Override
        public String getTitle() {
            return "biubiubiu";
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/main.fxml";
        }
    };

    public abstract String getTitle();
    public abstract String getFxmlFile();
}
