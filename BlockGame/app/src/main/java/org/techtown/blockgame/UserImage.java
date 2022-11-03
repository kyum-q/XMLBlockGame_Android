package org.techtown.blockgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class UserImage extends View {
    Paint paint;
    Bitmap cacheBitmap;
    Canvas cacheCanvas;

    int attackUserID=0, userImgID=0, life=0 ,w, h, selectImg = 0;

    Bitmap srcImg[] = new Bitmap[2];

    public UserImage(Context context) {
        super(context);

        init(context);
    }

    public UserImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public void setUserImg(int i){
        selectImg = i;
        cacheCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        cacheCanvas.drawBitmap(srcImg[selectImg],0,0,paint);
    }

    /*
    public void setImage(String attackUserImg, String userImg, int life) {
        this.attackUserImg = attackUserImg;
        this.userImg = userImg;
        this.life = life;

        testDrawing(w,h);

        //Log.d("setUSerImage",attackUserID +", " +userImgID+", "+life);
    }
    */

    public void setImage(int attackUserID, int userImgID, int life) {
        this.attackUserID = attackUserID;
        this.userImgID = userImgID;
        this.life = life;

        testDrawing(w,h);

        //Log.d("setUSerImage",attackUserID +", " +userImgID+", "+life);
    }


    public void init(Context context) {
        paint = new Paint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.w = w;
        this.h = h;

        createCacheBitmap(w,h);
        testDrawing(w,h);
    }

    public void createCacheBitmap(int w,int h){
        cacheBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas();
        cacheCanvas.setBitmap(cacheBitmap);
    }

    public void testDrawing(int w, int h){

        if(userImgID == 0 || attackUserID == 0)
            return;

        srcImg[0] = BitmapFactory.decodeResource(getResources(), userImgID);
        srcImg[1] = BitmapFactory.decodeResource(getResources(), attackUserID);

        /*
        if(userImg == null || attackUserImg == null)
            return;

        srcImg[0] = BitmapFactory.decodeFile(userImg);
        srcImg[1] = BitmapFactory.decodeFile(attackUserImg);

        */
        srcImg[0] = Bitmap.createScaledBitmap(srcImg[0], w, h, true);
        srcImg[1] = Bitmap.createScaledBitmap(srcImg[1], w, h, true);
        cacheCanvas.drawBitmap(srcImg[selectImg],0,0,paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(cacheBitmap != null){
            canvas.drawBitmap(cacheBitmap, 0,0,null);
        }
    }
}