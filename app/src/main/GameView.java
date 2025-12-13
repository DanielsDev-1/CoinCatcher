package com.hartmann.coincatcher;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {
    private Thread thread;
    private boolean isPlaying;
    private Player player;
    private ArrayList<Falling> coins;
    private Paint paint;
    private GameState gameState;
    private Random random;

    public GameView(Context context) {
        super(context);
        player = new Player(300, 800);
        coins = new ArrayList<>();
        paint = new Paint();
        gameState = new GameState();
        random = new Random();
    }

    @Override
    public void run() {
        while (isPlaying) {
            update();
            draw();
            sleep();
        }
    }

    private void update() {
        if (coins.size() < 5) {
            coins.add(new Falling(random.nextInt(600), 0));
        }
        for (Falling coin : coins) {
            coin.y += 10;
            if (coin.y > getHeight()) {
                coins.remove(coin);
                break;
            }
            if (player.isColliding(coin)) {
                gameState.score++;
                coins.remove(coin);
                break;
            }
        }
    }

    private void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawColor(Color.BLACK);

            paint.setColor(Color.YELLOW);
            for (Falling coin : coins) {
                canvas.drawCircle(coin.x, coin.y, 20, paint);
            }

            paint.setColor(Color.BLUE);
            canvas.drawRect(player.x, player.y, player.x + 100, player.y + 100, paint);

            paint.setColor(Color.WHITE);
            paint.setTextSize(50);
            canvas.drawText("Score: " + gameState.score, 50, 100, paint);

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        player.x = (int) event.getX() - 50;
        return true;
    }
  }
