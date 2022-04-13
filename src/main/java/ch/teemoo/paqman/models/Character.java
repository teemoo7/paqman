package ch.teemoo.paqman.models;

public abstract class Character {
	protected int posX;
	protected int posY;
	protected Direction movingDirection;
	protected int speed;

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public Direction getMovingDirection() {
		return movingDirection;
	}

	public void setMovingDirection(Direction movingDirection) {
		this.movingDirection = movingDirection;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public void move() {
		switch (movingDirection) {
		case LEFT:
			posX -= speed;
			break;
		case RIGHT:
			posX += speed;
			break;
		case UP:
			posY -= speed;
			break;
		case DOWN:
			posY += speed;
			break;
		default:
		}
	}
}
