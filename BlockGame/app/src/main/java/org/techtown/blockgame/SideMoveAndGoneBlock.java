package org.techtown.blockgame;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class SideMoveAndGoneBlock extends SideMoveBlock implements GoneBlockInterface {

    private int hitCount, score;
    private GameFragment gameFragment;

    public SideMoveAndGoneBlock() {
        super();
    }


    public void setBlock(int x ,int y ,int w ,int h ,int blockDown ,int imageID , int moveDelay,
                         int moveDirection, int score, int hitCount, GameFragment gameFragment) {
        super.setBlock(x,y,w,h,blockDown,imageID,moveDelay,moveDirection);
        this.score = score;
        this.hitCount = hitCount;
        this.gameFragment = gameFragment;
    }

    public SideMoveAndGoneBlock copyBlock() {
        SideMoveAndGoneBlock GoneBlock = new SideMoveAndGoneBlock();
        GoneBlock.setBlock(x,y,w,h,blockDown,imageID,moveDelay,moveDirection,score,hitCount,gameFragment);

        return GoneBlock;
    }

    /**
     * hitCount 점검하는 함수<br>
     * : hitCount가 0보다 작을 시 게임 score 증가시키기
     *
     * @return 블럭의 hitCount가 0보다 작거나 같을 시 true
     */
    @Override
    public boolean checkHitCount() {
        if(hitCount<=0) {
            gameFragment.increaseScore(score);
            return true;
        }
        return false;
    }
    /**
     * GoneBlock은 맞았는지 확인하고 맞았으면 hitCount 1 감소하고 true 리턴
     *
     * @param attack 블록에 맞았는지 비교한 label
     * @return 블록이 attack에 맞았을 시 true
     */
    @Override
    public boolean blockAttack(GrapicImage attack) {
        if(checkBlockMit(attack,0)) {
            hitCount--;
            return true;
        }
        return false;
    }

}

