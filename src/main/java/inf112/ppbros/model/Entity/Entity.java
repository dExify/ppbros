package inf112.ppbros.model.Entity;

import com.badlogic.gdx.math.Rectangle;

public interface Entity {
    
    /**
     * get the type of entity
     * @return the type of entity
     */
    public EntityType getType();

    /**
     * get the health of entity
     * @return health for entity
     */
    public int getHealth();

    /**
     * get the collision box of entity
     * @return rectangle collision box of entity
     */
    public Rectangle getCollisionBox();

    /**
     * get the x value of character
     * @return x value set for character
     */
    public float getX();

    /**
     * get the y value of character
     * @return y value set for character
     */
    public float getY();

    /**
     * get the speed of character
     * @return speed of character in pixel
     */
    public float getSpeed();

    /** 
     * Moves entity by updating x and y pos and updates positions for collisionBox
     * @param dx new x position
     * @param dy new y position
     */
    public void move(float dx, float dy);

    /**
     * get the attack range for character
     * @return attack range in pixels
     */
    public float getAttackRange();

    /**
     * get the attack damage the entity can do
     * @return attack damage of entity 
     */
    public int getAttackDmg();

    /**
     * get the height of the character
     * @return height of character
     */
    public int getHeight();

    /**
     * get the width of the character
     * @return height of character
     */
    public int getWidth();
}
