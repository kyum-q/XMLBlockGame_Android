package org.techtown.blockgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;


public class GameGroundPanel extends ConstraintLayout {

    int blockFinalCount=0, blockCount=0;
    int ready=-1, gameValue = 0;
    int attackCount = 1, firstDownAttack=0, downAttack=0, startCheck = 0; // 시작하면 1 아니면 0
    DontGoneBlock block[] = new DontGoneBlock[100000];
    org.techtown.blockgame.AttackThread attackThread[] = new AttackThread[100000];
    org.techtown.blockgame.AttackImage attack[] = new AttackImage[100000];
    org.techtown.blockgame.AimGraphic aim;
    org.techtown.blockgame.GameFragment gameFragment;
    org.techtown.blockgame.GameView gameView;
    Handler handler = new Handler();

    public GameGroundPanel(@NonNull Context context) {
        super(context);
        init();
    }

    public GameGroundPanel(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

    }

    public void setAttackImage(AttackImage attack){
        gameView.moveImage(attack);
    }

    public void setAttack(AttackImage[] attack, AttackThread attackThread[], int attackCount, AimGraphic aim){
        this.attack = attack;
        this.attackThread = attackThread;
        this.attackCount = attackCount;
        this.aim = aim;
    }

    public void setGameGround(GameFragment gameFragment, GameView gameView) {
        this.gameFragment = gameFragment;
        this.gameView = gameView;
    }

    public void createDontGoneBlock(DontGoneBlock newBlock) {
        if(newBlock.getState()) {
            block[blockFinalCount] = newBlock;
            //addView(block[blockFinalCount],newBlock.getW(),newBlock.getH());
            gameView.addImage(block[blockCount]);
            blockFinalCount++;
            blockCount++;
        }
    }

    /**
     * blockCount를 하나 줄이는 함수
     */
    public void decreaseBlockCount() {
        blockCount--;
        if(blockCount<=0)
            gameOver();
    }
    /**
     * 공격한 뒤 블록이 내려오는 블럭인지 확인하고
     * 그럴 경우 블럭을 움직이는 함수
     */
    public void setBlockDown() {
        for(int i=0;i<blockFinalCount;i++) {
            if(block[i]!=null && block[i].getState() && block[i].checkBlockDown()) {
                block[i].setX(block[i].getX());
                block[i].setY(block[i].getY()+block[i].getH());
                gameView.moveImage(block[i]);
                if(block[i].getY()>=getHeight()) {
                    if(block[i] instanceof GoneBlockInterface) {
                        ((MainActivity)MainActivity.mContext).playBallSound(2);
                        gameFragment.decreaseLife();
                    }
                    removeImage(block[i]);
                    decreaseBlockCount();
                }
            }
        }
        gameView.drawImage();
    }
    /**
     * 옆으로 움직이는 블록이 다른 블록과 만났을 시 반대편으로 움직이게 하는 함수
     *
     * @param direction 현재 움직이는 블록의 방향
     * @param myBlock 다른 블록과 만났는지 확인하고자 하는 움직이는 블록
     * @param lastAttackBlock 움직이는 블록이 마지막으로 만난 블록
     * @return 블록이 다른 블록과 만났을 경우 true
     */
    public boolean checkBlockSide(int direction, SideMoveBlock myBlock, DontGoneBlock lastAttackBlock) { //direction<0이면 <-방향 || >0이면 ->방향
        gameView.moveImage(myBlock);
        for(int i=0;i<blockFinalCount;i++) {
            if(block[i] != null && block[i].getState() && !(block[i].equals(myBlock)) && block[i].checkBlockMit(myBlock, 2)) {
                if(lastAttackBlock != null && lastAttackBlock.getState() || !(block[i].equals(lastAttackBlock)) ) {
                    if(direction<0 &&  block[i].checkBlockMit(myBlock, 1)) {
                        myBlock.setLastAttackBlockPoint(block[i]);
                        gameView.moveImage(block[i]);
                        return true;
                    }
                    if(direction>0 &&  block[i].checkBlockMit(myBlock, 1)) {
                        myBlock.setLastAttackBlockPoint(block[i]);
                        gameView.moveImage(block[i]);
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * blockMaxCount를 리턴하는 함수
     * @return blockMaxCount (block의 최대 갯수)
     */
    public int getBlockCount() { return blockFinalCount; }

    /**
     * attack을 움직이게 하는 함수<br>
     * attackThread를 생성해 움직여 공격 실행
     */
    public void attack() {
        aim.setDrawState(true);
        ready = 0;
        downAttack=0;
        for(int i=0; i<attackCount;i++) {
            attackThread[i] = new AttackThread(attack[i], i, block, this);
        }
        gameView.drawImage();
    }
    /**
     * attack 볼이 내려온 순서 확인하여
     * 맨 처음 내려온 함수를 다음 공격 시작 위치로 지정
     * 마지막에 함수가 내려오면 다시 준비 동작으로 하는 함수
     * @param i 내려온 볼의 attack index
     */
    public void setDownAttack(int i) {
        if(downAttack==0)
            firstDownAttack = i;
        if(downAttack==attackCount-1) {
            setGameReady();
            setReady(-1);
        }
        downAttack++;
        gameView.moveImage(attack[i]);
    }
    /**
     * user의 위치를 공격 후 attack이 맨 처음으로 떨어진 위치로 이동시키는 함수
     */
    private void setGameReady() {
        for(int i=0; i<attackCount;i++) {
            attack[i].setX(attack[firstDownAttack].getX());
            attack[i].setY(getHeight() - attack[i].getH());
        }
        aim.setAttackLocation(attack[0]);
        if(gameFragment != null)
            gameFragment.setUserLocation(attack[firstDownAttack]);
    }

    public void startAttack() {
        if(gameFragment != null)
            gameFragment.setUserImg(1);
        for (int i = 0; i < attackCount; i++) {
            int finalI = i;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (attackThread[finalI] != null && attackThread[finalI].getState() == Thread.State.NEW)
                        attackThread[finalI].start();
                }
            });

        }
        ready = 1;
    }

    /**
     * ready 정수 인자를 리턴하는 함수 <br>
     * ready : 0일 경우 attack 조준점 잡는, 1일 경우 attack 움직이는, -1일 경우 조준점 잡기 전 game set
     *
     * @return ready 리턴
     */
    public int getReady() { return ready; }
    /**
     * ready를 설정
     * @param ready 설정하고자 하는 ready 값
     */
    private void setReady(int ready) { this.ready = ready; }

    /**
     * 게임을 종료시키는 함수
     */
    public void gameOver() {
        aim.setDrawState(false);
        ready = -1;
        for (int i = 0; i < blockFinalCount; i++)
            if (block[i] != null) {
                removeImage(block[i]);
            }
        for (int i=0;i<attackCount;i++){
            attackThread[i].interrupt();
            removeImage(attack[i]);
        }

    }

    /**
     * 게임을 일시 중단하는 함수
     */
    public void gameStop() {
        for(int i=0;i<attackThread.length;i++)
            if(attackThread[i]!=null)
                attackThread[i].gameStop();
        for(int i=0;i<blockFinalCount;i++)
            if(block[i]!=null && block[i] instanceof SideMoveBlock) {
                ((SideMoveBlock)block[i]).gameStop();
            }
        if(ready == 0) {
            ready = 1;
            gameValue = 1;
        }
    }
    /**
     * 게임을 재시작하는 함수
     */
    public void gameRePlay() {
        for(int i=0;i<attackThread.length;i++)
            if(attackThread[i]!=null)
                attackThread[i].startGame();

        for(int i=0;i<blockFinalCount;i++)
            if(block[i]!=null && block[i] instanceof SideMoveBlock)
                ((SideMoveBlock)block[i]).startGame();
        if(gameValue == 1) {
            ready = 0;
            gameValue = 0;
        }
    }

    public void removeImage(GrapicImage image){
        if(image!=null) {
            image.removeImage();
            gameView.removeImage(image);
        }
    }
}
