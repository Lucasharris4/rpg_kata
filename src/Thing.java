public class Thing {
    public int health = 1000;

    public int takeDamage(int amount) {
        health -= amount;
        return health;
    }

    public boolean isDestroyed() {
        return health <= 0;
    }
}
