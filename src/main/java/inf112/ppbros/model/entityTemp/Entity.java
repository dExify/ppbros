package inf112.ppbros.model.entityTemp;

import com.badlogic.gdx.math.Rectangle;

public interface Entity {
  
  /**
  * Get the type of entity
  * @return the type of entity
  */
  public EntityType getType();
  
  /**
  * Get the health of entity
  * @return health for entity
  */
  public int getHealth();
  
  /**
  * Entity looses damage based on how much damage taken.
  * Entity dies when health drops to 0.
  * @param damage damage entity takes
  */
  public void takeDamage(int damage);
  
  /**
  * Get the collision box of entity
  * @return rectangle collision box of entity
  */
  public Rectangle getCollisionBox();
  
  /**
  * Get the x value of character
  * @return x value set for character
  */
  public float getX();
  
  /**
  * Get the y value of character
  * @return y value set for character
  */
  public float getY();
  
  /**
  * Get the speed of character
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
  * Get the attack range for character
  * @return attack range in pixels
  */
  public float getAttackRange();
  
  /**
  * Get the attack damage the entity can do
  * @return attack damage of entity 
  */
  public int getAttackDmg();
  
  /**
  * Get the height of the character
  * @return height of character
  */
  public float getHeight();
  
  /**
  * Get the width of the character
  * @return height of character
  */
  public float getWidth();
  
  /**
  * Sets the size of entity and size of collision box.
  * @param width width set for entity
  * @param height height set for entity
  */
  public void setSize(float width, float height);
}
