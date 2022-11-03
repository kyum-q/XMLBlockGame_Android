package org.techtown.blockgame;

import android.graphics.Point;

public class AttackThread extends Thread {

    private int delay, setDelay, ballDelay;
    private float x, y, startX, startY, reboundX, reboundY, moving = 5;
    private int directionX = 1, lastAttackBlock = -1, i = 0;
    private double formulaX;
    private AttackImage attack;
    private DontGoneBlock block[] = null;
    private GameGroundPanel gamePanel = null;
    private boolean gameState = true;
    /**
     * AttackThread의 생성자
     *
     * @param attack attack 이미지
     * @param i 몇번째 attack 공인지 알려주는 정수형 인자
     * @param block attack으로 때려야하는 block들
     * @param gamePanel 게임 진행 패널
     */
    public AttackThread(AttackImage attack, int i, DontGoneBlock block[], GameGroundPanel gamePanel) {

        this.gamePanel = gamePanel;
        this.attack = attack;
        this.i = i;
        this.block = block;
        this.reboundY = this.startY = this.y = attack.getY();
        this.reboundX = this.startX = this.x = attack.getX();

        this.setDelay = this.delay = attack.getDelay();
        this.ballDelay = attack.getBallCountDelay();
    }
    /**
     * attack이 움직이는 각도를 알아내는 함수<br>
     * : 원점과 포인트 점 사이에 각도를 알아내서 x의 증가 값에 따른 y의 증가 값 구하기
     *
     */
    private void nextXY(float pointX, float pointY) {
        float ratioX, ratioY;
        if(pointY == reboundY || pointX == startX && pointY == startY) {
            // 리바운드의 y좌표랑 조준점의 y좌표랑 같거나
            // 조준 점이 원점하고 똑같을 경우 각도 변경하지 않고 리바운드 점을 원점으로 변경
            reboundX = x;
            reboundY = y;
        }
        else {
            // 위에 경우가 아닌경우 원점과 조준점 사이의 각도 구하기
            ratioX = pointX - x;
            ratioY = pointY - y;
            if(ratioY == 0) {
                formulaX = 0;
            }
            else
                formulaX = ratioX/ratioY;

            if(formulaX>1)
                setDelay = (int) (delay*formulaX);
            else if(formulaX<-1)
                setDelay = (int) -(delay*formulaX);
            else
                setDelay = delay;

            if(ratioY>0)
                moving = -moving;

            startX = reboundX;
            startY = reboundY;
        }
    }
    /**
     * 게임을 중단하는 함수<br>
     * gameState를 false로 만듦
     */
    public void gameStop() { gameState = false; }
    /**
     * 현재 게임 상태(gameState)를 확인하는 함수<br>
     * gameState가 false일 경우 wait()
     */
    synchronized private void checkGameState() {
        if(!gameState) {
            try { this.wait(); }
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
     * attack을 움직인다
     */
    @Override
    public void run() {
        Point point = AimGraphic.getAimPoint();
        nextXY(point.x, point.y);
        try {
            Thread.sleep(i*ballDelay); // 볼 사이의 딜레이 만들기
        } catch (InterruptedException e1) {
            return;
        }

        while(true) {
            checkGameState(); // 게임이 진행 중인지 확인
            try {
                if(formulaX != 0) {
                    y -= moving;
                    if(directionX == 1)
                        x = (float) (x - moving*formulaX);
                    else
                        x= (float) (x + moving*formulaX);
                }
                else{
                    if(directionX == 1)
                        x = x - moving;
                    else
                        x = x + moving;
                }
                if(x<=0 || x>=gamePanel.getWidth())
                    directionX = -directionX;
                if(y<=0) {
                    formulaX = -formulaX;
                    moving = -moving;
                    //if(formulaX<0.1 && Math.random()>0.3) {
                    //	formulaX -= 0.3;
                    //}
                }
                if(y>=gamePanel.getHeight()) {
                    interrupt();
                }
                attack.setX(x);
                attack.setY(y);

                int count = gamePanel.getBlockCount();
                for(int i=0;i<count;i++) {
                    if(block[i]!=null && lastAttackBlock!=i && block[i].blockAttack(attack)) {
                        // 어택이 블럭에 맞았을 경우
                        ((MainActivity)MainActivity.mContext).playBallSound(0);
                        lastAttackBlock = i;

                        nextXY(reboundX, reboundY);

                        /*
                        if(block[i]!=null) {
                            float topY = block[i].getY() - y;
                            float bottomY = y - block[i].getY() + block[i].getHeight();
                            float leftX = x - block[i].getX();
                            float rightX = block[i].getX() + block[i].getWidth() - y;

                            if (topY > leftX && topY > rightX || bottomY > leftX && bottomY > rightX) {
                                // 위 아랫 면에 맞았을 경우
                                formulaX = -formulaX;
                                moving = -moving;
                            } else {
                                // 옆 면에 맞았을 경우
                                directionX = -directionX;
                            }
                        }
                         */

                        if(directionX==1)
                            directionX = -directionX;
                        else {
                            formulaX = -formulaX;
                            moving = -moving;
                        }


                        if(block[i] != null && gamePanel != null &&
                                block[i] instanceof GoneBlockInterface && ((GoneBlockInterface)block[i]).checkHitCount()) {
                            // 어택에 블럭이 맞아 사라졌을 경우
                            gamePanel.removeImage(block[i]);
                            block[i] = null;
                            ((MainActivity)MainActivity.mContext).playBallSound(1);
                            gamePanel.decreaseBlockCount();
                        }
                        break;
                    }
                }
                gamePanel.setAttackImage(attack);
                Thread.sleep(setDelay);
            } catch (InterruptedException e) {
                gamePanel.setDownAttack(this.i);
                return;
            }
        }
    }
}
