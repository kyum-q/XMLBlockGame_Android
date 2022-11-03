package org.techtown.blockgame;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.w3c.dom.Text;

/**
 * 게임의 움직이지 않지만 사라지는 블록 이미지 레이블 <br>
 * (extends DontGoneBlock implements GoneBlockInterface)
 *
 * @author 김경미
 */
public class GoneBlock extends DontGoneBlock implements GoneBlockInterface {

    private int hitCount=0, score=0;
    private GameFragment gameFragment;

    public GoneBlock() {
        super();
    }

    public void setBlock(int x , int y , int w , int h , int blockDown , int imageID ,
                         int score, int hitCount, GameFragment gameFragment){
        super.setBlock(x,y,w,h,blockDown,imageID);
        this.score = score;
        this.hitCount = hitCount;
        this.gameFragment = gameFragment;
    }

    public GoneBlock copyBlock() {
        GoneBlock GoneBlock = new GoneBlock();
        GoneBlock.setBlock(x,y,w,h,blockDown,imageID,score,hitCount,gameFragment);

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
            MainActivity.log("hit",getX()+", "+getY());
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
            MainActivity.log("attack",getX()+", "+getY());
            return true;

        }
        return false;
    }
}

