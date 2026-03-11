# 16-Bit Maze — Sokoban-Style Java Game

A grid-based maze game built in Java (Swing) for TXST CS Midterm.
Navigate a **6×10** board from start to exit before your steps run out.

---

## Gameplay Preview

```
┌─────────────────────────────────────────────────────┐
│  16-Bit Maze                          [Game ▾]      │
│─────────────────────────────────────────────────────│
│  Remaining steps: 25          Coins: 0              │
│─────────────────────────────────────────────────────│
│                                                     │
│   P  ██ ██ ██ ██ ██ ██ ██ ██ ██                    │
│      ██          +          ██                      │
│      ██    ██ ██ ██ ██ ██                           │
│                                                     │
│   ██ ██ ██ ██       ██ ██ ██ ██                    │
│   ██ ██ ██ ██       ██ ██ ██ ██  X                 │
│                                                     │
└─────────────────────────────────────────────────────┘
```

---

## Cell Legend

| Symbol | Color      | Meaning                                       |
|--------|------------|-----------------------------------------------|
| `P`    | Blue       | **Player** — you                              |
| `██`   | Dark Gray  | **Wall** — impassable                         |
| `  `   | Light Gray | **Floor** — walkable                          |
| `$`    | Yellow     | **Coin** — collectible                        |
| `M`    | Cyan       | **Map** — reveals all hidden cells instantly  |
| `+`    | Red        | **First Aid Kit** — restores +10 steps        |
| `X`    | Magenta    | **Exit** — reach this to win                  |
| `?`    | —          | **Hidden** — cell type unknown until revealed |

---

## Levels

### Level 1 — Fog of War  `(30 steps)`

All cells are hidden. Only the player spawn is visible.
Find a map or explore carefully to uncover the exit.

**As seen by the player:**
```
?  ?  ?  ?  ?  ?  ?  ?  ?  ?
?  ?  ?  ?  ?  ?  ?  ?  ?  ?
P  ?  ?  ?  ?  ?  ?  ?  ?  ?
?  ?  ?  ?  ?  ?  ?  ?  ?  ?
?  ?  ?  ?  ?  ?  ?  ?  ?  ?
?  ?  ?  ?  ?  ?  ?  ?  ?  ?
```

**Actual map (spoiler):**
```
.  .  .  .  █  .  .  █  .  .
.  █  .  .  █  .  .  █  .  .
P  █  .  .  █  .  .  █  .  .
.  █  .  .  .  .  .  .  .  .
.  .  .  █  █  █  .  .  █  .
.  .  .  .  .  █  .  X  █  .
```
> The exit `X` is at row 5, col 7 — completely hidden until revealed.

---

### Level 2 — Open Maze  `(25 steps)`

Fully visible board. A First Aid Kit grants +10 extra steps.
Navigate through winding corridors to reach the exit at bottom-right.

```
P  █  █  █  █  █  █  █  █  █
.  █  .  .  .  .  .  +  .  █
.  █  .  █  █  █  █  █  .  █
.  .  .  .  .  .  .  .  .  .
█  █  █  █  .  █  █  █  █  .
█  █  █  █  .  █  █  █  █  X
```

> Tip: Grab the First Aid Kit `+` early — it adds 10 steps and you only have 25.

---

### Level 3 — Deep Fog  `(20 steps)`

Nearly the entire board is hidden. Only 20 steps available.
You must find the hidden Map to reveal paths, then locate the First Aid Kit and exit.

**As seen by the player:**
```
P  ?  ?  ?  ?  ?  ?  ?  ?  ?
?  ?  ?  ?  ?  ?  ?  ?  ?  ?
?  ?  ?  ?  ?  ?  ?  ?  ?  ?
?  ?  ?  ?  ?  ?  ?  ?  ?  ?
?  ?  ?  ?  ?  ?  ?  ?  ?  ?
?  ?  ?  ?  ?  ?  ?  ?  ?  ?
```

**Actual map (spoiler):**
```
P  .  .  .  █  █  █  █  █  █
█  █  █  .  █  █  █  █  █  █
█  █  █  .  .  M  .  █  █  █
█  █  █  █  █  █  .  █  █  █
█  █  █  █  █  █  .  .  +  .
█  █  █  █  █  █  █  █  █  X
```

> The Map `M` at (row 2, col 5) reveals everything. The First Aid Kit `+` at (row 4, col 8) is essential — without it you cannot reach the exit in 20 steps.

---

## Game Mechanics

```
┌──────────────────────────────────────────────────────────────┐
│                        MOVEMENT                              │
│                                                              │
│                         ↑ Up                                 │
│                         │                                    │
│          ← Left  ──── [P] ──── Right →                      │
│                         │                                    │
│                        ↓ Down                                │
│                                                              │
│  Move into a wall      →  blocked (no step consumed)         │
│  Move into a coin      →  coin collected                     │
│  Move into a map       →  all hidden cells revealed          │
│  Move into first aid   →  +10 steps restored                 │
│  Move into exit        →  YOU WIN!                           │
│  Steps reach 0         →  GAME OVER                          │
└──────────────────────────────────────────────────────────────┘
```

### Hidden Cell Rules

| Hidden Cell     | What happens when you step on it            |
|-----------------|---------------------------------------------|
| Hidden Wall     | Revealed as a wall, movement blocked        |
| Hidden Floor    | Revealed as floor, player moves in          |
| Hidden Map      | Reveals ALL hidden cells on the board       |
| Hidden First Aid| Revealed, then +10 steps applied            |
| Hidden Exit     | Revealed, player moves in → WIN             |

---

## How to Run

**Requirements:** Java 11+

```bash
# From the repo root — compile all sources
javac edu/txst/midterm/*.java

# Run the game
java edu.txst.midterm.MazeGUI
```

**Loading a level:**
1. Click **Game → Open**
2. Select a `.csv` file (e.g. `level1.csv`, `level2.csv`, `level3.csv`)
3. Use **arrow keys** to move the player
4. Click **Game → Reset** to restart the current level

---

## Project Structure

```
midtermA/
├── edu/txst/midterm/
│   ├── MazeGUI.java          # Swing window, rendering, keyboard input
│   ├── GameEngine.java       # Movement logic, win/loss detection
│   ├── Board.java            # 6×10 grid data model
│   ├── BoardLoader.java      # Interface for level loading
│   ├── CSVBoardLoader.java   # Loads .csv files into Board
│   ├── StepCounter.java      # Tracks steps used / remaining
│   ├── level1.csv            # Level 1 — full fog of war  (30 steps)
│   ├── level2.csv            # Level 2 — open visible maze (25 steps)
│   └── level3.csv            # Level 3 — deep fog, tight steps (20 steps)
└── docs/                     # Javadoc HTML documentation
```

### CSV Format

```
<max_steps>
<row0: 10 comma-separated cell-type integers>
<row1>
...
<row5>
```

Example (`level2.csv`):
```
25
6,1,1,1,1,1,1,1,1,1
0,1,0,0,0,0,0,4,0,1
0,1,0,1,1,1,1,1,0,1
0,0,0,0,0,0,0,0,0,0
1,1,1,1,0,1,1,1,1,0
1,1,1,1,0,1,1,1,1,5
```

---

## Architecture

```
MazeGUI  (JFrame)
  ├── InfoPanel (JPanel)    — shows remaining steps & coins
  ├── GamePanel (JPanel)    — renders the 6×10 tile grid (64px per tile)
  └── KeyAdapter            — arrow keys → GameEngine.movePlayer()

GameEngine
  ├── findPlayer()          — scans board for cell type 6
  ├── findExit()            — scans board for cell type 5 or 15
  ├── movePlayer(dRow,dCol) — validates move, handles pickups, steps
  ├── removeHidden()        — subtracts 10 from all hidden cell values
  ├── playerWins()          — checks player position == exit position
  └── isGameOver()          — checks remainingSteps == 0

Board (6×10 Integer[][])
  └── StepCounter           — maxSteps, currentSteps, remaining
```

---

## Cell Type Reference

| Value | Visible | Type           |
|-------|---------|----------------|
| 0     | Yes     | Floor          |
| 1     | Yes     | Wall           |
| 2     | Yes     | Coin           |
| 3     | Yes     | Map            |
| 4     | Yes     | First Aid Kit  |
| 5     | Yes     | Exit           |
| 6     | Yes     | Player         |
| 10    | No      | Hidden Floor   |
| 11    | No      | Hidden Wall    |
| 12    | No      | Hidden Coin    |
| 13    | No      | Hidden Map     |
| 14    | No      | Hidden FAK     |
| 15    | No      | Hidden Exit    |

> Hidden cells (10–15) are converted to their visible counterpart (0–5) by subtracting 10 when the Map item is collected.

---

*Built with Java Swing — Texas State University CS Midterm Project*
