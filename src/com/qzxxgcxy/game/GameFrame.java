package com.qzxxgcxy.game;

import com.qzxxgcxy.map.GameMap;
import com.qzxxgcxy.map.MapTile;
import com.qzxxgcxy.tank.EnemyTank;
import com.qzxxgcxy.tank.MyTank;
import com.qzxxgcxy.tank.Tank;
import com.qzxxgcxy.util.MusicUtil;
import com.qzxxgcxy.util.MyUtil;
import com.sun.org.apache.bcel.internal.generic.BREAKPOINT;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.TagName;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static com.qzxxgcxy.util.Constant.*;
//游戏的主窗口类。
//所有的游戏展示的内容都要在该类中实现。
public class GameFrame extends Frame implements Runnable{
    //第一次使用的时候加载，而不是类加载
    private Image overImg = null;

    //1：定义一张和屏幕大小一致的图片
    private BufferedImage bufImg = new BufferedImage(FRAME_WIDTH,FRAME_HEIGHT,BufferedImage.TYPE_4BYTE_ABGR);
    //游戏状态
    private static int gameState;
    //菜单指向
    private static int menIndex;
    //标题栏的高度
    public  static int titleBarH;
    //定义坦克对象
    private static Tank myTank;
    //敌人的坦克容器
    private static List<Tank> enemies = new ArrayList<>();

    //用来记录本关产生了多少个敌人
    private static int bornEnemyCount;

    public static int killEnemyCount;

    //定义地图相关的内容
    private static GameMap gameMap = new GameMap();

    //对窗口进行初始化
    public GameFrame(){
        initFrame();
        initEventListener();
        //启动用于刷新窗口的线程
        new Thread(this).start();
    }

    /**
     * 进入下一关的方法
     */
    public static void nextLevel() {
        startGame(LevelInof.getInstance().getLevel()+1);
    }
    //开始过关动画
    public static int flashTime;
    public static final int RECT_WIDTH = 40;
    public static final int RECT_COUNT = FRAME_WIDTH/RECT_WIDTH+1;
    public static boolean isOpen = false;
    public static void startCrossLevel(){
        gameState = STATE_CROSS;
        flashTime = 0;
        isOpen = false;
    }
    //绘制过关动画
    public void drawCross(Graphics g){
        gameMap.drawBk(g);
        myTank.draw(g);
        gameMap.drawCover(g);

        g.setColor(Color.BLACK);
        //关闭百叶窗的效果
        if (!isOpen){
            for (int i =0; i < RECT_COUNT; i++){
                g.fillRect(i * RECT_WIDTH,0,flashTime,FRAME_HEIGHT);
            }
            //所有的叶片都关闭了
            if (flashTime++ - RECT_WIDTH > 5){
                isOpen = true;
                //初始化下一个地图
                gameMap.initMap(LevelInof.getInstance().getLevel()+1);
            }
        }else {
            //开百叶窗的效果
            for (int i = 0; i < RECT_COUNT; i++) {
                g.fillRect(i * RECT_WIDTH,0,flashTime,FRAME_HEIGHT);
            }
            if (flashTime-- == 0){
                startGame(LevelInof.getInstance().getLevel());
            }
        }
    }
    //对游戏进行初始化
    private void initGame(){
        gameState = STATE_MENU;
    }
    //属性进行初始化
    private void initFrame(){
        //设置标题
        setTitle(GAME_TITLE);
        //设置窗口大小
        setSize(FRAME_WIDTH,FRAME_WIDTH);
        //设置窗口的左上角的坐标
        setLocation(FRAME_X,FRAME_Y);
        //设置窗口大小不可改变
        setResizable(false);
        //设置窗口可见
        setVisible(true);

        //求标题栏的高度
         titleBarH = getInsets().top;
    }

    /*是Frame 类的方法，继承下来的方法，该方法负责了所有的绘制的内容，
    *所有需要在屏幕中显示的内容，都要在该方法内调用。该方法不能主动调用。必须通过调用
    * repaint();去回调该方法。
    * g1系统提供的画笔，系统进行初始化
    */
    @Override
    public void update(Graphics g1) {
        //2：得到图片的画笔
        Graphics g = bufImg.getGraphics();
        //3：使用图片画笔将所有的内容绘制到图片上
        g.setFont(GAME_FONT);
        switch (gameState){
            case STATE_MENU:
                drawMenu(g);
                break;
            case STATE_HELP:
                drawHelp(g);
                break;
            case STATE_ABOUT:
                drawAbout(g);
                break;
            case STATE_RUN:
                drawRun(g);
                break;
            case STATE_LOST:
                drawLost(g,"过关失败");
                break;
            case STATE_WIN:
                drawWin(g);
                break;
            case STATE_CROSS:
                drawCross(g);
                break;
        }

        //4：使用系统画笔，将图片绘制到frame上来
        g1.drawImage(bufImg,0,0,null);
    }

    /**
     * 绘制游戏结束的方法 TODO
     * @param g
     */
    private void drawLost(Graphics g,String str) {
        //保证只加载一次
        if(overImg == null){
            overImg = MyUtil.createImage("res/over.png");
        }
        g.setColor(Color.BLACK);
        g.fillRect(0,0,FRAME_WIDTH,FRAME_HEIGHT);

        int imgW = overImg.getWidth(null);
        int imgH = overImg.getHeight(null);

        g.drawImage(overImg,FRAME_WIDTH - imgW >> 1,FRAME_HEIGHT-imgH >> 1,null);

        //添加按键提示信息
        g.setColor(Color.WHITE);
        g.drawString(OVER_STR0,10,FRAME_HEIGHT-20);
        g.drawString(OVER_STR1,FRAME_WIDTH-200,FRAME_HEIGHT-20);

        //失败文字
        g.setColor(Color.WHITE);
        g.drawString(str,FRAME_WIDTH/2-30,50);
    }

    /**
     * 游戏胜利的界面
     * @param g
     */
    private void drawWin(Graphics g){
        drawLost(g,"游戏通关");
    }

    //游戏运行状态的绘制内容
    private void drawRun(Graphics g) {
        //绘制黑色的背景
        g.setColor(Color.BLACK);
        g.fillRect(0,0,FRAME_WIDTH,FRAME_HEIGHT);

        //绘制地图的碰撞层
        gameMap.drawBk(g);

        drawEnemies(g);

        myTank.draw(g);

        //绘制地图的遮挡层
        gameMap.drawCover(g);

        drawExplodes(g);

        //子弹和坦克的碰撞的方法
        bulletCollideTank();

        //子弹和所有的地图块的碰撞
        bulletAndTankCollideMapTile();

    }
    //绘制所有的敌人的坦克，如果敌人已经死亡，从容器中移除
    private void drawEnemies(Graphics g){
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            if(enemy.isDie()){
                enemies.remove(i);
                i--;
                continue;
            }
            enemy.draw(g);
        }
    }

    private Image helpImg;
    private Image aboutImg;
    private void drawAbout(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,FRAME_WIDTH,FRAME_HEIGHT);
        if (aboutImg == null){
            aboutImg = MyUtil.createImage("res/about.jpg");
        }
        int width = aboutImg.getWidth(null);
        int height = aboutImg.getHeight(null);

        int x = FRAME_WIDTH - width >>1;
        int y = FRAME_HEIGHT - height >>1;
        g.drawImage(aboutImg,x,y,null);

        g.setColor(Color.WHITE);
        g.drawString("按任意键继续",10,FRAME_HEIGHT-10);
    }

    private void drawHelp(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,FRAME_WIDTH,FRAME_HEIGHT);
        if (helpImg == null){
         helpImg = MyUtil.createImage("res/help.jpg");
        }
        int width = helpImg.getWidth(null);
        int height = helpImg.getHeight(null);

        int x = FRAME_WIDTH - width >>1;
        int y = FRAME_HEIGHT - height >>1;
        g.drawImage(helpImg,x,y,null);

        g.setColor(Color.WHITE);
        g.drawString("按任意键继续",10,FRAME_HEIGHT-10);
    }

    //绘制菜单状态下的内容
    //g 画笔对象，系统提供的
    private void drawMenu(Graphics g){
        //绘制黑色的背景
        g.setColor(Color.BLACK);
        g.fillRect(0,0,FRAME_WIDTH,FRAME_HEIGHT);

        final int STR_WIDTH = 76;
        int x = FRAME_WIDTH - STR_WIDTH>>1;
        int y = FRAME_HEIGHT/3;
        //行间距
        final int DIS = 50;

        for (int i = 0;i < MENUS.length;i++){
            if(i == menIndex){//选中的菜单项的颜色设置为红色。
                g.setColor(Color.RED);
            }else {//其他的为白色
                g.setColor(Color.WHITE);
            }
            g.drawString(MENUS[i],x,y+DIS*i);
        }
    }

    //初始化窗口的事件监听
    private void initEventListener (){
        //注册监听事件
        addWindowListener(new WindowAdapter() {
            //点击关闭按钮的时候，方法会被自动调用
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        //添加按键监听事件
        addKeyListener(new KeyAdapter() {
            //按键被按下的时候被回调的方法
            @Override
            public void keyPressed(KeyEvent e) {
                //获得被按下键的键值
                int keyCode = e.getKeyCode();
                //不同的游戏状态，给出不同的处理的方法。
                switch (gameState){
                    case STATE_MENU:
                        keyPressedEventMenu(keyCode);
                        break;
                    case STATE_HELP:
                        keyPressedEventHelp(keyCode);
                        break;
                    case STATE_ABOUT:
                        keyPressedEventAbout(keyCode);
                        break;
                    case STATE_RUN:
                        keyPressedEventRun(keyCode);
                        break;
                    case STATE_LOST:
                        keyPressedEventLost(keyCode);
                        break;
                    case STATE_WIN:
                        keyPressedEventWin(keyCode);
                        break;
                }
            }
            //按键松开的时候回调的内容
            @Override
            public void keyReleased(KeyEvent e) {
                //获得被按下键的键值
                int keyCode = e.getKeyCode();
                //不同的游戏状态，给出不同的处理的方法。
                if (gameState == STATE_RUN){
                    keyReleasedEventRun(keyCode);
                }
            }
        });
    }

    //游戏通过的按键处理
    private void keyPressedEventWin(int keyCode) {
        keyPressedEventLost(keyCode);
    }

    //按键松开的时候，游戏中的处理方法
    private void keyReleasedEventRun(int keyCode){
        switch (keyCode){
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                myTank.setState(Tank.STATE_STAND);
                break;
        }
    }
    //游戏结束的按键处理 TODO
    private void keyPressedEventLost(int keyCode) {
        //结束游戏
        if(keyCode == KeyEvent.VK_ESCAPE){
            System.exit(0);
        }else if(keyCode == KeyEvent.VK_ENTER){
            setGameState(STATE_MENU);
            //很多游戏的操作需要关闭，有些的某些属性需要重置
            resetGame();
        }
    }

    //重置游戏状态
    private void resetGame(){
        killEnemyCount = 0;
        menIndex = 0;
        //先让自己的坦克的子弹还回对象池
        myTank.bulletsReturn();
        //销毁自己的坦克
        myTank = null;
        //清空敌人相关资源
        for (Tank enemy : enemies) {
            enemy.bulletsReturn();
        }
        enemies.clear();
        //清空地图资源
        gameMap = null;
    }
    //游戏运行中的按键处理方法
    private void keyPressedEventRun(int keyCode) {
        switch (keyCode){
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                myTank.setDir(Tank.DIR_UP);
                myTank.setState(Tank.STATE_MOVE);
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                myTank.setDir(Tank.DIR_DOWN);
                myTank.setState(Tank.STATE_MOVE);
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                myTank.setDir(Tank.DIR_LEFT);
                myTank.setState(Tank.STATE_MOVE);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                myTank.setDir(Tank.DIR_RIGHT);
                myTank.setState(Tank.STATE_MOVE);
                break;
            case KeyEvent.VK_SPACE:
                myTank.fire();
                break;
        }
    }

    private void keyPressedEventAbout(int keyCode) {
        setGameState(STATE_MENU);
    }

    private void keyPressedEventHelp(int keyCode) {
        setGameState(STATE_MENU);
    }
    //菜单状态下的按键的处理
    private void keyPressedEventMenu(int keyCode) {
        switch (keyCode){
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                if (--menIndex < 0){
                    menIndex = MENUS.length-1;
                }
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                if (++menIndex > MENUS.length -1){
                    menIndex = 0;
                }
                break;
            case KeyEvent.VK_ENTER:
                switch (menIndex){
                    case 0:
                        startGame(1);
                        break;
                    case 1:
                        //继续游戏，进入选择关卡的界面
                        break;
                    case 2:
                        setGameState(STATE_HELP);
                        break;
                    case 3:
                        setGameState(STATE_ABOUT);
                        break;
                    case 4:
                        System.exit(0);
                        break;
                }
                break;
        }
    }
    /**
     *开始游戏的方法
     * 并加载level关卡信息
     */
    private static void startGame(int level) {
        enemies.clear();
        if (gameMap == null){
            gameMap = new GameMap();
        }
        gameMap.initMap(level);
        MusicUtil.playStart();
        killEnemyCount = 0;
        bornEnemyCount = 0;
        gameState = STATE_RUN;
        //创建坦克对象，敌人的坦克对象
        myTank = new MyTank(FRAME_WIDTH/3,FRAME_HEIGHT-Tank.RADIUS,Tank.DIR_UP);

        //使用一个单独的线程用于控制生产敌人的坦克
        new Thread(){
            @Override
            public void run() {
                while (true){
                    if(LevelInof.getInstance().getEnemyCount()>bornEnemyCount && enemies.size() < ENEMY_MAX_COUNT){
                        Tank enemy = EnemyTank.createEnemy();
                        enemies.add(enemy);
                        bornEnemyCount ++;
                    }
                    try {
                        Thread.sleep(ENEMY_BORN_IMTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //只有在游戏 run 状态下才创建敌人坦克
                    if(gameState != STATE_RUN){
                        break;
                    }
                }
            }
        }.start();
    }

    //线程控制刷新
    @Override
    public void run() {
        while (true){
            //在此调用repaint，回调update
            repaint();
            try {
                Thread.sleep(REPAINT_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //敌人的坦克的子弹和我的坦克的碰撞
    private void bulletCollideTank(){
        //我的坦克的子弹和所有的敌人的碰撞
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            enemy.collideBullets(myTank.getBullets());
        }
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            myTank.collideBullets(enemy.getBullets());
        }
    }

    //所有的子弹和地图块的碰撞
    private void bulletAndTankCollideMapTile(){
        //自己的坦克的子弹和地图块的碰撞
        myTank.bulletsCollideMapTiles(gameMap.getTiles());

        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            enemy.bulletsCollideMapTiles(gameMap.getTiles());
        }
        //我的坦克和地图的碰撞
        if(myTank.isCollideTile(gameMap.getTiles())){
            myTank.back();
        }
        //敌人的坦克和地图的碰撞
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            if(enemy.isCollideTile(gameMap.getTiles())){
                enemy.back();
            }
        }
        //清理所有的被销毁的地图块
        gameMap.clearDestoryTile();
    }

    //所有的坦克上的爆炸效果
    private void drawExplodes(Graphics g){
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            enemy.drawExplodes(g);
        }
        myTank.drawExplodes(g);
    }

    //获得游戏状态和修改游戏状态
    public static void setGameState(int gameState) {
        GameFrame.gameState = gameState;
    }
    public static int getGameState() {
        return gameState;
    }

    /**
     * 是否是最后一关
     * @return
     */
    public static boolean isLastLeve(){
        //当前关卡和总关卡一致
        int currlevel = LevelInof.getInstance().getLevel();
        int levelCount = GameInfo.getLevelCount();
        return currlevel == levelCount;
    }

    /**
     * 判断是否过关
     * @return
     */
    public static boolean isCrossLevel(){
        return killEnemyCount == LevelInof.getInstance().getEnemyCount();
    }

}
