package edu.txst.midterm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

/**
 * Main GUI window for the 16-Bit Maze game.
 * Displays the game board, handles keyboard input for player movement,
 * and manages loading and resetting levels via a menu bar.
 */
public class MazeGUI extends JFrame {
	private Board originalBoard;
	private Board currentBoard;
	private GameEngine engine;
	private GamePanel gamePanel;
	private InfoPanel infoPanel;
	private JMenuItem resetItem;
	private int stepCounter;

	/**
	 * Constructs the main game window, initializes the menu, info panel,
	 * game panel, and keyboard listener.
	 */
	public MazeGUI() {
		setTitle("16-Bit Maze");
		setSize(640, 480); // Adjusted for 10x5 grid with scaling
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		initMenu();

		infoPanel = new InfoPanel();
		gamePanel = new GamePanel();
		add(infoPanel, BorderLayout.NORTH);
		add(gamePanel, BorderLayout.CENTER);

		// Handle Keyboard Input
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (engine == null)
					return;

				switch (e.getKeyCode()) {
					case KeyEvent.VK_UP -> engine.movePlayer(-1, 0);
					case KeyEvent.VK_DOWN -> engine.movePlayer(1, 0);
					case KeyEvent.VK_LEFT -> engine.movePlayer(0, -1);
					case KeyEvent.VK_RIGHT -> engine.movePlayer(0, 1);
				}
				gamePanel.repaint();
			infoPanel.setRemainingSteps(currentBoard.stepCounter.getRemainingSteps());

				// Check for game over
				if (engine.isGameOver()) {
					JOptionPane.showMessageDialog(MazeGUI.this,
							"Game over! You used all available steps!", "Level Complete",
							JOptionPane.INFORMATION_MESSAGE);

					// Optional: Disable engine to prevent movement after win
					engine = null;
					resetItem.setEnabled(false);
				}
				// Check for victory
				if (engine != null && engine.playerWins()) {
					JOptionPane.showMessageDialog(MazeGUI.this,
							"Congratulations! You found the exit.", "Level Complete",
							JOptionPane.INFORMATION_MESSAGE);

					// Optional: Disable engine to prevent movement after win
					engine = null;
					resetItem.setEnabled(false);
				}
			}
		});
	}

	/**
	 * Initializes the menu bar with Open and Reset menu items.
	 * Open prompts the user to load a CSV level file.
	 * Reset restarts the current level from its original state.
	 */
	private void initMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");

		JMenuItem openItem = new JMenuItem("Open");
		resetItem = new JMenuItem("Reset");
		resetItem.setEnabled(false); // Disabled by default

		openItem.addActionListener(e -> openFile());
		resetItem.addActionListener(e -> resetGame());

		gameMenu.add(openItem);
		gameMenu.add(resetItem);
		menuBar.add(gameMenu);
		setJMenuBar(menuBar);
	}

	/**
	 * Opens a file chooser dialog for the user to select a CSV level file.
	 * Loads the board, initializes the game engine, and updates the display.
	 */
	private void openFile() {
		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
		int result = fileChooser.showOpenDialog(this);

		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			CSVBoardLoader loader = new CSVBoardLoader();

			// Load and Store
			originalBoard = loader.load(selectedFile.getAbsolutePath());
			currentBoard = originalBoard.clone();
			engine = new GameEngine(currentBoard);

			resetItem.setEnabled(true);
			gamePanel.setBoard(currentBoard);
			gamePanel.repaint();
			infoPanel.setRemainingSteps(currentBoard.stepCounter.getRemainingSteps());
		}
	}

	/**
	 * Resets the current level by cloning the original board and creating
	 * a new game engine. Refreshes the display to the initial state.
	 */
	private void resetGame() {
		if (originalBoard != null) {
			currentBoard = originalBoard.clone();
			engine = new GameEngine(currentBoard);
			gamePanel.setBoard(currentBoard);
			gamePanel.repaint();
		}
	}

	/**
	 * Inner panel that displays game statistics such as remaining steps and coins.
	 */
	private class InfoPanel extends JPanel {
		private JLabel infoRemainingSteps;
		private JLabel infoCoins;

		/**
		 * Constructs the InfoPanel with labels for remaining steps and coins.
		 */
		public InfoPanel() {
			this.setLayout(new FlowLayout());
			this.add(new JLabel("Remaining steps: "));
			infoRemainingSteps = new JLabel("0");
			this.add(infoRemainingSteps);
			this.add(new JLabel("Coins: "));
			infoCoins = new JLabel("0");
			this.add(infoCoins);
		}

		/**
		 * Updates the remaining steps label with the given value.
		 *
		 * @param remainingSteps the number of steps remaining
		 */
		public void setRemainingSteps(int remainingSteps) {
			this.infoRemainingSteps.setText(Integer.toString(remainingSteps));
		}

		/**
		 * Updates the coins label with the given value.
		 *
		 * @param infoCoins the number of coins collected
		 */
		public void setInfoCoins(int infoCoins) {
			this.infoCoins.setText(Integer.toString(infoCoins));
		}
	}

	/**
	 * Inner panel responsible for rendering the maze grid.
	 * Each cell is drawn as a colored tile based on its type.
	 */
	private class GamePanel extends JPanel {
		private Board board;
		private final int TILE_SIZE = 64; // Scale up for visibility

		/**
		 * Sets the board to be rendered by this panel.
		 *
		 * @param board the board to display
		 */
		public void setBoard(Board board) {
			this.board = board;
		}

		/**
		 * Paints the game board by iterating over all cells and drawing each tile.
		 *
		 * @param g the Graphics context used for drawing
		 */
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (board == null)
				return;

			for (int r = 0; r < 6; r++) {
				for (int c = 0; c < 10; c++) {
					int cell = board.getCell(r, c);
					drawTile(g, cell, c * TILE_SIZE, r * TILE_SIZE);
				}
			}
		}

		/**
		 * Draws a single tile at the specified pixel coordinates using a color
		 * corresponding to the cell type.
		 *
		 * @param g    the Graphics context used for drawing
		 * @param type the cell type constant
		 * @param x    the x pixel coordinate
		 * @param y    the y pixel coordinate
		 */
		private void drawTile(Graphics g, int type, int x, int y) {
			// Placeholder colors until you link the sprite loading logic
			switch (type) {
				case 0 -> g.setColor(Color.LIGHT_GRAY); // Floor
				case 1 -> g.setColor(Color.DARK_GRAY); // Wall
				case 2 -> g.setColor(Color.YELLOW); // Coin
				case 3 -> g.setColor(Color.CYAN); // Map
				case 4 -> g.setColor(Color.RED); // First Aid Kit
				case 5 -> g.setColor(Color.MAGENTA); // Exit
				case 6 -> g.setColor(Color.BLUE); // Player
				default -> g.setColor(Color.BLACK);
			}
			g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
			g.setColor(Color.WHITE);
			g.drawRect(x, y, TILE_SIZE, TILE_SIZE); // Grid lines
		}
	}

	/**
	 * Entry point for the application. Launches the MazeGUI on the Swing event thread.
	 *
	 * @param args command-line arguments (not used)
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new MazeGUI().setVisible(true));
	}
}
