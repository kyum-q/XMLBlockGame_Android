package org.techtown.blockgame;

import android.view.View;

/**
 * 맞으면 사라지는 블록일 경우에 필요한 함수를 가진 interface
 *
 * @author 김경미
 */
public interface GoneBlockInterface {
    /**
     * hitCount 점검하는 함수<br>
     * : hitCount가 0보다 작을 시 게임 score 증가시키기
     *
     * @return 블럭의 hitCount가 0보다 작거나 같을 시 true
     */
    public boolean checkHitCount();
    /**
     * GoneBlock은 맞았는지 확인하고 맞았으면 hitCount 1 감소하고 true 리턴
     *
     * @param attack 블록에 맞았는지 비교한 label
     * @return 블록이 attack에 맞았을 시 true
     */
    public boolean blockAttack(GrapicImage attack);
}

