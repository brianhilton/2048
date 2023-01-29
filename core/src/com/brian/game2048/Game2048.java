package com.brian.game2048;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.concurrent.ThreadLocalRandom;

public class Game2048 extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture numberSquare;
	private Array<Array<Integer>> gameBoard;
	private boolean lost;
	private BitmapFont font;

	private void spawnTile() {
		boolean boardFull = true;

		// Did we lose?
		for (Array<Integer> col : gameBoard) {
			for (Integer num : col) {
				if (num == 0) {
					boardFull = false;
				}
			}
		}
		if (boardFull)
			return;

		Integer x = 0;
		Integer y = 0;

		// Find open tile
		do {
			x = ThreadLocalRandom.current().nextInt(0, 3 + 1);
			y = ThreadLocalRandom.current().nextInt(0, 3 + 1);

		} while (gameBoard.get(x).get(y) != 0);

		boolean z = ThreadLocalRandom.current().nextInt(0, 1 + 1) == 1;

		if (z)
			gameBoard.get(x).set(y, 2);
		else
			gameBoard.get(x).set(y, 4);
	}

	// W == 1, A == 2, S == 3, D == 4
	private void translateArray(int direction) {
		switch (direction) {
			case 1:
				for (int i = 0; i < 4; ++i) {
					for (int j = 3; j >= 0; --j) {
						Array<Integer> col = gameBoard.get(i);
						if (gameBoard.get(i).get(j) == 0)
							continue;
						int moveY = 3;
						int current = gameBoard.get(i).get(j);

						for (int y = (j + 1); y < 4; ++y) {
							if (gameBoard.get(i).get(y) != 0) {
								if (gameBoard.get(i).get(j) == gameBoard.get(i).get(y)) {
									gameBoard.get(i).set(y, gameBoard.get(i).get(y) * 2);
									current = 0;
								}

								moveY = y - 1;
								break;
							}

						}
						gameBoard.get(i).set(j, 0);
						gameBoard.get(i).set(moveY, current);
					}
				}
				break;

			case 2:
				for (int i = 0; i < 4; ++i) {
					for (int j = 0; j < 4; ++j) {
						Array<Integer> col = gameBoard.get(i);
						if (gameBoard.get(i).get(j) == 0)
							continue;
						int moveX = 0;
						int current = gameBoard.get(i).get(j);

						for (int x = i - 1; x >= 0; --x) {
							if (x != i && gameBoard.get(x).get(j) != 0) {
								if (gameBoard.get(i).get(j) == gameBoard.get(x).get(j)) {
									gameBoard.get(x).set(j, gameBoard.get(x).get(j) * 2);
									current = 0;
								}
								moveX = x + 1;
								break;
							}

						}

						gameBoard.get(i).set(j, 0);
						gameBoard.get(moveX).set(j, current);
					}
				}
				break;

			case 3:
				for (int i = 0; i < 4; ++i) {
					for (int j = 0; j < 4; ++j) {
						Array<Integer> col = gameBoard.get(i);
						if (gameBoard.get(i).get(j) == 0)
							continue;
						int moveY = 0;
						int current = gameBoard.get(i).get(j);

						for (int y = j; y >= 0; --y) {
							if (y != j && gameBoard.get(i).get(y) != 0) {
								if (gameBoard.get(i).get(j) == gameBoard.get(i).get(y)) {
									gameBoard.get(i).set(y, gameBoard.get(i).get(y) * 2);
									current = 0;
								}

								moveY = y + 1;
								break;
							}

						}
						gameBoard.get(i).set(j, 0);
						gameBoard.get(i).set(moveY, current);
					}
				}
				break;

			case 4:
				for (int i = 3; i >= 0; --i) {
					for (int j = 0; j < 4; ++j) {
						Array<Integer> col = gameBoard.get(i);
						if (gameBoard.get(i).get(j) == 0)
							continue;
						int moveX = 3;
						int current = gameBoard.get(i).get(j);

						for (int x = i + 1; x < 4; ++x) {
							if (x != i && gameBoard.get(x).get(j) != 0) {
								if (gameBoard.get(i).get(j) == gameBoard.get(x).get(j)) {
									gameBoard.get(x).set(j, gameBoard.get(x).get(j) * 2);
									current = 0;
								}

								moveX = x - 1;
								break;
							}

						}

						gameBoard.get(i).set(j, 0);
						gameBoard.get(moveX).set(j, current);
					}
				}
				break;
		}
	}


	@Override
	public void create () {
		batch = new SpriteBatch();
		numberSquare = new Texture(Gdx.files.internal("number_square.png"));
		lost = false;
		font = new BitmapFont();

		gameBoard = new Array<Array<Integer>>();
		for (int i = 0; i < 4; ++i) {
			gameBoard.add(new Array<Integer>());
			for (int j = 0; j < 4; ++j) {
				gameBoard.get(i).add(0);
			}
		}
		spawnTile();
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 1, 1, 1);
		batch.begin();

		for (int i = 0; i < 4; ++i) {

			for (int j = 0; j < 4; ++j) {
				batch.draw(numberSquare, i * 120, j * 120);
				font.draw(batch, gameBoard.get(i).get(j).toString(), ((i + 1) * 120 - 60), ((j + 1) * 120 - 60));
			}
		}

		batch.end();

		if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
			translateArray(1);
			spawnTile();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
			translateArray(3);
			spawnTile();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
			translateArray(2);
			spawnTile();
		}if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
			translateArray(4);
			spawnTile();
		}


	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
