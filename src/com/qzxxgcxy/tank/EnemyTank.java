package com.qzxxgcxy.tank;

import com.qzxxgcxy.game.GameFrame;
import com.qzxxgcxy.game.LevelInof;
import com.qzxxgcxy.util.Constant;
import com.qzxxgcxy.util.EnemyTanksPool;
import com.qzxxgcxy.util.MyUtil;

import java.awt.*;
//敌人坦克类
public class EnemyTank extends Tank{
    private static final int TYPE_GREEN = 0;
    public static final int TYPE_YELLOW = 1;
    private int type = TYPE_GREEN;

    //坦克的图片数组
    private static Image[] greenImg;
    private static Image[] yellowImg;
    //纪录5秒开始的时间
    private long aiTime;

    static {
        greenImg = new Image[4];
        greenImg[0] = MyUtil.createImage("res/ul.png");
        greenImg[1] = MyUtil.createImage("res/dl.png");
        greenImg[2] = MyUtil.createImage("res/ll.png");
        greenImg[3] = MyUtil.createImage("res/rl.png");
        yellowImg = new Image[4];
        yellowImg[0] = MyUtil.createImage("res/ul2.png");
        yellowImg[1] = MyUtil.createImage("res/dl2.png");
        yellowImg[2] = MyUtil.createImage("res/ll2.png");
        yellowImg[3] = MyUtil.createImage("res/rl2.png");
    }

    private EnemyTank(int x, int y, int dir) {
        super(x, y, dir);
        //敌人一旦创建就计时
        aiTime = System.currentTimeMillis();
        type = MyUtil.getRandomNumber(0,2);
    }

    public EnemyTank(){
        type = MyUtil.getRandomNumber(0,2);
        aiTime = System.currentTimeMillis();
    }

    //用于创建一个敌人的坦克
    public static Tank createEnemy(){
        int x = MyUtil.getRandomNumber(0,2) == 0 ? RADIUS :
                Constant.FRAME_WIDTH - RADIUS;
        int y = GameFrame.titleBarH + RADIUS;
        int dir = DIR_DOWN;
        EnemyTank enemy = (EnemyTank)EnemyTanksPool.get();
        enemy.setX(x);
        enemy.setY(y);
        enemy.setDir(dir);
        enemy.setEnemy(true);
        enemy.setState(STATE_MOVE);
        //根据游戏的难度设置敌人的血量
        int maxHp = Tank.DEFAULT_HP*LevelInof.getInstance().getLevelType();
        enemy.setHp(maxHp);
        enemy.setMaxHP(maxHp);
        //通过关卡信息中敌人类型，来设置当前出生的敌人的类型
        int enemyType = LevelInof.getInstance().getRandomEnemyType();
        enemy.setType(enemyType);

        return enemy;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void drawImgTank(Graphics g) {
        ai();
        g.drawImage(type == TYPE_GREEN ? greenImg[getDir()] :
                yellowImg[getDir()],getX() - RADIUS, getY() - RADIUS, null);
    }
    //敌人的AI
    private void ai(){
        if(System.currentTimeMillis() - aiTime > Constant.ENEMY_AI_INTERVAL){
            //间隔5秒随机一个状态
            setDir(MyUtil.getRandomNumber(DIR_UP,DIR_RIGHT+1));
            setState(MyUtil.getRandomNumber(0,2) == 0 ? STATE_STAND : STATE_MOVE);
            aiTime = System.currentTimeMillis();
        }
        //比较小的概率去开火
        if(Math.random() < Constant.ENEMY_FIRE_PERCENT){
            fire();
        }
    }
}
