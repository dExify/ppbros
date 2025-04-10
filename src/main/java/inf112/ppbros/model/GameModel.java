package inf112.ppbros.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

import inf112.ppbros.model.Entity.EnemyModel;
import inf112.ppbros.model.Entity.PlayerModel;
import inf112.ppbros.model.Entity.RandomEnemyMaker;
import inf112.ppbros.model.Platform.PlatformGrid;
import inf112.ppbros.model.Platform.PlatformGridMaker;
import inf112.ppbros.view.ScreenView;
import inf112.ppbros.view.StartMenuView;

public class GameModel extends Game {
    private PlayerModel player;
    private List<EnemyModel> enemies;
    private RandomEnemyMaker randomEnemyMaker;
    private int cameraPos;
    private Timer timer;
    private CameraYPos timerTask;
    private PlatformGridMaker platformGridMaker;
    private ArrayList<Rectangle> hitboxes;
    private EnemyModel enemy; // PLACEHOLDER
    
    public GameModel() { // change later so it has the background and platform as parameters
        this.setScreen(new StartMenuView(this));
        this.cameraPos = 0;
        this.platformGridMaker = new PlatformGridMaker();
        randomEnemyMaker = new RandomEnemyMaker();
        enemies = new ArrayList<>();
        this.timer = new Timer();
        this.timerTask = new CameraYPos();
        this.hitboxes = new ArrayList<>(); // PLACEHOLDER
        
        // Sets player start position
        makePlayer(0, 150);
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
    public void movePlayer(float deltaX, float deltaY) { // for å bevege karakter når man bruker wasd
        float prevX = player.getX();
        float prevY = player.getY();
        player.move(deltaX * player.getSpeed(), deltaY * player.getSpeed());
        if (platformCollision()) {
            player.setX(prevX);
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
    // public void playerAttacksEnemy() {
    //     enemy.takeDamage(player.getAttackDmg());
    //     // TODO: do a check for when the enemy no longer has hp and update score/kill count
    // }

    // KOMMENTERT UT FOR Å FÅ FUNGERENDE KODE

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
        PlatformGrid platformGrid;
        platformGrid = platformGridMaker.getNextPlatformGrid();
        for (int i = 0; i < 5; i++) {
            updateEnemies(platformGrid);
        }
        hitboxes.addAll(platformGrid.getHitboxes());
        return platformGrid;
    }

    private void updateEnemies(PlatformGrid platformGrid) {
        EnemyModel newEnemy = randomEnemyMaker.getNext(platformGrid);
        enemies.add(newEnemy);
    }

    public List<EnemyModel> getEnemies() {
        return enemies;
    }

    public boolean checkOutOfBounds() {
        return isOutOfBounds();
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
        timer.scheduleAtFixedRate(timerTask, 0, 25);
    }

    public void stopTimer() {
        timerTask.cancel();
    }

    @Override
    public void dispose() {
        timer.cancel();
    }

    public List<Rectangle> getHitboxes() {
        return hitboxes;
    }
    public void jump() {
        player.jump();
    }
    public void updatePlayer() {
        hitboxes.sort(Comparator.comparingDouble(platform -> Math.abs(platform.y - player.getY())));
        player.update(Gdx.graphics.getDeltaTime(), hitboxes);
    }
    
}
