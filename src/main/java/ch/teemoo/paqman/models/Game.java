package ch.teemoo.paqman.models;

import java.util.ArrayList;
import java.util.List;

public class Game {
	private State state;
	private boolean gameStarted = false;
	private boolean pacmanCaught = false;
	private int ghostsCount;
	private int score;
	private final Pacman pacman = new Pacman();
	private final List<Ghost> ghosts = new ArrayList<>();
	private int ghostsTopSpeedIndex;
	private Bonus bonus;

	public static enum State {
		//todo: use this instead of isGameStarted?
		NONE, STARTED, OVER, PAUSE
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public boolean isGameStarted() {
		return gameStarted;
	}

	public void setGameStarted(boolean gameStarted) {
		this.gameStarted = gameStarted;
	}

	public boolean isPacmanCaught() {
		return pacmanCaught;
	}

	public void setPacmanCaught(boolean pacmanCaught) {
		this.pacmanCaught = pacmanCaught;
	}

	public int getGhostsCount() {
		return ghostsCount;
	}

	public void setGhostsCount(int ghostsCount) {
		this.ghostsCount = ghostsCount;
	}

	public int getScore() {
		return score;
	}

	public Pacman getPacman() {
		return pacman;
	}

	public List<Ghost> getGhosts() {
		return ghosts;
	}

	public void addGhost(Ghost ghost) {
		this.ghosts.add(ghost);
	}

	public void resetScore() {
		this.score = 0;
	}

	public void incrementScore(int increment) {
		this.score += increment;
	}

	public void incrementGhostsCount() {
		this.ghostsCount += 1;
	}

	public int getGhostsTopSpeedIndex() {
		return ghostsTopSpeedIndex;
	}

	public void setGhostsTopSpeedIndex(int ghostsTopSpeedIndex) {
		this.ghostsTopSpeedIndex = ghostsTopSpeedIndex;
	}

	public void incrementCurrentTopSpeed() {
		this.ghostsTopSpeedIndex += 1;
	}

	public Bonus getBonus() {
		return bonus;
	}

	public void setBonus(Bonus bonus) {
		this.bonus = bonus;
	}
}
