package cn.tangjiabin.biubiubiu.controller;

import cn.tangjiabin.biubiubiu.bean.Bullet;
import cn.tangjiabin.biubiubiu.bean.Enemy;
import cn.tangjiabin.biubiubiu.util.StageUtil;
import cn.tangjiabin.biubiubiu.util.ThreadPool;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;


/**
 * @author J.Tang
 */
public class Controller implements Initializable {


    @FXML
    private AnchorPane gamePanel;

    /**
     * 窗体
     */
    private Stage stage;

    /**
     * 分数图标
     */
    private ImageView fractionImageView;

    /**
     * 分数
     */
    private int fraction;

    /**
     * 分数Label
     */
    private Label fractionLabel;

    /**
     * 背景图
     */
    private ImageView bgImgView;

    /**
     * 飞机图
     */
    private ImageView aircraftImageView;

    /**
     * 子弹加载控制
     */
    private boolean bulletLoad = false;

    /**
     * 子弹数量
     */
    private int bulletNumber = 50;

    /**
     * 子弹图片集合
     */
    private List<Bullet> bulletImageViewList;

    /**
     * 敌机加载控制
     */
    private boolean enemyLoad = false;

    /**
     * 敌机数量
     */
    private int enemyNumber = 5;

    /**
     * 敌机速度
     */
    private int enemySpeed = 10;

    /**
     * 敌机集合
     */
    private List<Enemy> enemyViewList;

    /**
     * 飞机爆炸效果图
     */
    private ImageView blastImageView;

    /**
     * 飞机移动速度
     */
    private int aircraftSpeed = 10;

    /**
     * 子弹密度
     */
    private int bulletDensity = 500;

    /**
     * 子弹移动速度
     */
    private int bulletVelocity = 5;

    /**
     * 游戏结束
     */
    private ImageView gameOverImageView;

    /**
     * 游戏开始
     */
    private ImageView startImageView;

    /**
     * 按键监听
     */
    private boolean keyMonitor = false;

    /**
     * 碰撞验证
     */
    private boolean collision = false;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //获取窗口
        stage = StageUtil.getStageManager().getStage();

        //监听窗口关闭
        stage.setOnCloseRequest(event -> System.exit(0));

        //加载图片
        imgLoad();

        //加载背景
        backgroundLoad();

        //加载飞机
        aircraftLoad();

        //监听按键
        stage.addEventHandler(KeyEvent.KEY_PRESSED, this::keyMonitor);

        //开始按钮监听
        startImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> startGame());


    }

    /**
     * 开始
     */
    private void startGame() {

        gamePanel.getChildren().remove(startImageView);

        Platform.runLater(() -> gamePanel.getChildren().remove(gameOverImageView));

        bulletLoad = true;

        enemyLoad = true;

        keyMonitor = true;

        collision = true;

        fraction = 0;

        //加载子弹
        ThreadPool.getInstance().execute(this::bulletLoad);

        //子弹监听
        ThreadPool.getInstance().execute(this::bulletMonitor);

        //加载敌机
        ThreadPool.getInstance().execute(this::enemyLoad);

        //敌机运动
        ThreadPool.getInstance().execute(this::enemyMonitor);

        //碰撞检测
        ThreadPool.getInstance().execute(this::collisionDetection);

        //分数监听
        ThreadPool.getInstance().execute(this::scoreMonitor);
    }

    /**
     * 加载图片文件
     */
    private void imgLoad() {

        //加载背景图
        Image bgImage = new Image("/img/bg.jpg");
        bgImgView = new ImageView(bgImage);
        bgImgView.setLayoutY(0);
        bgImgView.setLayoutX(0);
        bgImgView.setFitWidth(400);
        bgImgView.setFitHeight(600);

        //加载飞机图片
        Image aircraft = new Image("/img/feiji.png");
        aircraftImageView = new ImageView();
        aircraftImageView.setImage(aircraft);
        aircraftImageView.setFitHeight(40);
        aircraftImageView.setFitWidth(70);
        aircraftImageView.setLayoutX(165);
        aircraftImageView.setLayoutY(500);

        //加载子弹图片
        Image bulletImage = new Image("/img/zidan.png");
        bulletImageViewList = Collections.synchronizedList(new ArrayList<>());
        ImageView bulletImageView;
        Bullet bullet;
        for (int i = 0; i < bulletNumber; i++) {

            bullet = new Bullet();
            bulletImageView = new ImageView();
            bulletImageView.setImage(bulletImage);
            bulletImageView.setFitWidth(20);
            bulletImageView.setFitHeight(20);
            bullet.setBulletImageView(bulletImageView);
            bullet.setPower(1);
            bullet.setState(0);

            bulletImageViewList.add(bullet);
        }

        //加载敌机图片
        List<String> imgList = Collections.synchronizedList(new ArrayList<>());
        imgList.add("/img/enemy1.png");
        imgList.add("/img/enemy2.png");
        imgList.add("/img/enemy3.png");
        imgList.add("/img/enemy4.png");
        imgList.add("/img/enemy5.png");
        imgList.add("/img/enemy6.png");

        enemyViewList = Collections.synchronizedList(new ArrayList<>());
        Enemy enemy;
        ImageView enemyView;

        for (String img : imgList) {
            enemy = new Enemy();

            Image enemyImg = new Image(img);
            enemyView = new ImageView(enemyImg);
            enemyView.setFitHeight(40);
            enemyView.setFitWidth(50);

            enemy.setEnemyImageView(enemyView);
            enemy.setEnemyState(0);
            enemy.setHealthPoint(1);

            enemyViewList.add(enemy);
        }

        //加载爆炸图片
        Image blastImage = new Image("/img/blast.png");
        blastImageView = new ImageView(blastImage);
        blastImageView.setFitWidth(40);
        blastImageView.setFitHeight(40);

        //加载游戏开始图
        Image startImage = new Image("/img/start.jpg");
        startImageView = new ImageView(startImage);
        startImageView.setFitHeight(80);
        startImageView.setFitWidth(160);
        startImageView.setLayoutY(260);
        startImageView.setLayoutX(120);
        gamePanel.getChildren().add(startImageView);

        //加载游戏结束图片
        Image gameOverImage = new Image("/img/gameover.jpg");
        gameOverImageView = new ImageView(gameOverImage);
        gameOverImageView.setFitHeight(80);
        gameOverImageView.setFitWidth(160);
        gameOverImageView.setLayoutY(260);
        gameOverImageView.setLayoutX(120);

        //加载分数图标
        Image fractionImage = new Image("/img/start.PNG");
        fractionImageView = new ImageView(fractionImage);
        fractionImageView.setFitWidth(30);
        fractionImageView.setFitHeight(30);
        fractionImageView.setLayoutX(10);
        fractionImageView.setLayoutY(10);

        //加载分数标签
        fractionLabel = new Label();
        fraction = 0;
        fractionLabel.setText(String.valueOf(fraction));
        fractionLabel.setFont(new Font("Arial", 25));
        fractionLabel.setLayoutX(60);
        fractionLabel.setLayoutY(10);
        fractionLabel.setTextFill(Color.WHITE);
        gamePanel.getChildren().add(1, fractionLabel);
        gamePanel.getChildren().add(fractionImageView);

    }

    /**
     * 加载背景
     */
    private void backgroundLoad() {
        gamePanel.getChildren().add(0, bgImgView);
    }

    /**
     * 按键监听
     */
    private void keyMonitor(KeyEvent event) {
        switch (event.getCode()) {
            case UP:
                if (keyMonitor) {
                    if (aircraftImageView.getLayoutY() > 0) {
                        aircraftImageView.setLayoutY(aircraftImageView.getLayoutY() - aircraftSpeed);
                    }
                }
                break;
            case DOWN:
                if (keyMonitor) {
                    if (aircraftImageView.getLayoutY() < 530) {
                        aircraftImageView.setLayoutY(aircraftImageView.getLayoutY() + aircraftSpeed);
                    }
                }
                break;
            case LEFT:
                if (keyMonitor) {
                    if (aircraftImageView.getLayoutX() > 0) {
                        aircraftImageView.setLayoutX(aircraftImageView.getLayoutX() - aircraftSpeed);
                    }
                }
                break;
            case RIGHT:
                if (keyMonitor) {

                    if (aircraftImageView.getLayoutX() < 400) {
                        aircraftImageView.setLayoutX(aircraftImageView.getLayoutX() + aircraftSpeed);
                    }
                }
                break;
            case ESCAPE:
                System.exit(0);
                break;
            case Q:
                startGame();
                break;
            default:
                break;
        }

    }

    /**
     * 加载飞机
     */
    private void aircraftLoad() {
        gamePanel.getChildren().add(aircraftImageView);
    }

    /**
     * 加载子弹
     */
    private void bulletLoad() {

        while (bulletLoad) {

            for (Bullet bullet : bulletImageViewList) {

                if (bullet.getState() == 0) {
                    ImageView bulletImageView = bullet.getBulletImageView();
                    bulletImageView.setLayoutX(aircraftImageView.getLayoutX());
                    bulletImageView.setLayoutY(aircraftImageView.getLayoutY());
                    Platform.runLater(() -> gamePanel.getChildren().add(bulletImageView));
                    bullet.setState(1);
                    break;
                }
            }
            try {
                Thread.sleep(bulletDensity);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 子弹运行监测
     */
    private void bulletMonitor() {
        while (bulletLoad) {

            for (Bullet bullet : bulletImageViewList) {
                if (bullet.getState() == 1) {
                    ImageView bulletImageView = bullet.getBulletImageView();
                    bulletImageView.setLayoutY(bulletImageView.getLayoutY() - 1);
                    if (bulletImageView.getLayoutY() <= 0) {
                        bullet.setState(0);
                        Platform.runLater(() -> gamePanel.getChildren().remove(bulletImageView));
                    }
                }
            }

            try {
                Thread.sleep(bulletVelocity);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 加载敌机
     */
    private void enemyLoad() {

        Random random = new Random();
        int enemyPosition = 0;
        int generateNumber;
        Enemy enemy;

        while (enemyLoad) {

            generateNumber = random.nextInt(enemyNumber);

            for (int i = 0; i < generateNumber; i++) {
                enemy = enemyViewList.get(random.nextInt(enemyViewList.size() - 1));
                enemyPosition = random.nextInt(400 - 50);
                if (enemy.getEnemyState() == 0) {
                    enemy.setEnemyState(1);
                    ImageView enemyImageView = enemy.getEnemyImageView();
                    enemyImageView.setLayoutY(0);
                    enemyImageView.setLayoutX(enemyPosition);
                    Platform.runLater(() -> gamePanel.getChildren().add(enemyImageView));
                }

            }

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 监听敌机
     */
    private void enemyMonitor() {
        while (enemyLoad) {

            for (Enemy enemy : enemyViewList) {
                ImageView enemyImageView = enemy.getEnemyImageView();

                if (enemy.getEnemyState() == 1) {
                    if (enemyImageView.getLayoutY() < 550) {
                        Platform.runLater(() -> enemyImageView.setLayoutY(enemyImageView.getLayoutY() + 1.5));
                    } else {
                        enemy.setEnemyState(0);
                        Platform.runLater(() -> gamePanel.getChildren().remove(enemyImageView));
                    }
                }
            }

            try {
                Thread.sleep(enemySpeed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 碰撞检测
     */
    private void collisionDetection() {

        while (collision) {

            for (Enemy enemy : enemyViewList) {

                if (enemy.getEnemyState() == 1) {

                    ImageView enemyView = enemy.getEnemyImageView();

                    //敌机高
                    double height = enemyView.getLayoutY();
                    double height1 = enemyView.getLayoutY() + 40;
                    //敌机宽
                    double width = enemyView.getLayoutX();
                    double width1 = enemyView.getLayoutX() + 50;

                    for (Bullet bullet : bulletImageViewList) {

                        if (bullet.getState() == 1) {

                            ImageView bulletView = bullet.getBulletImageView();

                            //子弹位置
                            double bulletY = bulletView.getLayoutY();
                            double bulletX = bulletView.getLayoutX();
                            //验证相撞
                            boolean vheight = height <= bulletY && bulletY <= height1;
                            boolean vwidth = width <= bulletX && bulletX <= width1;

                            if (vheight && vwidth) {

                                //移除子弹
                                bullet.setState(0);
                                Platform.runLater(() -> gamePanel.getChildren().remove(bulletView));

                                //设置敌机爆炸效果
                                blastImageView.setLayoutY(enemyView.getLayoutY());
                                blastImageView.setLayoutX(enemyView.getLayoutX());
                                Platform.runLater(() -> {
                                    gamePanel.getChildren().remove(blastImageView);
                                    gamePanel.getChildren().add(blastImageView);
                                });

                                enemy.setEnemyState(0);
                                Platform.runLater(() -> gamePanel.getChildren().remove(enemyView));

                                //添加分数
                                fraction++;
                                Platform.runLater(() -> fractionLabel.setText(String.valueOf(fraction)));

                                //半秒后移除爆炸效果
                                ThreadPool.getInstance().execute(() -> {
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    Platform.runLater(() -> {
                                        gamePanel.getChildren().remove(blastImageView);
                                    });
                                });

                            }

                        }
                    }

                    if (collision) {

                        double layoutY1 = aircraftImageView.getLayoutY();
                        double layoutY2 = aircraftImageView.getLayoutY() + 40;
                        double layoutY3 = aircraftImageView.getLayoutY() + 20;
                        double layoutX1 = aircraftImageView.getLayoutX();
                        double layoutX2 = aircraftImageView.getLayoutX() + 70;
                        double layoutX3 = aircraftImageView.getLayoutX() + 35;

                        //左上点
                        boolean luh = height <= layoutY1 && layoutY1 <= height1;
                        boolean luw = width <= layoutX1 && layoutX1 <= width1;

                        //右上点
                        boolean ruh = height <= layoutY1 && layoutY1 <= height1;
                        boolean ruw = width <= layoutX2 && layoutX2 <= width1;

                        //左下点
                        boolean llh = height <= layoutY2 && layoutY2 <= height1;
                        boolean llw = width <= layoutX1 && layoutX1 <= width1;

                        //右下点
                        boolean rlh = height <= layoutY2 && layoutY2 <= height1;
                        boolean rlw = width <= layoutX2 && layoutX2 <= width1;

                        //中间点
                        boolean mh = height <= layoutY3 && layoutY3 <= height1;
                        boolean mw = width <= layoutX3 && layoutX3 <= width1;

                        boolean upperLeft = luh && luw;

                        boolean lowerLeft = llh && llw;

                        boolean upperRight = ruh && ruw;

                        boolean lowerRight = rlh && rlw;

                        boolean middle = mh && mw;

                        if (upperLeft || lowerLeft || upperRight || lowerRight || middle) {
                            bulletLoad = false;
                            enemyLoad = false;
                            keyMonitor = false;
                            collision = false;
                            Platform.runLater(() -> gamePanel.getChildren().add(gameOverImageView));

                            //清空子弹
                            for (Bullet bullet : bulletImageViewList) {
                                bullet.setState(0);
                                Platform.runLater(() -> gamePanel.getChildren().remove(bullet.getBulletImageView()));
                            }

                            //清空敌机
                            for (Enemy enemy1 : enemyViewList) {
                                enemy1.setEnemyState(0);
                                Platform.runLater(() -> gamePanel.getChildren().remove(enemy1.getEnemyImageView()));
                            }
                        }
                    }
                }
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 分数监听
     */
    private void scoreMonitor() {

        int grade = 10;

        while (true) {

            if (fraction > grade) {
                grade = grade * 2;
                enemySpeed--;
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
