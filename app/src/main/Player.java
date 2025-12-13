package com.hartmann.coincatcher;

import android.graphics.Rect;

public class Player {
    public int x, y;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isColliding(Falling coin) {
        Rect playerRect = new Rect(x, y, x + 100, y + 100);
        Rect coinRect = new Rect(coin.x - 20, coin.y - 20, coin.x + 20, coin.y + 20);
        return Rect.intersects(playerRect, coinRect);
    }
                                 }
