package org.techtown.blockgame;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    public static Context mContext;
    ConstraintLayout container;
    org.techtown.blockgame.MainFragment mainFragment;
    org.techtown.blockgame.GameFragment gameFragment;
    org.techtown.blockgame.xmlSelectFragment xmlFragment;
    org.techtown.blockgame.GameThread gameThread;
    org.techtown.blockgame.GameGroundPanel groundPanel;

    Music gameMusic[] = new Music[3];
    Music music = new Music();
    boolean bgmValue=false, gamePause=false, gameValue =false;
    Handler handler = new Handler();

    String xmlFileName = "block2.xml";

    final int STEP_SIZE = 0;
    final int STEP_InitBg = 1;
    final int STEP_GameFont = 2;
    final int STEP_GameSentence = 3;
    final int STEP_GameBg = 4;
    final int STEP_BallSound = 5;
    final int STEP_GameSound = 6;
    final int STEP_Attack = 7;
    final int STEP_Aim = 8;
    final int STEP_User = 9;
    final int STEP_FinalScore = 10;
    final int STEP_Block = 11;
    final int STEP_Obj = 12;

    String sentence[] = new String[3];
    String ballSound[] = new String[3];
    String gameSound[] = new String[3];
    Point size = new Point();
    int width = 0, height = 0, selectSentece = 0;
    int initBgID = 0, gameBgID = 0, attackImgID = 0, attackUserID = 0, userImgID = 0;
    int attackCount = -1, attackDelay = -1, ballCountDelay = -1;
    String attackImg = null, attackUserImg = null, userImg = null;
    int life = 0, finalScore = 0;
    Point userSize = new Point();
    int fontColor[] = new int[3];
    int aimColor[] = new int[3];

    DontGoneBlock block[] = new DontGoneBlock[100000];
    int blockCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        container = findViewById(R.id.container);
        mainFragment = (org.techtown.blockgame.MainFragment) getSupportFragmentManager().findFragmentById(R.id.mainFragment);
        gameFragment = new org.techtown.blockgame.GameFragment();
        xmlFragment = new org.techtown.blockgame.xmlSelectFragment();
        xmlRead();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        int x,y;
        x = container.getWidth();
        y = container.getHeight();

        if(x>size.x)
            size.x = x;
        if(y>size.y)
            size.y = y;

        setScreenSize();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void setScreenSize(){

        if(size.x <= 0 || size.y <=0)
            return;
        Log.d("size","W = "+width +", H = "+height+" || size : "+size.x+", "+size.y);

        if(width > size.x)
            width = size.x;
        if(height > size.y)
            height = size.y;

        Log.d("size","W = "+width +", H = "+height+" || size : "+size.x+", "+size.y);

        container.getLayoutParams().width = width;
        container.getLayoutParams().height = height;

        container.setTop((size.y - height)/2);
    }

    public void setXmlFileName(String name) {
        gameFragment = new org.techtown.blockgame.GameFragment();
        xmlFileName = name;
        xmlRead();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int curId = item.getItemId();

        switch (curId) {
            case R.id.menu_play:
                if(!gameValue)
                    onFragmentChange(1);
                else
                    onFragmentChange(0);
                break;
            case R.id.menu_pause:
                if(gameValue) {
                    if (!gamePause) {
                        music.stopAudio();
                        gameThread.gameStop();
                        groundPanel = findViewById(R.id.gameGround);
                        groundPanel.gameStop();
                        gamePause = true;
                    } else {
                        if (bgmValue)
                            music.playAudio();
                        gameThread.startGame();
                        groundPanel.gameRePlay();
                        gamePause = false;
                    }
                }
                break;
            case R.id.menu_music:
                if(!bgmValue) {
                    music.playAudio();
                    bgmValue = true;
                }
                else {
                    music.stopAudio();
                    bgmValue = false;
                }
                break;
            case R.id.menu_xmlSelect:
                onFragmentChange(2);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void onFragmentChange(int index) {
        if (index == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
            showToast("Game Init");
            setXMLInitPanel();
        } else if (index == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, gameFragment).commit();
            showToast("Game Start");
            setXMLGamePanel();

            music.stopAudio();
            music.setMusicString(gameSound[2]);
            if(bgmValue)
                music.playAudio();
        }
        else if (index == 2) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, xmlFragment).commit();
        }
    }

    public void playBallSound(int type) {
        // 0==hit 1==remove 2==die
        //new Music(ballSound[type]).playAudio();
        handler.post(new Runnable() {
            @Override
            public void run() {
                gameMusic[type].playAudio();
            }
        });

    }

    public boolean getGameValue(){
        return gameValue;
    }

    public void gameResult(int score) {
        if (score >= finalScore) {
            selectSentece = 1;
            music.setMusicString(gameSound[0]);
            music.playAudio();
        }
        else {
            selectSentece = 2;
            music.setMusicString(gameSound[1]);
            music.playAudio();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
        setXMLInitPanel();
        gameThread.interrupt();
    }

    public void setXMLGamePanel() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        gameFragment.setBackground(gameBgID);
                        gameFragment.setAttack(attackCount, attackDelay, ballCountDelay, attackImgID);
                        gameFragment.setUser(attackUserID, userImgID, userSize, life);
                        for (int i = 0; i < blockCount; i++) {
                            gameFragment.createDontGoneBlock(block[i].copyBlock());
                        }
                        gameFragment.setAimColor(aimColor[0], aimColor[1], aimColor[2]);
                        gameFragment.serFinalScore(finalScore);
                        gameThread = new GameThread(gameFragment.gameGround);
                        gameThread.start();
                        gameValue = true;
                    }
                });
            }
        });
    }

    public void setXMLInitPanel() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                mainFragment.setInitBackground(initBgID);
                mainFragment.setSentence(sentence[selectSentece]);
                mainFragment.setFontColor(fontColor[0], fontColor[1], fontColor[2]);
                gameValue = false;
            }
        });
    }

    public static int ConvertDPtoPX(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public static void log(String s, String s2){
        Log.d(s,s2);
    }

    public void xmlRead() {
        AssetManager am = getResources().getAssets();
        InputStream is = null;

        try {
            // XML 파일 스트림 열기
            is = am.open(xmlFileName);

            // XML 파서 초기화
            XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();

            // XML 파서에 파일 스트림 지정.
            parser.setInput(is, "UTF-8");

            int eventType = parser.getEventType();
            int step = -1;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    // XML 데이터 시작
                } else if (eventType == XmlPullParser.START_TAG) {
                    String startTag = parser.getName();
                    if (startTag.equals("Size")) {
                        step = STEP_SIZE;

                        int titleSize = ConvertDPtoPX(mContext,40);

                        width = Integer.parseInt(parser.getAttributeValue(null, "w"));
                        height = Integer.parseInt(parser.getAttributeValue(null, "h"))+titleSize;
                        Log.d("title", String.valueOf(titleSize));
                        Log.d("title", String.valueOf(height));
                        //width = ConvertDPtoPX(this, width);
                        //height = ConvertDPtoPX(this, height);

                        setScreenSize();

                    } else if (startTag.equals("InitBg")) {
                        step = STEP_InitBg;
                    } else if (startTag.equals("Font")) {
                        step = STEP_GameFont;

                        fontColor[0] = Integer.parseInt(parser.getAttributeValue(null, "r"));
                        fontColor[1]  = Integer.parseInt(parser.getAttributeValue(null, "g"));
                        fontColor[2]  = Integer.parseInt(parser.getAttributeValue(null, "b"));

                    } else if (startTag.equals("GameSentence")) {
                        step = STEP_GameSentence;

                        selectSentece = 0;
                        sentence[0] = parser.getAttributeValue(null, "start");
                        sentence[1] = parser.getAttributeValue(null, "win");
                        sentence[2] = parser.getAttributeValue(null, "lose");
                    } else if (startTag.equals("GameBg")) {
                        step = STEP_GameBg;
                    } else if (startTag.equals("BallSound")) {
                        step = STEP_BallSound;

                        ballSound[0] = parser.getAttributeValue(null, "hitSound");
                        ballSound[1] = parser.getAttributeValue(null, "removeSound");
                        ballSound[2] = parser.getAttributeValue(null, "dieSound");

                        gameMusic[0] = new Music(parser.getAttributeValue(null, "hitSound"));
                        gameMusic[1] = new Music(parser.getAttributeValue(null, "removeSound"));
                        gameMusic[2] = new Music(parser.getAttributeValue(null, "dieSound"));

                    }else if (startTag.equals("GameSound")) {
                        step = STEP_GameSound;

                        gameSound[0] = parser.getAttributeValue(null, "winEndSound");
                        gameSound[1] = parser.getAttributeValue(null, "loseEndSound");
                        gameSound[2] = parser.getAttributeValue(null, "backGroundSound");

                        music.setMusicString(gameSound[2]);

                    } else if (startTag.equals("Attack")) {
                        step = STEP_Attack;
                        attackCount = Integer.parseInt(parser.getAttributeValue(null, "count"));
                        attackDelay = Integer.parseInt(parser.getAttributeValue(null, "delay"));
                        ballCountDelay = Integer.parseInt(parser.getAttributeValue(null, "ballCountDelay"));

                        attackImg = parser.getAttributeValue(null, "img");
                        attackImgID = getResources().getIdentifier("@drawable/" + attackImg, "drawable", this.getPackageName());
                    } else if (startTag.equals("Aim")) {
                        step = STEP_Aim;

                        aimColor[0] = Integer.parseInt(parser.getAttributeValue(null, "r"));
                        aimColor[1] = Integer.parseInt(parser.getAttributeValue(null, "g"));
                        aimColor[2] = Integer.parseInt(parser.getAttributeValue(null, "b"));

                    } else if (startTag.equals("User")) {
                        step = STEP_User;

                        userSize.x = Integer.parseInt(parser.getAttributeValue(null, "w"));
                        userSize.y = Integer.parseInt(parser.getAttributeValue(null, "h"));
                        //userSize.x = ConvertDPtoPX(this, userSize.x);
                        //userSize.y = ConvertDPtoPX(this, userSize.y);

                        life = Integer.parseInt(parser.getAttributeValue(null, "life"));

                        attackUserImg = parser.getAttributeValue(null, "attackImg");
                        attackUserID = getResources().getIdentifier("@drawable/" + attackUserImg, "drawable", this.getPackageName());
                        userImg = parser.getAttributeValue(null, "img");
                        userImgID = getResources().getIdentifier("@drawable/" + userImg, "drawable", this.getPackageName());
                    } else if (startTag.equals("FinalScore")) {
                        step = STEP_FinalScore;

                        finalScore = Integer.parseInt(parser.getAttributeValue(null, "winScore"));
                    } else if (startTag.equals("Block")) {
                        step = STEP_Block;

                        blockCount=0;

                        Log.d("blockStart", "blockStart");
                    } else if (startTag.equals("Obj")) {
                        step = STEP_Obj;
                        int x = 0, y = 0, w = 0, h = 0, score = 0, hitCount = 0, blockDown = 0, moveDelay = 0, moveDirection = 0, blockImgID = 0;
                        String blockImg;

                        String type = parser.getAttributeValue(null, "type");
                        x = Integer.parseInt(parser.getAttributeValue(null, "x"));
                        y = Integer.parseInt(parser.getAttributeValue(null, "y"));
                        w = Integer.parseInt(parser.getAttributeValue(null, "w"));
                        h = Integer.parseInt(parser.getAttributeValue(null, "h"));
                        /*
                        x = ConvertDPtoPX(this, x);
                        y = ConvertDPtoPX(this, y);
                        w = ConvertDPtoPX(this, w);
                        h = ConvertDPtoPX(this, h);
                        */
                        blockDown = Integer.parseInt(parser.getAttributeValue(null, "blockDown"));

                        blockImg = parser.getAttributeValue(null, "img");
                        blockImgID = getResources().getIdentifier("@drawable/" + blockImg, "drawable", this.getPackageName());

                        if (type.equals("move") || type.equals("moveAndGone")) {
                            moveDelay = Integer.parseInt(parser.getAttributeValue(null, "moveDelay"));
                            moveDirection = Integer.parseInt(parser.getAttributeValue(null, "moveDirection"));
                        }
                        if (type.equals("gone") || type.equals("moveAndGone")) {
                            score = Integer.parseInt(parser.getAttributeValue(null, "score"));
                            hitCount = Integer.parseInt(parser.getAttributeValue(null, "hitCount"));
                        }

                        if (type.equals("dontGone")) {
                            block[blockCount] = new DontGoneBlock();
                            block[blockCount].setBlock(x, y, w, h, blockDown, blockImgID);
                        } else if (type.equals("move")) {
                            block[blockCount] = new SideMoveBlock();
                            ((SideMoveBlock) block[blockCount]).setBlock(x, y, w, h, blockDown, blockImgID, moveDelay, moveDirection);
                        } else if (type.equals("gone")) {
                            block[blockCount] = new GoneBlock();
                            ((GoneBlock) block[blockCount]).setBlock(x, y, w, h, blockDown, blockImgID, score, hitCount, gameFragment);
                        } else if (type.equals("moveAndGone")) {
                            block[blockCount] = new SideMoveAndGoneBlock();
                            ((SideMoveAndGoneBlock) block[blockCount]).setBlock(x, y, w, h, blockDown, blockImgID, moveDelay, moveDirection, score, hitCount, gameFragment);
                        } else
                            continue;

                        blockCount++;
                    } else {
                        step = -1;
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    String endTag = parser.getName();
                    if ((endTag.equals("InitBg") && step != STEP_InitBg || endTag.equals("GameSentence") && step != STEP_GameSentence ||
                            endTag.equals("GameBg") && step != STEP_GameBg || endTag.equals("Attack") && step != STEP_Attack ||
                            endTag.equals("User") && step != STEP_User || endTag.equals("FinalScore") && step != STEP_FinalScore)) {
                        // TODO : error
                    } else if (endTag.equals("Block")) {
                        //blockCount++;
                        Log.d("blockcount", String.valueOf(blockCount));
                    }
                    step = -1;
                } else if (eventType == XmlPullParser.TEXT) {
                    String text = parser.getText();
                    if (step == STEP_InitBg) {
                        String initBg = text;
                        initBgID = getResources().getIdentifier("@drawable/" + initBg, "drawable", this.getPackageName());
                        Log.d("initText", initBg);
                    } else if (step == STEP_GameBg) {
                        String gameBg = text;
                        gameBgID = getResources().getIdentifier("@drawable/" + gameBg, "drawable", this.getPackageName());
                        Log.d("gameBg", gameBg);
                    }
                }

                eventType = parser.next();
            }

            if (initBgID == 0 || gameBgID == 0 ||
                    attackImg == null || attackCount == -1 || attackDelay == -1 || ballCountDelay == -1 ||
                    attackUserImg == null || userImg == null || life == 0 || finalScore == 0) {
                // ERROR : XML is invalid.
            } else {
                setXMLInitPanel();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}