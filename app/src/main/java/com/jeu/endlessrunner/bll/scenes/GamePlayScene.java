package com.jeu.endlessrunner.bll.scenes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.Timer;
import java.util.TimerTask;

import com.jeu.endlessrunner.be.Constants;
import com.jeu.endlessrunner.be.Floor;
import com.jeu.endlessrunner.be.PauseButton;
import com.jeu.endlessrunner.be.Player;
import com.jeu.endlessrunner.bll.IScene;
import com.jeu.endlessrunner.bll.managers.BirdManager;
import com.jeu.endlessrunner.bll.managers.GravityManager;
import com.jeu.endlessrunner.bll.managers.HealthManager;
import com.jeu.endlessrunner.bll.managers.ObstacleManager;
import com.jeu.endlessrunner.bll.managers.SceneManager;

public class GamePlayScene implements IScene {

    private final int X_POSITION = Constants.SCREEN_WIDTH / 4;
    private final int GRAVITY_THRESHOLD = 20;
    private final int UPDATE_TIMER_INTERVAL = 2000;

    private GravityManager mGravityManager;
    private ObstacleManager mObstacleManager;
    private BirdManager mBirdManager; // NOUVEAU
    private HealthManager mHealthManager;

    private Rect mTextRect;

    private PauseButton mPauseButton;

    private Player mPlayer;
    private Point mPlayerPoint;

    private Floor mFloor;

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

    public GamePlayScene() {
        newGame();
        mTextRect = new Rect();
    }

    @Override
    public void update() {
        if (!mGameOver && !mIsPaused) {
            mPlayer.update(mPlayerPoint, mIsJumping);
            mFloor.update();

            playerGravity();
            checkCollisionObstacle();
            checkCollisionBird();

            mObstacleManager.update();

            // Active et met à jour les oiseaux
            mBirdManager.setActive(mScore);
            mBirdManager.update();

            if (mHealthManager.update(mAmountOfDamage)) {
                mGameOver = true;
            }

            if (!mIsTimerStarted) {
                mScoreTimer.scheduleAtFixedRate(mScoreTimerTask, UPDATE_TIMER_INTERVAL, UPDATE_TIMER_INTERVAL);
                mIsTimerStarted = true;
            }
        }
    }

    /**
     * If the player is colliding with an obstacle, increase amountOfDamage.
     */
    private void checkCollisionObstacle() {
        if (mObstacleManager.collisionWithPlayer(mPlayer.getRect())) {
            mAmountOfDamage++;
        }
    }

    /**
     * NOUVEAU - If the player is colliding with a bird, increase amountOfDamage.
     */
    private void checkCollisionBird() {
        if (mBirdManager.collisionWithPlayer(mPlayer.getRect())) {
            mAmountOfDamage++;
        }
    }

    /**
     * Checks if the player is suspended in the air. If yes - adjust the gravity
     * accordingly.
     */
    private void playerGravity() {
        mPlayerPoint.set(mPlayerPoint.x, mPlayerPoint.y + (int) mGravity);

        // Si le joueur touche le sol, on le remet juste sur le sol (pas plus bas)
        int solY = Constants.SCREEN_HEIGHT - 300 - 10;
        if (mPlayerPoint.y > solY) {
            mPlayerPoint.y = solY;
            mGravity = 0;
            mIsJumping = false;
            mDoubleJumpAvailable = true;
        } else if (mIsJumping && mGravity < 0) {
            mGravity += 1.3;
        } else if (mIsJumping && mGravity < GRAVITY_THRESHOLD) {
            mGravity += 1.8f;
        }
    }

    /**
     * Adjust the gravity so the player "jumps".
     */
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
        canvas.drawColor(Color.parseColor("#e5faff"));

        mFloor.draw(canvas);
        mObstacleManager.draw(canvas);
        mBirdManager.draw(canvas); // NOUVEAU - Dessine les oiseaux
        mPlayer.draw(canvas);

        mPauseButton.draw(canvas, mIsPaused);

        Paint paint = new Paint();
        drawScore(canvas, paint);

        mHealthManager.draw(canvas);

        if (mGameOver) {
            paint.setTextSize(100);
            paint.setColor(Color.WHITE);
            paint.setShadowLayer(5, 0, 0, Color.BLACK);
            drawCenterText(canvas, paint, "Perdu !", "Score: " + mScore);
        }
    }

    /**
     * Draws the score on the screen.
     *
     * @param canvas
     * @param paint
     */
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
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
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
    }

    /**
     * Sets the scene ready for a new game.
     */
    private void newGame() {
        mGravityManager = new GravityManager();
        mObstacleManager = new ObstacleManager(300, 100, 100, Color.BLUE);

        // NOUVEAU - Initialisation du BirdManager
        mBirdManager = new BirdManager(
                800, // Gap entre les oiseaux
                60, // Hauteur des oiseaux
                80, // Largeur des oiseaux
                Color.RED // Couleur (temporaire, l'image sera utilisée)
        );

        mHealthManager = new HealthManager();
        mPauseButton = new PauseButton();

        int playerWidth = 100;
        int playerHeight = 100;

        // MODIFICATION : Position Y du joueur ajustée pour être sur la route
        int playerY = Constants.SCREEN_HEIGHT - 300 - playerHeight; // 300 = hauteur de votre route

        mPlayerPoint = new Point(X_POSITION, playerY);
        Rect playerRect = new Rect(
                X_POSITION,
                playerY,
                X_POSITION + playerWidth,
                playerY + playerHeight);
        mPlayer = new Player(playerRect, Color.BLACK);

        mFloor = new Floor(new Rect());

        mGravity = 0;
        mIsJumping = true;
        mDoubleJumpAvailable = true;
        mAllowedToJump = false;

        mGameOver = false;
        mIsPaused = false;

        mAmountOfDamage = 0;
        mScore = 0;

        mScoreTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (!mGameOver && !mIsPaused)
                    mScore++;
            }
        };
        mScoreTimer = new Timer(true);
    }

    /**
     * Takes the given text and draws it in the center of the screen.
     *
     * @param canvas
     * @param paint
     * @param textOne
     */
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