package com.qzxxgcxy.util;

import com.qzxxgcxy.map.MapTile;
import com.qzxxgcxy.tank.EnemyTank;
import com.qzxxgcxy.tank.Tank;

import java.util.ArrayList;
import java.util.List;

public class MapTilePool{

    public static final int DEFAULT_POOL_SIZE = 50;
    public static final int POOL_MAX_SIZE = 70;

    private static List<MapTile> pool = new ArrayList<>();
    static {
        for (int i = 0; i <DEFAULT_POOL_SIZE ; i++){
            pool.add(new MapTile());
        }
    }
    //从池塘中获取一个坦克对象
    public static MapTile get(){
        MapTile tile = null;
        //池塘被掏空了
        if(pool.size() == 0){
            tile = new MapTile();
        }else{//这个池塘中还有对象，拿走第一个位置的坦克对象
            tile = pool.remove(0);
        }
        return tile;
    }
    //坦克被销毁的时候，归还到池塘中来
    public static void theReturn(MapTile tile){
        //池塘中的坦克的个数已经到达了最大值，那就不再归还
        if(pool.size() == POOL_MAX_SIZE){
            return;
        }
        pool.add(tile);
    }
}
