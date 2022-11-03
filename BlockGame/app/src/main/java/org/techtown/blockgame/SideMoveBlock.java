package org.techtown.blockgame;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class SideMoveBlock extends DontGoneBlock implements Runnable {

    protected int moveDelay, moveX, moveDirection = 5, moveValue = 1;
    protected boolean gameState = true;
    protected DontGoneBlock lastAttackBlock = null;
    protected Thread th = null;
    protected GameGroundPanel groundPanel;
    //View parent;

    Handler handler = new Handler();
    SideMoveBlock blockImage;

    public SideMoveBlock() {
        super();
    }

    public void setBlock(int x ,int y ,int w ,int h ,int blockDown ,int imageID ,int moveDelay, int moveDirection){
        super.setBlock(x,y,w,h,blockDown,imageID);
        this.moveDelay = moveDelay;
        this.moveDirection = moveDirection;
        if(moveDirection<0)
            moveValue = -1;

        moveX = moveDirection;

        groundPanel = ((MainActivity)MainActivity.mContext).gameFragment.gameGround;

        th = new Thread(this);
        th.start();
    }

    public SideMoveBlock copyBlock() {
        SideMoveBlock GoneBlock = new SideMoveBlock();
        GoneBlock.setBlock(x,y,w,h,blockDown,imageID,moveDelay,moveDirection);

        return GoneBlock;
    }

    public void setMove(int moveValue) {
        this.setX(this.getX() + moveValue);
    }

    /**
     * 좌우로 움직이면서 마지막으로 만난 블록을 변경하는 함수
     *
     * @param lastAttackBlock 변경하고자 하는 마지막으로 만난 블록
     */
    public void setLastAttackBlockPoint(DontGoneBlock lastAttackBlock) {
        this.lastAttackBlock = lastAttackBlock;
    }
    /**
     * 게임을 중단하는 함수<br>
     * gameState를 false로 만듦
     */
    public void gameStop() {gameState = false;}
    /**
     * 현재 게임 상태(gameState)를 확인하는 함수<br>
     * gameState가 false일 경우 wait()
     */
    synchronized private void checkGameState() {
        if(!gameState) {
            try { wait();}
            catch (InterruptedException e) { return; }
        }
    }
    /**
     * 게임을 재시작하는 함수<br>
     * gameState를 true로 만들고 wait한 것들을 notifyAll()
     */
    synchronized public void startGame() {
        gameState = true;
        notifyAll();
    }
    /**
     * Thread의 run 함수<br>
     * block을 움직인다
     */
    @Override
    public void run() {
        while (true) {
            checkGameState();

            try {
                Thread.sleep(moveDelay);

                if(groundPanel == null )
                    continue;
                if(getX()+getW()>=groundPanel.getWidth()) {
                    moveX = -(moveDirection*moveValue);
                    lastAttackBlock = null;
                }
                else if(getX()<=0) {
                    moveX = moveDirection*moveValue;
                    lastAttackBlock = null;
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        int x = (int) getX();
                        setX(x + moveX);
                    }
                });


                if(groundPanel!=null && groundPanel.checkBlockSide(moveX,this,lastAttackBlock)) {
                    if(moveX>0)
                        moveX = -(moveDirection*moveValue);
                    else
                        moveX = moveDirection*moveValue;
                }

            } catch (InterruptedException e) { return; }
        }
    }
}
