package org.techtown.blockgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GameView extends View {

    Paint paint;
    Bitmap cacheBitmap;
    Canvas cacheCanvas;
    Paint clearPaint = new Paint();

    Bitmap imageBitmap[] = new Bitmap[10000];
    Point imagePoint[] = new Point[10000];
    GrapicImage image[] = new GrapicImage[100000];
    int imageCount = 0;

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        createCacheBitmap(w,h);
        drawImage();
    }

    public void createCacheBitmap(int w,int h){
        cacheBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas();
        cacheCanvas.setBitmap(cacheBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(cacheBitmap != null){
            canvas.drawBitmap(cacheBitmap, 0,0,null);
        }
    }

    public void addImage(GrapicImage newImage){
        int imageID = newImage.getImageID();
        image[imageCount] = newImage;
        imageBitmap[imageCount] = BitmapFactory.decodeResource(getResources(), imageID);
        imageBitmap[imageCount] = Bitmap.createScaledBitmap(imageBitmap[imageCount], newImage.getW(), newImage.getH(), true);
        imagePoint[imageCount] = new Point(newImage.getX(), newImage.getY());
        imageCount++;
    }

    public void drawImage() {
        cacheCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        for(int i=0;i<imageCount;i++) {
            if(image[i] != null) {
                cacheCanvas.drawBitmap(imageBitmap[i], image[i].getX(), image[i].getY(), paint);
                imagePoint[i].x = image[i].getX();
                imagePoint[i].y = image[i].getY();
            }
        }
        invalidate();
    }
    public void moveImage(GrapicImage moveImage) {

        for(int i=0;i<imageCount;i++) {
            if(image[i] != null && image[i]==moveImage) {
                cacheCanvas.drawBitmap(imageBitmap[i], imagePoint[i].x, imagePoint[i].y, clearPaint);
                cacheCanvas.drawBitmap(imageBitmap[i], image[i].getX(), image[i].getY(), paint);
                imagePoint[i].x = image[i].getX();
                imagePoint[i].y = image[i].getY();
                invalidate();
                break;
            }
        }
    }
    public void removeImage(GrapicImage removeImage) {
        for(int i=0;i<imageCount;i++) {
            if(image[i]==removeImage && image[i] != null) {
                cacheCanvas.drawBitmap(imageBitmap[i], imagePoint[i].x, imagePoint[i].y, clearPaint);
                image[i] = null;
                invalidate();
                break;
            }
        }
    }
}
