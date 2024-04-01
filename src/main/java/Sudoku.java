public class Sudoku {

	// sudoku box size
	int boxSize = 3;
	// row size
	int totalN = boxSize * boxSize;
	int[][] rows = new int[totalN][totalN + 1];
	int[][] columns = new int[totalN][totalN + 1];
	int[][] boxes = new int[totalN][totalN + 1];
	int[][] board;
	boolean sudokuSolved = false;

	public static void main(String args[]) {
		int[][] inputBoard = {
				{ 0, 0, 0, 0, 0, 0, 0, 0, 3 }, 
				{ 5, 7, 0, 0, 0, 9, 0, 2, 1 },
				{ 0, 0, 2, 0, 0, 0, 0, 7, 0 },
				{ 9, 0, 0, 0, 0, 4, 0, 0, 0 },
				{ 6, 0, 0, 7, 0, 0, 3, 5, 0 },
				{ 7, 0, 0, 0, 8, 1, 0, 0, 0 }, 
				{ 0, 6, 0, 0, 1, 0, 2, 0, 0 }, 
				{ 2, 0, 0, 0, 0, 0, 0, 4, 5 },
				{ 0, 0, 1, 4, 0, 5, 0, 0, 0 } };
		
		Sudoku simpleSudokuSolver = new Sudoku();
		System.out.println("Input Board :");
		simpleSudokuSolver.printBoard(inputBoard);
		simpleSudokuSolver.solveSudokuPuzzle(inputBoard);
		System.out.println("\n sudokuSolved: " + simpleSudokuSolver.sudokuSolved);
		System.out.println("Output Board :");
		simpleSudokuSolver.printBoard(simpleSudokuSolver.board);
	}

	 String printBoard(int[][] inputboard) {
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				sb.append(inputboard[i][j] + ",");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	private boolean checkToPopulate(int d, int row, int col) {
		/*
		 * Check if one could place a number d in (row, col) cell
		 */
		int idx = (row / boxSize) * boxSize + col / boxSize;
		return rows[row][d] + columns[col][d] + boxes[idx][d] == 0;
	}

	private boolean checkToPopulateOnlyOneCell(int row, int col) {
		/*
		 * Check if one could only place one number in (row, col) cell
		 */
		int position = 0, count = 0;
		for (int indexY = 1; indexY < 10; indexY++) {
			int indeX = (row / boxSize) * boxSize + col / boxSize;
			if (rows[row][indexY] + columns[col][indexY] + boxes[indeX][indexY] == 0) {
				count += 1;
				position = indexY;
			}
		}
		if (count == 1) {
			populateTheNumber(position, row, col);
		}
		return false;
	}

	private void populateTheNumber(int number, int row, int col) {
		/*
		 * Place a number in (row, col) cell
		 */
		int idx = (row / boxSize) * boxSize + col / boxSize;

		rows[row][number]++;
		columns[col][number]++;
		boxes[idx][number]++;
		board[row][col] = number;
	}

	private void removeTheNumber(int d, int row, int col) {
		/*
		 * Remove a number which didn't lead to a solution
		 */
		int idx = (row / boxSize) * boxSize + col / boxSize;
		rows[row][d]--;
		columns[col][d]--;
		boxes[idx][d]--;
		board[row][col] = 0;
	}

	private void populateNextSetOfNumbers(int row, int col) {
		/*
		 * Call backtrack function in recursion to continue to place numbers till the
		 * moment we have a solution
		 */
		// if we're in the last cell
		// that means we have the solution
		if ((col == totalN - 1) && (row == totalN - 1)) {
			sudokuSolved = true;
		}
		// if not yet
		else {
			// if we're in the end of the row
			// go to the next row
			if (col == totalN - 1)
				applyBacktracking(row + 1, 0);
			// go to the next column
			else
				applyBacktracking(row, col + 1);
		}
	}

	private void applyBacktracking(int row, int col) {
		/*
		 * Backtracking
		 */
		// if the cell is empty
		if (board[row][col] == 0) {
			// iterate over all numbers from 1 to 9
			for (int d = 1; d < 10; d++) {
				if (checkToPopulate(d, row, col)) {
					populateTheNumber(d, row, col);
					populateNextSetOfNumbers(row, col);
					// if sudoku is solved, there is no need to backtrack
					// since the single unique solution is promised
					if (!sudokuSolved)
						removeTheNumber(d, row, col);
				}
			}
		} else
			populateNextSetOfNumbers(row, col);
	}

	public void solveSudokuPuzzle(int[][] board) {
		this.board = board;

		// init rows, columns and boxes
		for (int i = 0; i < totalN; i++) {
			for (int j = 0; j < totalN; j++) {
				int num = board[i][j];
				if (num != 0) {
					populateTheNumber(num, i, j);
				}
			}
		}
		while (true) {
			int flag = 0;
			for (int i = 0; i < totalN; i++) {
				for (int j = 0; j < totalN; j++) {
					if (board[i][j] == 0) {
						if (checkToPopulateOnlyOneCell(i, j) == true) {
							flag = 1;
						}
					}
				}
			}
			if (flag == 0) {
				break;
			}

		}
		applyBacktracking(0, 0);
	}
}