package com.qzxxgcxy.map;

import com.qzxxgcxy.util.Constant;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 玩家的大本营
 */
public class TankHouse {
    //老巢的x，y坐标
    public static final int HOUSE_X = (Constant.FRAME_WIDTH-3*MapTile.tileW >> 1)+2;
    public static final int HOUSE_Y = Constant.FRAME_HEIGHT-2*MapTile.tileW;

    //一共六块地图块
    private List<MapTile> tiles = new ArrayList<>();
    public TankHouse() {
        tiles.add(new MapTile(HOUSE_X,HOUSE_Y));
        tiles.add(new MapTile(HOUSE_X,HOUSE_Y+MapTile.tileW));
        tiles.add(new MapTile(HOUSE_X+MapTile.tileW,HOUSE_Y));

        tiles.add(new MapTile(HOUSE_X+MapTile.tileW*2,HOUSE_Y));
        tiles.add(new MapTile(HOUSE_X+MapTile.tileW*2,HOUSE_Y+MapTile.tileW));
        //有文字的块
        tiles.add(new MapTile(HOUSE_X+MapTile.tileW,HOUSE_Y+MapTile.tileW));
        //设置老巢地图块的类型
        tiles.get(tiles.size()-1).setType(MapTile.TYPE_HOUSE);
    }

    public void draw(Graphics g){
        for (MapTile tile : tiles) {
            tile.draw(g);
        }
    }

    public List<MapTile> getTiles() {
        return tiles;
    }
}
