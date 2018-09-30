package cn.tangjiabin.biubiubiu;

import cn.tangjiabin.biubiubiu.util.StageUtil;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage){
        StageUtil.initialize(primaryStage);

    }


    public static void main(String[] args) {
        launch(args);
    }
}
