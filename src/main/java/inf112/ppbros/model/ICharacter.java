package inf112.ppbros.model;

public interface ICharacter {
    
    /**
     * get the x value of character
     * @return x value set for character
     */
    public int getX();

    /**
     * get the y value of character
     * @return y value set for character
     */
    public int getY();

    /**
     * set the x value of character
     * @return new x value of character
     */ 
    public int setX();

    /**
     * set the y value of character
     * @return new y value of character
     * @return
     */
    public int setY();

    /**
     * get the speed of character
     * @return speed of character
     */
    public int getSpeed();

    /**
     * get the attack range for character
     * @return attack range in pixels
     */
    public int getAttackRange();

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
