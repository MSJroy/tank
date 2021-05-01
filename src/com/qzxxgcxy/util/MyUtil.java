package com.qzxxgcxy.util;

import java.awt.*;

//工具类
public class MyUtil {
    private MyUtil(){}
    /**得到指定的期间的随机数
     * min 区间最小值，包含
     * max 区间最大值，不包含
     * return 随机数
     */
    public static final int getRandomNumber(int min,int max){
        return (int)(Math.random()*(max-min)+min);
    }

    //得到随机的颜色
    public static final Color getRandomColor(){
        int red = getRandomNumber(0,256);
        int blue = getRandomNumber(0,256);
        int green = getRandomNumber(0,256);
        return new Color(red,blue,green);
    }

    /**判断一个点是否在某一个正方形的内部，
     * @param rectX 正方形的中心点的x坐标
     * @param rectY 正方形的中心点的y坐标
     * @param radius 正方形的边长的一半
     * @param pointX 点的x坐标
     * @param pointY 点的y坐标
     * @return 如果点在正方形内部，返回true，否则返回false
     */
    public static final boolean isCollide(int rectX,int rectY,int radius,int pointX,int pointY){
        //正方形中心点 和 点 的 x y 轴 的距离
        int disX = Math.abs(rectX - pointX);
        int disY = Math.abs(rectY - pointY);
        if(disX < radius && disY < radius)
            return true;
        return false;
    }

    /**
     * 根据图片的资源路径创建加载图片对象
     * @param path 图片资源的路径
     * @return
     */
    public static final Image createImage(String path){
        return Toolkit.getDefaultToolkit().createImage(path);
    }

    private static final String[] NAMES = {
            "虎","狼","鼠","鹿","貂","猴","貘","树懒","斑马","狗",
            "狐","熊","象","豹子","麝牛","狮子","小熊猫","疣猪","羚羊",
            "驯鹿","考拉","犀牛","猞猁","穿山甲","长颈鹿","熊猫","食蚁兽",
            "猩猩","海牛","水獭","灵猫","海豚海象","鸭嘴兽","刺猬","北极狐",
            "无尾熊","北极熊","袋鼠","犰狳","河马","海豹","鲸鱼","鼬",
            "龙鱼","塘鳢","鲶鱼","鲨鱼","章鱼"
    };

    private static final String[] MODIFY = {
        "可爱","可爱","萌萌","羞羞","笨笨","呆呆","美丽","聪明","伶俐","狡猾","讨厌"
    };

    /**
     * 得到一个随机的名字
     * @return
     */
    public static final String getRandomName(){
        return MODIFY[getRandomNumber(0,MODIFY.length)] + "的" + NAMES[getRandomNumber(0,NAMES.length)];
    }
}
