package org.techtown.blockgame;

/**
 * 	game을 움직이는 Thread
 *
 * @author 김경미
 */
public class GameThread extends Thread {
    private GameGroundPanel gamePanel = null;
    private boolean gameState = true;
    /**
     * GameThread 생성자
     *
     * @param gamePanel gamePanel
     */
    public GameThread(GameGroundPanel gamePanel) {
        this.gamePanel = gamePanel;
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
     * Thread의 run 함수
     */
    @Override
    public void run() {
        gamePanel.attack();
        while(true) {
            checkGameState();
            try {
                if(gamePanel.getReady()==-1) {
                    gamePanel.attack();
                    gamePanel.setBlockDown();
                }
                sleep(600);
            } catch (InterruptedException e) {
                gamePanel.gameOver();
                return;
            }
        }
    }
}