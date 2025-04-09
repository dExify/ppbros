package inf112.ppbros.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

import inf112.ppbros.controller.AudioController;
import inf112.ppbros.model.Entity.EnemyModel;
import inf112.ppbros.model.Entity.EntityType;
import inf112.ppbros.model.Entity.PlayerModel;
import inf112.ppbros.model.Entity.RandomEnemyMaker;
import inf112.ppbros.model.Platform.PlatformGrid;
import inf112.ppbros.model.Platform.PlatformGridMaker;
import inf112.ppbros.model.Platform.TileConfig;
import inf112.ppbros.view.ScreenView;
import inf112.ppbros.view.StartMenuView;
import inf112.ppbros.view.TilePositionInPixels;

public class GameModel extends Game {
    private PlayerModel player;
    private int score;
    private List<EnemyModel> enemies;
    private RandomEnemyMaker randomEnemyMaker;
    private int cameraPos;
    private Timer timer;
    private CameraYPos timerTask;
    private static long lastExecution = 0;
    private final long cooldownTimeMs = 1000; // 1 second, can be changed
    private PlatformGridMaker platformGridMaker;
    private PlatformGrid platformGrid;
    private PlatformGrid basePlatform;
    private ArrayList<Rectangle> hitboxes;
    private EnemyModel enemy; // PLACEHOLDER
    
    public GameModel() {
        this.setScreen(new StartMenuView(this));
        this.score = 0;
        this.cameraPos = 0;
        this.platformGridMaker = new PlatformGridMaker();
        randomEnemyMaker = new RandomEnemyMaker();
        enemies = new ArrayList<>();
        this.timer = new Timer();
        this.timerTask = new CameraYPos();
        this.platformHitboxes = new ArrayList<Rectangle>();
        this.enemyHitboxes = new ArrayList<Rectangle>();
    }

    @Override
    public void create() {
        this.setScreen(new ScreenView(this));
    }

    /**
     * Returns player
     * @return player
     */
    public PlayerModel getPlayer() {
        return player;
    }

    /**
     * Returns enemies as a list.
     * @return enemies 
     */
    public List<EnemyModel> getEnemies() {
        return enemies;
    }

    /**
     * Returns player score
     * @return score
     */
    public int getScore(){
        return score;
    }

    /**
     * Adds 1 point to score
     */
    public void addToScore(){
        score ++;
    }

    /**
     * Creates an instance of player with start values
     * @param startX start x value
     * @param startY start y value
     */
    public void makePlayer(int startX, int startY) {
        this.player = new PlayerModel(startX, startY);
    }

    /** Moves player based on its speed.
     * Checks for collision with platforms and enemies.
     * Collision with platforms puts the player back to previous position.
     * Collision with enemies makes player take damage
     * @param deltaX horizontal movement
     * @param deltaY vertical movement
    */
    public void movePlayer(float deltaX, float deltaY) {
        float prevX = player.getX();
        float prevY = player.getY();
        player.move(deltaX * player.getSpeed(), deltaY * player.getSpeed());
        if (collisionCheck(platformHitboxes)) {
            player.setX(prevX);
            player.setY(prevY);
        }
        if (collisionCheck(enemyHitboxes)){
            playerIsHit();
        }
    }

    /**
     * Returns enemy player can attack
     * @return enemy player can attack
     */
    public EnemyModel attackableEnemy() {
        for (EnemyModel enemy : enemies){ // if mutiple enemies on same platform, this will not work well
            if (player.canAttack(enemy)) {
                return enemy;
            }
        }
        return null;
    }
   
    /**
     * Player attacks enemy and enemy takes damage.
     * When enemy no longer has more health they die.
     */
    public void playerAttacksEnemy(EnemyModel enemy) {
        enemy.takeDamage(player.getAttackDmg());
        if (enemy.getHealth() == 0) {
            addToScore();
        }
        
    }

    /**
     * Player is hit by enemy and takes damage.
     * Has a cooldown to prevent player from continously taking damage.
     * When player no longe has more health they die/its game over.
     */
    public void playerIsHit() {
        long now = System.currentTimeMillis();
        if (now - lastExecution >= cooldownTimeMs){
            lastExecution = now;
            player.takeDamage(10); // change to getAttackdmg() but once we have added mutiple enemy types? 
            System.out.println("Player is hit, -10 hp!");
            System.out.println("Player health: " + player.getHealth());
        }
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

    public boolean checkOutOfBounds() {
        return isOutOfBounds();
    }

    /**
     * Checks if player collides with an array containing collision boxes as rectangles
     * @return true if player collides with a rectangle, false if they don't
     */
    private boolean collisionCheck(ArrayList<Rectangle> collisionBox) {
        for (Rectangle rec : collisionBox) {
            if (player.collidesWith(rec)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Builds a platform grid and returns the platformGrid object and corresponding enemy on grid
     * @return PlatformGrid with enemies
     */
    public PlatformGrid getNextPlatformGrid() {
        platformGrid = platformGridMaker.getNextPlatformGrid();
        for (int i = 0; i < 5; i++) {
            updateEnemies(platformGrid);
        }
        platformHitboxes.addAll(platformGrid.getHitboxes());
        return platformGrid;
    }

    /**
     * Create new enemy based on platform grid.
     * Converts enemy's position from tiles to pixels.
     * Updates and adds collision box for enemy.
     * Adds enemy to ArrayList of enemies.
     * @param platformGrid grid to check valid position for enemy
     */
    private void updateEnemies(PlatformGrid platformGrid) {
        EnemyModel newEnemy = randomEnemyMaker.getNext(platformGrid);
        // Convert enemy positions from tiles to pixels 
        Coordinate enemyPosInPixels = TilePositionInPixels.getTilePosInPixels((int)newEnemy.getX(), (int)newEnemy.getY(), TileConfig.TILE_SIZE);
        // Update and add collisionbox to a list of all enemy collision boxes 
        newEnemy.updateCollisionBox(enemyPosInPixels.x(), enemyPosInPixels.y());
        enemyHitboxes.add(newEnemy.getCollisionBox());

        enemies.add(newEnemy);
    }

    /**
     * Returns an integer that represents the x coordiante of the viewport
     * @return int
     */
    public int getCameraYCoordinate() {
        cameraPos = timerTask.getCameraPos();
        return cameraPos;
    }

    /**
     * Start timer with fixed rate execution
     */
    public void startTimer() {
        timer.scheduleAtFixedRate(timerTask, 0, 13);
    }

    /**
     * Stop camera movement.
     */
    public void stopTimer() {
        timerTask.cancel();
    }

    /**
     * Terminate timer
     */
    public void dispose() {
        timer.cancel();
    }

    /**
     * Get platform hitboxes
     * @return platform hitboxes as an ArrayList containing Rectangles
     */
    public ArrayList<Rectangle> getPlatformHitboxes() {
        return platformHitboxes;
    }
    public void jump() {
        player.jump();
    }
    public void updatePlayer() {
        player.update(Gdx.graphics.getDeltaTime(), hitboxes);
    }
    
}
