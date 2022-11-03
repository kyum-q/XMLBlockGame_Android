package org.techtown.blockgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class AimGraphic extends View {
    Paint paint;
    Canvas mCanvas;
    Bitmap mBitmap;
    float attackX, attackY;
    static float x, y;
    boolean drawState = true;
    GameGroundPanel groundPanel;

    public AimGraphic(Context context) {
        super(context);
        init(context);
    }

    public AimGraphic(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);
    }

    public void setAttackLocation(AttackImage attack){
        attackX = attack.getX() + attack.getW()/2;
        attackY = attack.getY();
    }

    public void setGroundPanel(GameGroundPanel groundPanel) {
        this.groundPanel = groundPanel;
    }

    public void setAimColor(int r,int g,int b){
        paint.setColor(Color.rgb(r,g,b));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas();
        mCanvas.setBitmap(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mBitmap != null){
            canvas.drawBitmap(mBitmap,0,0,null);
        }
    }

    public void setDrawState(boolean state) {
        drawState = state;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        x = (int) event.getX();
        y = (int) event.getY();

        if(drawState) {
            switch (action) {
                case MotionEvent.ACTION_UP:
                    mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    drawState = false;
                    groundPanel.startAttack();

                    break;
                case MotionEvent.ACTION_DOWN:
                    mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    mCanvas.drawLine(attackX, attackY, x, y, paint);
                    break;
                case MotionEvent.ACTION_MOVE:
                    mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    mCanvas.drawLine(attackX, attackY, x, y, paint);
                    break;
            }
        }
        invalidate();

        return true;
    }

    /**
     * aimPoint를 알아내기
     * @return aimPoint 리턴
     */
    public static Point getAimPoint() {
        return new Point((int)x,(int)y);
    }
}
