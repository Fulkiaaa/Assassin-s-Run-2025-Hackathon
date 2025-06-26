// GamePlayScene.java - Version complète avec niveau 3 : templiers + dagues

package vigneshgbe.endlessrunner.bll.scenes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.Timer;
import java.util.TimerTask;

import vigneshgbe.endlessrunner.R;
import vigneshgbe.endlessrunner.be.Constants;
import vigneshgbe.endlessrunner.be.Floor;
import vigneshgbe.endlessrunner.be.PauseButton;
import vigneshgbe.endlessrunner.be.Player;
import vigneshgbe.endlessrunner.be.ScrollingBackground;
import vigneshgbe.endlessrunner.bll.IScene;
import vigneshgbe.endlessrunner.bll.managers.BirdManager;

import vigneshgbe.endlessrunner.bll.managers.GravityManager;
import vigneshgbe.endlessrunner.bll.managers.HealthManager;
import vigneshgbe.endlessrunner.bll.managers.ObstacleManager;
import vigneshgbe.endlessrunner.bll.managers.TemplarManager;
import vigneshgbe.endlessrunner.bll.managers.SceneManager;

public class GamePlayScene implements IScene {

    private final int X_POSITION = Constants.SCREEN_WIDTH / 4;
    private final int GRAVITY_THRESHOLD = 20;
    private final int UPDATE_TIMER_INTERVAL = 2000;

    private GravityManager mGravityManager;
    private ObstacleManager mObstacleManager;
    private HealthManager mHealthManager;

    private BirdManager mBirdManager;
    private TemplarManager mTemplarManager;

    private Rect mTextRect;
    private PauseButton mPauseButton;
    private Player mPlayer;
    private Point mPlayerPoint;
    private Floor mFloor;
    private ScrollingBackground mBackground;

    private float mGravity;
    private boolean mIsJumping;
    private boolean mDoubleJumpAvailable;
    private boolean mAllowedToJump;
    private boolean mGameOver;
    private boolean mIsPaused;
    private int mAmountOfDamage;
    private int mScore;
    private boolean mIsTimerStarted;
    private TimerTask mScoreTimerTask;
    private Timer mScoreTimer;
    private boolean mShowLevel2Message = false;
    private long mLevel2MessageStartTime = 0;
    private boolean mShowLevel3Message = false;
    private long mLevel3MessageStartTime = 0;

    public GamePlayScene() {
        newGame();
        mTextRect = new Rect();
        Bitmap bgBitmap = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.background);
        mBackground = new ScrollingBackground(bgBitmap, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, 5);
    }

    @Override
    public void update() {
        if (!mGameOver && !mIsPaused) {
            mBackground.update();
            mPlayer.update(mPlayerPoint, mIsJumping);
            mPlayer.update();
            mFloor.update();

            if (mScore >= 15) {
                mBirdManager.update();
                if (mBirdManager.checkCollision(mPlayer.getRect())) {
                    mAmountOfDamage++;
                }
                if (mScore == 15 && !mShowLevel2Message) {
                    mShowLevel2Message = true;
                    mLevel2MessageStartTime = System.currentTimeMillis();
                }
            }

            if (mScore >= 30) {
                mTemplarManager.update();
                if (mTemplarManager.checkCollision(mPlayer.getRect(), mObstacleManager.getObstacleRects())) {
                    mAmountOfDamage++;
                }
                if (mScore == 30 && !mShowLevel3Message) {
                    mShowLevel3Message = true;
                    mLevel3MessageStartTime = System.currentTimeMillis();
                }
            }

            playerGravity();
            checkCollisionObstacle();
            mObstacleManager.update();

            if (mHealthManager.update(mAmountOfDamage)) {
                mGameOver = true;
            }

            if (!mIsTimerStarted) {
                mScoreTimer.scheduleAtFixedRate(mScoreTimerTask, UPDATE_TIMER_INTERVAL, UPDATE_TIMER_INTERVAL);
                mIsTimerStarted = true;
            }
        }
    }

    private void checkCollisionObstacle() {
        if (mObstacleManager.collisionWithPlayer(mPlayer.getRect())) {
            mAmountOfDamage++;
        }
    }

    private void playerGravity() {
        mPlayerPoint.set(mPlayerPoint.x, mPlayerPoint.y + (int) mGravity);
        if (!mAllowedToJump && !mGravityManager.isPlayerNotTouchingFloor(mPlayer, mFloor)) {
            mGravity = 0;
            mIsJumping = false;
            mDoubleJumpAvailable = true;
            mPlayerPoint.set(mPlayerPoint.x, mFloor.getRect().top - mPlayer.getRect().height() / 2 + 50);

        } else if (mIsJumping && mGravity < 0) {
            mGravity += 1.3;
        } else if (mIsJumping && mGravity < GRAVITY_THRESHOLD) {
            mGravity += 1.8f;
        }
    }

    private void jump() {
        if (!mIsJumping) {
            mGravity = -GRAVITY_THRESHOLD;
            mIsJumping = true;
        } else if (mIsJumping && mDoubleJumpAvailable) {
            mGravity = -GRAVITY_THRESHOLD;
            mDoubleJumpAvailable = false;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        mBackground.draw(canvas);
        mFloor.draw(canvas);
        mObstacleManager.draw(canvas);

        if (mScore >= 15)
            mBirdManager.draw(canvas);
        if (mScore >= 30)
            mTemplarManager.draw(canvas);

        mPlayer.draw(canvas);
        mPauseButton.draw(canvas, mIsPaused);

        Paint paint = new Paint();
        drawScore(canvas, paint);
        mHealthManager.draw(canvas);

        if (mShowLevel2Message && System.currentTimeMillis() - mLevel2MessageStartTime < 1000) {
            drawLevelMessage(canvas, "Niveau 2 - attention aux oiseaux", Color.YELLOW);
        } else {
            mShowLevel2Message = false;
        }

        if (mShowLevel3Message && System.currentTimeMillis() - mLevel3MessageStartTime < 1000) {
            drawLevelMessage(canvas, "Niveau 3 - attention aux templiers", Color.RED);
        } else {
            mShowLevel3Message = false;
        }

        if (mGameOver) {
            paint.setTextSize(100);
            paint.setColor(Color.WHITE);
            paint.setShadowLayer(5, 0, 0, Color.BLACK);
            drawCenterText(canvas, paint, "PERDU !", "Score: " + mScore);
        }
    }

    private void drawLevelMessage(Canvas canvas, String text, int color) {
        Paint msgPaint = new Paint();
        msgPaint.setColor(color);
        msgPaint.setTextSize(80);
        msgPaint.setTextAlign(Paint.Align.CENTER);
        msgPaint.setShadowLayer(5, 0, 0, Color.BLACK);
        canvas.drawText(text, Constants.SCREEN_WIDTH / 2f, Constants.SCREEN_HEIGHT / 2f, msgPaint);
    }

    private void drawScore(Canvas canvas, Paint paint) {
        paint.setTextSize(100);
        paint.setColor(Color.WHITE);
        paint.setShadowLayer(5, 0, 0, Color.BLACK);
        canvas.drawText("Score: " + mScore, 50, 50 + paint.descent() - paint.ascent(), paint);
    }

    @Override
    public void terminate() {
    }

    @Override
    public void recieveTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (mPauseButton.getRect().contains((int) event.getX(), (int) event.getY())) {
                mIsPaused = !mIsPaused;
            } else if (!mGameOver) {
                jump();
            } else {
                SceneManager.ACTIVE_SCENE = Constants.MENU_SCENE;
                mScoreTimer.cancel();
                mIsTimerStarted = false;
                newGame();
            }
        }
    }

    private void newGame() {
        mGravityManager = new GravityManager();
        mFloor = new Floor(new Rect());
        mObstacleManager = new ObstacleManager(300, 100, 100, Color.BLUE, mFloor);
        mHealthManager = new HealthManager();

        mBirdManager = new BirdManager(mFloor);

        mTemplarManager = new TemplarManager(mFloor);

        mPauseButton = new PauseButton();
        mPlayer = new Player(new Rect(0, 0, 100, 100), Color.BLACK);
        mPlayerPoint = new Point(X_POSITION, Constants.SCREEN_HEIGHT / 2);

        mGravity = 0;
        mIsJumping = true;
        mDoubleJumpAvailable = true;
        mAllowedToJump = false;
        mGameOver = false;
        mIsPaused = false;
        mAmountOfDamage = 0;
        mScore = 0;
        mShowLevel2Message = false;
        mShowLevel3Message = false;

        mScoreTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (!mGameOver && !mIsPaused) {
                    mScore++;
                }
            }
        };
        mScoreTimer = new Timer(true);
    }

    private void drawCenterText(Canvas canvas, Paint paint, String textOne, String textTwo) {
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.getClipBounds(mTextRect);
        int cHeight = mTextRect.height();
        int cWidth = mTextRect.width();
        paint.getTextBounds(textOne, 0, textOne.length(), mTextRect);
        float x = cWidth / 2f;
        float y = cHeight / 2f - 50;
        canvas.drawText(textOne, x, y, paint);
        y = cHeight / 2f + 50;
        canvas.drawText(textTwo, x, y, paint);
    }
}
