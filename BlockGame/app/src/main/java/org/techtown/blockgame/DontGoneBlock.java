package org.techtown.blockgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class DontGoneBlock extends GrapicImage{
    protected int blockDown=0;

    public DontGoneBlock(){

    }

    public DontGoneBlock copyBlock() {
        DontGoneBlock dontGoneBlock = new DontGoneBlock();
        dontGoneBlock.setBlock(x,y,w,h,blockDown,imageID);

        return dontGoneBlock;
    }

    public void setBlock(int x, int y, int w, int h, int blockDown, int imageID){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.blockDown = blockDown;
        this.imageID = imageID;
    }


    public boolean checkBlockMit(GrapicImage image,int i) { // i==0면 all i==1면 x만 확인 i==2면 y만 확인
        if(i==0) {
            if(x<=image.getX()+image.getW() && x+w>=image.getX()
                    && y<=image.getY()+image.getH() && y+h>=image.getY())
                return true;
        }
        else if(i==1) {
            if(x<image.getX()+image.getW() && x+w>image.getX())
                return true;
        }
        else if(i==2) {
            if(y<image.getY()+image.getH() && y+h>image.getY()) {
                return true;
            }
        }
        return false;
    }
    /**
     * GoneBlock은 맞았는지 확인하고 맞았으면 true 리턴
     *
     * @param attack 블록에 맞았는지 비교한 label
     * @return 블록이 attack에 맞았을 시 true
     */
    public boolean blockAttack(GrapicImage attack) {
        if(checkBlockMit(attack,0))
            return true;
        return false;
    }
    /**
     * block이 내려가는 블록인지 확인하는 함수
     *
     * @return block이 내려가는 블록이면 true 아니면 false
     */
    public boolean checkBlockDown() {
        if(blockDown>0)
            return true;
        return false;
    }
}
