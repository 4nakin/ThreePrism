/**
 * 
 */
package com.diamondgfx.ogam.threeprism;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

/**
 * @author Brandon Richey
 *
 */
public class GemBoard {
	public static final int BOARD_WIDTH  = 10;
	public static final int BOARD_HEIGHT = 10;
	
	public static final int MATCH_POINTS = 10;
	
	public static final int GAME_HEIGHT = 704;
	
	public static final int NONE  = 0;
	public static final int NORTH = 1;
	public static final int SOUTH = 2;
	public static final int EAST  = 3;
	public static final int WEST  = 4;
	
	private int[][] _board;
	private Gem[]   _gemList;
	private Random  _rng;
	
	private boolean _selected;
	private int     _selectedX;
	private int     _selectedY;
	private Texture _cursor;
	
	private int     _tempScore;
	private int     _score;
	private int     _multiplier;
	
	public GemBoard() {
		_rng        = new Random();
		_gemList    = new Gem[8];
		_board      = new int[BOARD_HEIGHT][BOARD_WIDTH];
		_score      = 0;
		_tempScore  = 0;
		_multiplier = 0;
		_selectedX  = 0;
		_selectedY  = 0;
		_selected   = false;
		_cursor     = new Texture(Gdx.files.internal("data/selected.png"));
		
		_initGems();
		_resetBoard();
		_multiplier = 0;
		_correctBoard(true);
	}
	
	private void _correctBoard(boolean initial)
	{
		boolean doneGen = false;
		while (!doneGen) {
			_multiplier++;
		    beginMatchSearch();
		    doneGen = _complete();
		    _initBoard();
		}
		if (!initial) {
			_score      += (_tempScore * _multiplier);
			//System.out.println(Integer.toString(_score) + " [x" + Integer.toString(_multiplier) + "] [" + Integer.toString(_tempScore) + "]");
		}
		_tempScore   = 0;
		_multiplier  = 0;
	}
	
	private void _resetBoard() {
		for (int y = 0; y < BOARD_HEIGHT; y++) {
			for (int x = 0; x < BOARD_WIDTH; x++) {
				_board[y][x] = 0;
			}
		}
	}
	
	private void _initBoard() {
		for (int y = 0; y < BOARD_HEIGHT; y++) {
		    for (int x = 0; x < BOARD_WIDTH; x++) {
		    	if (_board[y][x] == 0) {
		    		_board[y][x] = _rng.nextInt(6) + 1;
		    	}
		    }
		}
	}
	
	private void _initGems() {
		// Gems are:
		// 0 => blank
		// 1 => red
		// 2 => blue
		// 3 => magenta
		// 4 => green
		// 5 => yellow
		// 6 => cyan
		// 7 => white
		
		_gemList[0] = new Gem(/* blank */);
		_gemList[1] = new Gem("red");
		_gemList[2] = new Gem("blue");
		_gemList[3] = new Gem("magenta");
		_gemList[4] = new Gem("green");
		_gemList[5] = new Gem("yellow");
		_gemList[6] = new Gem("cyan");
		_gemList[7] = new Gem("white");
	}
	
	private void _gravity() {
		for (int y = BOARD_HEIGHT - 1; y > 0; y--) {
			for (int x = 0; x < BOARD_WIDTH; x++) {
				if (_board[y][x] == 0) {
					_board[y][x] = _board[y-1][x];
					_board[y-1][x] = 0;
				}
			}
		}
		_initBoard();
		_correctBoard(false);
		if (!_validMovesRemaining()) {
			System.out.println("Game over! Score: " + Integer.toString(_score));
		}
	}
	
	public void draw(SpriteBatch batch) {
		for (int y = 0; y < BOARD_HEIGHT; y++) {
			for (int x = 0; x < BOARD_WIDTH; x++) {
				if (_board[y][x] == 0) continue;
				_gemList[_board[y][x]].draw(batch, (x * 64), (GAME_HEIGHT - (y + 1) * 64));
			}
		}
		if (_selected) {
			batch.draw(_cursor, (_selectedX * 64), (GAME_HEIGHT - (_selectedY + 1) * 64));
		}
	}
	
	public void beginMatchSearch() {
		_searchMatches();
	}
	
	private void _searchMatches() {
		int matches = 0;
		for (int y = 0; y < BOARD_HEIGHT; y++) {
		    for (int x = 0; x < BOARD_WIDTH; x++) {
		    	if (_board[y][x] == 0) continue;
		    	matches = _countMatches(x, y);
		    	if (matches >= 3) _fill(x, y, _board[y][x]);
	    	}
		}
	}
	
    private int _countMatches(int x, int y) {
		int matches = 1;
			
		if (x - 1 >= 0) {
		    if (_board[y][x-1] == _board[y][x]) matches++;
		}
		if (x + 1 < BOARD_WIDTH) {
		    if (_board[y][x+1] == _board[y][x]) matches++;
		}
		if (y - 1 >= 0) {
		    if (_board[y-1][x] == _board[y][x]) matches++;
		}
		if (y + 1 < BOARD_HEIGHT) {
		    if (_board[y+1][x] == _board[y][x]) matches++;
		}
		return matches;
    }
	
    private void _fill(int x, int y, int searchFor) {
    	_tempScore += 10;
    	_board[y][x] = 0;
    	if (x - 1 >= 0 && _board[y][x-1] == searchFor) {
    		_fill(x-1, y, searchFor);
    	}
    	if (x + 1 < BOARD_WIDTH && _board[y][x+1] == searchFor) {
    		_fill(x+1, y, searchFor);
    	}
    	if (y - 1 >= 0 && _board[y-1][x] == searchFor) {
    		_fill(x, y-1, searchFor);
    	}
    	if (y + 1 < BOARD_HEIGHT && _board[y+1][x] == searchFor) {
    		_fill(x, y+1, searchFor);
    	}	
    }

    private boolean _complete() {
    	for (int y = 0; y < BOARD_HEIGHT; y++) {
    		for (int x = 0; x < BOARD_WIDTH; x++) {
    			if (_board[y][x] == 0) return false;
    		}
    	}
    	return true;
    }
    
    public void checkForInput(int x, int y) {
		if (!_selected) {
			_selected = true;
			_select(x, y);
		} else {
			int x1 = _selectedX;
			int y1 = _selectedY;
			_select(x, y);
			if (x1 == _selectedX && y1 == _selectedY) {
				_selected = false;
				return;
			}
			if (Math.abs(x1 - _selectedX) <= 1 && Math.abs(y1 - _selectedY) <= 1) {
				swap(x1, y1, _selectedX, _selectedY);
				_selected = false;
			} else {
				_selected = true;
			}
		}
    }
    
    private void _select(int x, int y) {
    	//System.out.println("R: " + Integer.toString(x) + ", " + Integer.toString(y));
    	_selectedX = (int) Math.floor((double)x / 64.0);
    	_selectedY = (int) Math.floor((double)y / 64.0);
    	//System.out.println("S: " + Integer.toString(_selectedX) + ", " + Integer.toString(_selectedY));
    }
    
    private int _checkForMatches(int x, int y, int gem)
    {
    	int matches = 1;
    	// Check for immediate matches
    	// Check north
    	if (_northMatch(x, y, gem)) {
    		int ny = y - 1;
    		matches += _allMatch(x, ny, gem, SOUTH) + 1;
    	}
    	// Check south
    	if (_southMatch(x, y, gem)) {
    		int ny = y + 1;
    		matches += _allMatch(x, ny, gem, NORTH) + 1;
    	}
    	// Check west
    	if (_westMatch(x, y, gem)) {
    		int nx = x - 1;
    		matches += _allMatch(nx, y, gem, EAST) + 1;
    	}
    	// Check east
    	if (_eastMatch(x, y, gem)) {
    		int nx = x + 1;
    		matches += _allMatch(nx, y, gem, WEST) + 1;
    	}
    	
    	return matches;
    }
    
    private boolean _northMatch(int x, int y, int gem) {
    	return (y > 0 && _board[y-1][x] == gem);
    }
    
    private boolean _southMatch(int x, int y, int gem) {
    	return (y < BOARD_HEIGHT - 1 && _board[y+1][x] == gem);
    }
    
    private boolean _eastMatch(int x, int y, int gem) {
    	return (x < BOARD_WIDTH - 1 && _board[y][x+1] == gem);
    }
    
    private boolean _westMatch(int x, int y, int gem) {
    	return (x > 0 && _board[y][x-1] == gem);
    }
    
    private int _allMatch(int x, int y, int gem, int exclude) {
    	int matches = 0;
    	if (exclude != NORTH && _northMatch(x, y, gem)) {
    		matches++;
    	}
    	// Check south
    	if (exclude != SOUTH && _southMatch(x, y, gem)) {
    		matches++;
    	}
    	// Check east
    	if (exclude != EAST && _eastMatch(x, y, gem)) {
    		matches++;
    	}
    	// Check west
    	if (exclude != WEST && _westMatch(x, y, gem)) {
    		matches++;
    	}
    	return matches;
    }
    
    public void swap(int x1, int y1, int x2, int y2) {
    	int temp1 = _board[y1][x1];
    	int temp2 = _board[y2][x2];
    	_board[y1][x1] = temp2;
    	_board[y2][x2] = temp1;
    	int matches = Math.max(_checkForMatches(x2, y2, _board[y2][x2]), _checkForMatches(x1, y1, _board[y1][x1]));
    	if (matches < 3){
    		_board[y1][x1] = temp1;
    		_board[y2][x2] = temp2;
    		_score -= 10;
    	} else {
    		beginMatchSearch();
    		_gravity();
    	}
    }
    
    public boolean _fakeSwap(int x1, int y1, int x2, int y2) {
    	int temp1 = _board[y1][x1];
    	int temp2 = _board[y2][x2];
    	_board[y1][x1] = temp2;
    	_board[y2][x2] = temp1;
    	int matches = Math.max(_checkForMatches(x2, y2, _board[y2][x2]), _checkForMatches(x1, y1, _board[y1][x1]));
    	_board[y1][x1] = temp1;
		_board[y2][x2] = temp2;
		return matches >= 3;
    }
    
    private boolean _validMovesRemaining() {
		for (int y = 1; y < BOARD_HEIGHT - 1; y++) {
			for (int x = 1; x < BOARD_WIDTH - 1; x++) {
				if (_fakeSwap(x, y, x, y-1)) return true;
				if (_fakeSwap(x, y, x, y+1)) return true;
				if (_fakeSwap(x, y, x-1, y)) return true;
				if (_fakeSwap(x, y, x+1, y)) return true;
			}
		}
    	
    	return false;
    }
    
    public int getScore() { return _score; }
}
