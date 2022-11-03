package org.techtown.blockgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class AttackImage extends GrapicImage{
    private int delay=0,ballCountDelay=0;

    public AttackImage(int delay, int ballCountDelay, int size) {
        this.delay = delay;
        this.ballCountDelay = ballCountDelay;
        this.w = this.h = size;
        init();
    }

    public void init() {

    }

    public int getDelay() {
        return delay;
    }

    public int getBallCountDelay() {
        return ballCountDelay;
    }


    public void setImage(int imageID){
        this.imageID = imageID;
    }

    /*
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.w=w;
        this.h=h;

        createCacheBitmap(w,h);
        testDrawing(w,h);
    }

    public void createCacheBitmap(int w,int h){
        cacheBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas();
        cacheCanvas.setBitmap(cacheBitmap);
    }

    public void testDrawing(int w, int h){
        Bitmap srcImg;
        if(imageID == 0)
            return;
        srcImg = BitmapFactory.decodeResource(getResources(), imageID);
        srcImg = Bitmap.createScaledBitmap(srcImg, w, h, true);
        cacheCanvas.drawBitmap(srcImg, 0, 0, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (cacheBitmap != null) {
            canvas.drawBitmap(cacheBitmap, 0, 0, null);
        }
    }
    */

}
