package cn.tangjiabin.biubiubiu.bean;


import javafx.scene.image.ImageView;

/**
 * 敌机
 *
 * @author : J.Tang
 * @version : v1.0
 * @email : seven_tjb@163.com
 * @date : 2018-09-28
 **/

public class Enemy {

    private ImageView enemyImageView;

    private Integer healthPoint;

    private Integer enemyState;

    public ImageView getEnemyImageView() {
        return enemyImageView;
    }

    public void setEnemyImageView(ImageView enemyImageView) {
        this.enemyImageView = enemyImageView;
    }

    public Integer getHealthPoint() {
        return healthPoint;
    }

    public void setHealthPoint(Integer healthPoint) {
        this.healthPoint = healthPoint;
    }

    public Integer getEnemyState() {
        return enemyState;
    }

    public void setEnemyState(Integer enemyState) {
        this.enemyState = enemyState;
    }
}
