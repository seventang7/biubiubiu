package cn.tangjiabin.biubiubiu.bean;

import javafx.scene.image.ImageView;

/**
 * 子弹类
 *
 * @author : J.Tang
 * @version : v1.0
 * @email : seven_tjb@163.com
 * @date : 2018-09-28
 **/

public class Bullet {

    private ImageView bulletImageView;

    private int state;

    private int power;

    public ImageView getBulletImageView() {
        return bulletImageView;
    }

    public void setBulletImageView(ImageView bulletImageView) {
        this.bulletImageView = bulletImageView;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }
}
