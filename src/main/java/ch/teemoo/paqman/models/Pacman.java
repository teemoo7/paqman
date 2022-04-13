package ch.teemoo.paqman.models;

public class Pacman extends Character {
	private int lives;
	private Direction lookingDirection;

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public Direction getLookingDirection() {
		return lookingDirection;
	}

	public void setLookingDirection(Direction lookingDirection) {
		this.lookingDirection = lookingDirection;
	}

	public void decrementLives() {
		this.lives -= 1;
	}
}
