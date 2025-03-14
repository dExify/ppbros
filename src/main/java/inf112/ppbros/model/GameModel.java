package inf112.ppbros.model;

public class GameModel {
    private PlayerModel player;
    private EnemyModel enemy;

    public GameModel() { // change later so it has the background and platform as parameters
        // TODO: add necessary variables

    }

    /** Moves player to the left based on its speed */
    public void movePlayerLeft(float delta) { // for å bevege karakter når man bruker wasd
        player.move(-delta * player.getSpeed(), 0);
        // TODO: use method in playerModel to check if player collides with obstacles in the game
        // remember to update previous pos for character

        // TODO: use method that checks if player went out of map
    }

    /** Moves player to the right based on its speed */
    public void movePlayerRight(float delta) {
        player.move(delta * player.getSpeed(), 0);
        // TODO: use method in playerModel to check if player collides with obstacles in the game
        // remember to update previous pos for character
    }

    /** Moves player to the up based on its speed */
    public void movePlayerUp(float delta) {
        player.move(0, delta * player.getSpeed());
        // TODO: use method in playerModel to check if player collides with obstacles in the game
        // remember to update previous pos for character
    }

    /** Moves player to the down based on its speed */
    public void movePlayerDown(float delta) {
        player.move(0, -delta * player.getSpeed());
        // TODO: use method in playerModel to check if player collides with obstacles in the game
        // remember to update previous pos for character

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
     * Checks if player is out of bounds, if so they loose health 
     * @return true if player is out of bounds, false if they are within bounds
     */
    public boolean isOutOfBounds() {
        // TODO: check if player is out of map and loose health/a life if they are
        return false;
    }
    
    
}
