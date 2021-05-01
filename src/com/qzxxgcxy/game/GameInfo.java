package com.qzxxgcxy.game;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 游戏相关的信息的类
 */
public class GameInfo {
    //从配置文件中读取
    //关卡数量
    private static int levelCount;

    static {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("level/gameinfo"));
            levelCount = Integer.parseInt(prop.getProperty("levelCount"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getLevelCount(){
        return levelCount;
    }
}
