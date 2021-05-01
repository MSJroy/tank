package com.qzxxgcxy.util;

import com.qzxxgcxy.game.Bullet;
import com.qzxxgcxy.game.Explode;

import java.util.ArrayList;
import java.util.List;

/**
 * 爆炸效果对象池
 */
public class ExplodesPool {

    public static final int DEFAULT_POOL_SIZE = 10;
    public static final int POOL_MAX_SIZE = 20;

    //用于保存所有的爆炸效果的容器
    private static List<Explode> pool = new ArrayList<>();
    //
    static {
        for (int i = 0; i <DEFAULT_POOL_SIZE ; i++){
            pool.add(new Explode());
        }
    }
    //从池塘中获取一个爆炸对象
    public static Explode get(){
        Explode explode = null;
        //池塘被掏空了
        if(pool.size() == 0){
            explode = new Explode();
        }else{
            explode = pool.remove(0);
        }
        return explode;
    }
    public static void theReturn(Explode explode){
        if(pool.size() == POOL_MAX_SIZE){
            return;
        }
        pool.add(explode);
    }
}
