package com.qzxxgcxy.util;

import com.qzxxgcxy.game.Bullet;
import com.qzxxgcxy.tank.EnemyTank;
import com.qzxxgcxy.tank.Tank;

import java.util.ArrayList;
import java.util.List;

/**
 * 敌人坦克对象池
 */
public class EnemyTanksPool {

    public static final int DEFAULT_POOL_SIZE = 20;
    public static final int POOL_MAX_SIZE = 20;

    private static List<Tank> pool = new ArrayList<>();
    static {
        for (int i = 0; i <DEFAULT_POOL_SIZE ; i++){
            pool.add(new EnemyTank());
        }
    }
    //从池塘中获取一个坦克对象
    public static Tank get(){
        Tank tank = null;
        //池塘被掏空了
        if(pool.size() == 0){
            tank = new EnemyTank();
        }else{//这个池塘中还有对象，拿走第一个位置的坦克对象
            tank = pool.remove(0);
        }
        return tank;
    }
    //坦克被销毁的时候，归还到池塘中来
    public static void theReturn(Tank tank){
        //池塘中的坦克的个数已经到达了最大值，那就不再归还
        if(pool.size() == POOL_MAX_SIZE){
            return;
        }
        pool.add(tank);
    }
}
