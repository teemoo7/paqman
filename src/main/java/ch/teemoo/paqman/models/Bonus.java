package ch.teemoo.paqman.models;

public class Bonus {
	private final int posX;
	private final int posY;
	private final int value;
	private int timeToLive;

	public Bonus(int posX, int posY, int value, int timeToLive) {
		this.posX = posX;
		this.posY = posY;
		this.value = value;
		this.timeToLive = timeToLive;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public int getValue() {
		return value;
	}

	public int getTimeToLive() {
		return timeToLive;
	}

	public void decrementTimeToLive() {
		this.timeToLive -= 1;
	}
}
