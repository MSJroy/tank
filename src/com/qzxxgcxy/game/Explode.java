package com.qzxxgcxy.game;

import com.qzxxgcxy.util.MyUtil;

import java.awt.*;

//用来控制爆炸效果的类
public class Explode {
    public static final int EXPLODE_FRAME_COUNT = 8;
    //导入资源
    private static Image[] img;
    //爆炸效果的图片的宽度和高度
    private static int explodeWidth;
    private static int explodeHeight;
    static {
        img = new Image[EXPLODE_FRAME_COUNT/2];
        for (int i = 0; i < img.length; i++) {
            img[i] = MyUtil.createImage("res/boom_"+i+".png");
        }
        explodeWidth = img[0].getWidth(null)/2;
        explodeHeight = img[0].getHeight(null);
    }

    //爆炸效果的属性
    private int x,y;
    //当前播放的帧的下标[0-11]
    private int index;
    //
    private boolean visible = true;

    public Explode() {
        index = 0;
    }

    public Explode(int x, int y) {
        this.x = x;
        this.y = y;
        index = 0;
    }

    public void draw(Graphics g){
        //对爆炸效果图片的宽高的确定
        if (explodeHeight <= 0){
            explodeHeight = img[0].getHeight(null);
            explodeWidth = img[0].getWidth(null)>>1;
        }
        if(!visible)return;
        g.drawImage(img[index/2],x-explodeWidth,y-explodeHeight,null);
        index ++;
        //播放完最后一帧，设置为不可见
        if(index >= EXPLODE_FRAME_COUNT){
            visible = false;
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

}
