package com.qzxxgcxy.tank;

import com.qzxxgcxy.util.MyUtil;

import java.awt.*;
//自己的坦克
public class MyTank extends Tank {

    private static Image[] tankImg;
    //静态代码块中对它进行初始化
    static {
        tankImg = new Image[4];
        tankImg[0] = MyUtil.createImage("res/u.png");
        tankImg[1] = MyUtil.createImage("res/d.png");
        tankImg[2] = MyUtil.createImage("res/l.png");
        tankImg[3] = MyUtil.createImage("res/r.png");

    }
    public MyTank(int x, int y, int dir) {
        super(x, y, dir);
    }
    @Override
    public void drawImgTank(Graphics g) {
        g.drawImage(tankImg[getDir()], getX() - RADIUS, getY() - RADIUS, null);
    }
}
