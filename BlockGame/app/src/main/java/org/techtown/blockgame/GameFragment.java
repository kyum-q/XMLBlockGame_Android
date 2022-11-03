package org.techtown.blockgame;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class GameFragment extends Fragment {

    private int finalScore;
    org.techtown.blockgame.GameGroundPanel gameGround;
    org.techtown.blockgame.AttackImage attack[];
    org.techtown.blockgame.AttackThread attackThread[];
    org.techtown.blockgame.UserImage user;
    org.techtown.blockgame.AimGraphic aim;
    org.techtown.blockgame.GameView gameView;
    View gameBg;
    TextView lifeView, scoreView;

    int score=0, life=0;
    int attackUserID=0, userImgID=0, maxLife=0;
    int attackCount=0, attackDelay=0, ballCountDelay=0, attackImgID = 0, bgID = 0;
    // 메모리에 올리면서 연결해준다
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_game, container, false);

        gameGround = rootView.findViewById(R.id.gameGround);
        gameBg = rootView.findViewById(R.id.gameBg);
        user = rootView.findViewById(R.id.user);
        aim = rootView.findViewById(R.id.aim);
        lifeView = rootView.findViewById(R.id.lifeView);
        scoreView = rootView.findViewById(R.id.scoreView);
        gameView = rootView.findViewById(R.id.gameView);

        aim.setGroundPanel(gameGround);
        gameGround.setGameGround(this,gameView);

        return rootView;
    }

    public void setBackground(int bgID) {
        this.bgID =bgID;
        if(gameBg!=null)
            gameBg.setBackgroundResource(bgID);
    }

    public void setAttack(int count, int delay, int ballCountDelay, int attackImg) {
        this.attackCount = count;
        this.attackDelay = delay;
        this.ballCountDelay = ballCountDelay;
        this.attackImgID = attackImg;
        attack = new AttackImage[attackCount];
        attackThread = new AttackThread[attackCount];
        for(int i=0; i<attackCount;i++) {
            int dp = MainActivity.ConvertDPtoPX(getContext(),20);
            attack[i] = new AttackImage(delay, ballCountDelay, dp);
            //gameGround.addView(attack[i],dp,dp);
            attack[i].setX(gameGround.getWidth()/2-dp/2);
            attack[i].setY(gameGround.getHeight() - attack[i].getH());
            Log.d("gameGround",gameGround.getHeight()+" ," +dp);
            Handler handler = new Handler();
            int finalI = i;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    attack[finalI].setImage(attackImgID);
                    gameView.addImage(attack[finalI]);
                }
            });
        }
        gameGround.setAttack(attack,attackThread,attackCount,aim);
        aim.setAttackLocation(attack[0]);
        //Log.d("gemeFragment", String.valueOf(attackImgID));
    }

    public void setUser(int attackUserID, int userImgID, Point size, int life) {
        this.attackUserID = attackUserID;
        this.userImgID = userImgID;

        user.getLayoutParams().width = size.x;
        user.getLayoutParams().height = size.y;

        this.maxLife = this.life = life;
        String s ="";
        for(int i=0;i<life;i++)
            s+="♥";
        lifeView.setText(s);
        if(user != null)
            user.setImage(attackUserID,userImgID,life);
    }

    public void setUserImg(int i){
        user.setUserImg(i);
    }

    /**
     * user의 위치를 공격 후 attack이 맨 처음으로 떨어진 위치로 이동시키는 함수
     */
    public void setUserLocation(AttackImage attack) {
        if(((MainActivity)MainActivity.mContext).getGameValue()) {
            user.setUserImg(0);
            user.setX(attack.getX() - user.getWidth() / 2);
        }
    }

    public void setAimColor(int r,int g,int b){
        aim.setAimColor(r,g,b);
    }

    public void serFinalScore(int finalScore) {
        this.finalScore = finalScore;
        this.score = 0;
        scoreView.setText(" Score: "+ score);
    }

    /**
     * score를 n만큼 증가 시키는 함수
     *
     * @param n 은 증가되는 score값인 정수형 인수
     */
    public void increaseScore(int n) {
        score += n;
        //((MainActivity)MainActivity.mContext).setScore(" Score: "+ score);
        scoreView.setText(" Score: "+ score);
        if(checkScore())
            ((MainActivity)MainActivity.mContext).gameResult(score);
    }
    /**
     * score 확인하는 함수 (승패 여부 결정)
     *
     * @return user의 점수가 승리점수를 넘었는지 확인하고 리턴하는 논리형 인자
     */
    private boolean checkScore() {
        if(score >= finalScore)
            return true;
        return false;
    }
    /**
     * life를 감소시키고 life가 모두 없어졌을 시 패배로 게임 종료 시키는 함수
     */
    public void decreaseLife() {
        life--;
        if(life<=0) {
            ((MainActivity)MainActivity.mContext).gameResult(score);
        }
        else {
            String s ="";
            for(int i=0;i<life;i++)
                s +="♥";
            //((MainActivity)MainActivity.mContext).setLife(s);
            lifeView.setText(s);
        }
    }

    public void createDontGoneBlock(DontGoneBlock newBlock) {
        if(newBlock != null) {
            gameGround.createDontGoneBlock(newBlock);
        }
    }


}