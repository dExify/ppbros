package inf112.ppbros.model;

import java.util.ArrayList;
import java.util.Timer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

import inf112.ppbros.controller.AudioController;
import inf112.ppbros.model.Entity.EnemyModel;
import inf112.ppbros.model.Entity.PlayerModel;
import inf112.ppbros.model.Platform.PlatformGrid;
import inf112.ppbros.model.Platform.PlatformGridMaker;
import inf112.ppbros.view.ScreenView;
import inf112.ppbros.view.StartMenuView;

public class GameModel extends Game {
    private PlayerModel player;
    private EnemyModel enemy;
    private int cameraPos;
    private Timer timer;
    private CameraYPos timerTask;
    private PlatformGridMaker platformGridMaker;
    private PlatformGrid platformGrid;
    private ArrayList<Rectangle> hitboxes;
    
    public GameModel() { // change later so it has the background and platform as parameters
        this.setScreen(new StartMenuView(this));
        this.cameraPos = 0;
        this.platformGridMaker = new PlatformGridMaker();
        this.timer = new Timer();
        this.timerTask = new CameraYPos();
        this.hitboxes = new ArrayList<Rectangle>();
    }

    public PlayerModel getPlayer() {
        return player;
    }

    /**
     * Creates an instance of player with start values
     * @param startX start x value
     * @param startY start y value
     */
    public void makePlayer(int startX, int startY) {
        this.player = new PlayerModel(startX, startY);
    }

    /** Moves player to the left based on its speed */
    public void movePlayerLeft(float delta) { // for å bevege karakter når man bruker wasd
        float prevX = player.getX();
        player.move(-delta * player.getSpeed(), 0);
        if (platformCollision()) {
            player.setX(prevX);
        } 
        // TODO: Check if player collides with enemies
    }

    /** Moves player to the right based on its speed */
    public void movePlayerRight(float delta) {
        float prevX = player.getX();
        player.move(delta * player.getSpeed(), 0);
        if (platformCollision()) {
            player.setX(prevX);
        } 
          // TODO: Check if player collides with enemies
    }


    /** Moves player to the up based on its speed */
    public void movePlayerUp(float delta) {
        float prevY = player.getY();
        player.move(0, delta * player.getSpeed());
        if (platformCollision()) {
            player.setY(prevY);
        } 
         // TODO: Check if player collides with enemies
    }


    /** Moves player to the down based on its speed */
    public void movePlayerDown(float delta) {
        float prevY = player.getY();
        player.move(0, -delta * player.getSpeed());
        if (platformCollision()) {
            player.setY(prevY);
        }
        // TODO: Check if player collides with enemies
    }

    /**
     * Checks to see if player lands a hit on an enemy
     * @return true if player can land a hit, false if they cannot
     */
    public boolean canPlayerAttack() {
        return player.canAttack(enemy);
    }
   
    /**
     * Player attacks enemy and enemy takes damage.
     * When enemy no longer has more health they die.
     */
    public void playerAttacksEnemy() {
        enemy.takeDamage(player.getAttackDmg());
        // TODO: do a check for when the enemy no longer has hp and update score/kill count
    }

    /**
     * Player is hit by enemy and takes damage.
     * When player no longe has more health they die.
     */
    public void playerIsHit() {
        // TODO: do a check if player collision box and enemy collision box is overlapping, if so player looses damage
    }

    /**
     * Checks if player is out of bounds left, right and bottom of screeen.
     * @return true if player is out of bounds, false if they are within bounds
     */
    public boolean isOutOfBounds() {
        float playerX = player.getX();
        float playerY = player.getY();
        return (playerX + player.getWidth() < 0) || (playerX > Gdx.graphics.getWidth() || 
        playerY + player.getHeight() < getCameraYCoordinate() - Gdx.graphics.getHeight()/2);
    }

    private boolean platformCollision() {
        for (Rectangle rec : hitboxes) {
            if (player.collidesWith(rec)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void create() {
        this.setScreen(new ScreenView(this));
    }

    /**
     * Builds a platform grid and returns the platformGrid object
     * @return PlatformGrid
     */
    public PlatformGrid getNextPlatformGrid() {
        platformGrid = platformGridMaker.getNextPlatformGrid();
        hitboxes.addAll(platformGrid.getHitboxes());
        return platformGrid;
    }

    /**
     * Returns an integer that represents the x coordiante of the viewport
     * @return int
     */
    public int getCameraYCoordinate() {
        cameraPos = timerTask.getCameraPos();
        return cameraPos;
    }

    public void startTimer() {
        timer.scheduleAtFixedRate(timerTask, 0, 13);
    }

    public void stopTimer() {
        timerTask.cancel();
    }

    public void dispose() {
        timer.cancel();
    }
}
