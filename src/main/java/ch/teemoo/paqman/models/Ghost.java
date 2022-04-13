package ch.teemoo.paqman.models;

public class Ghost extends Character {

	public Ghost(int posX, int posY, Direction movingDirection, int speed) {
		this.posX = posX;
		this.posY = posY;
		this.speed = speed;
		this.movingDirection = movingDirection;
	}

}
