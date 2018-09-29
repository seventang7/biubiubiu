package cn.tangjiabin.biubiubiu.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * 窗口管理
 *
 * @author : J.Tang
 * @version : v1.0
 * @email : seven_tjb@163.com
 * @date : 2018-09-27
 **/

public class StageUtil {


    private final Stage primaryStage;

    private static StageUtil stageUtil;

    private StageUtil(Stage stage) {
        this.primaryStage = stage;
    }

    public synchronized static StageUtil initialize(Stage stage) {
        if (stageUtil == null) {
            stageUtil = new StageUtil(stage);
        }
        stageUtil.switchScene(FxmlView.INDEX);
        return stageUtil;
    }

    public static StageUtil getStageManager(){
        return stageUtil;
    }

    public void switchScene(final FxmlView view) {
        Parent parent = loadViewNodeHierarchy(view.getFxmlFile());
        show(parent, view.getTitle());
    }

    public void showScene(final FxmlView view){
        Scene scene = prepareScene(loadViewNodeHierarchy(view.getFxmlFile()));
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setTitle(view.getTitle());
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
        primaryStage.setScene(scene);

    }

    public Double getStageX(){
        return primaryStage.getX();
    }

    public Double getStageY(){
        return primaryStage.getY();
    }

    public void setStageXY(Double x,Double y){
        primaryStage.setX(x);
        primaryStage.setY(y);

    }

    public void miniStage(){
        primaryStage.setIconified(true);
    }



    private void show(final Parent rootnode, String title) {
        Scene scene = prepareScene(rootnode);

        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.setHeight(600);
        primaryStage.setWidth(400);
        try {
            primaryStage.show();
        } catch (Exception exception) {
        }
    }

    private Scene prepareScene(Parent rootnode) {
        Scene scene = primaryStage.getScene();

        if (scene == null) {
            scene = new Scene(rootnode);
        }
        scene.setRoot(rootnode);
        return scene;
    }

    /**
     * Loads the object hierarchy from a FXML document and returns to root node
     * of that hierarchy.
     *
     * @return Parent root node of the FXML document hierarchy
     */
    public Parent loadViewNodeHierarchy(String fxmlFilePath) {
        Parent rootNode = null;
        try {
            rootNode = FXMLLoader.load(getClass().getResource(fxmlFilePath));
        } catch (Exception exception) {
        }
        return rootNode;
    }


    public Stage getStage() {
        return this.primaryStage;
    }
}
