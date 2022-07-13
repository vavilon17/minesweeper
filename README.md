# Minesweeper

### General notes:
1. I tried to keep the code self-documented and provided only reasonable amount of documentation. Sorry, it you find it not enough!
2. Code is not supported with tests. If doing that for production purpose, for sure we need them
3. I broke some Java naming conventions to be aligned with the requirements (for example, N, K etc)
4. I decided to not separate an implementation of each Part but rather placed all the code needed for the final solution
5. I placed reasonable amount of validations and just throwing exceptions in some basic cases that will be exposed to a client
6. I provided also `GamePlayer` class which allows to imitate a game in console mode. It was not required but rather exists for the test convenience
7. `GamePlayer` doesn't reflect all the aspects of the game, but rather serving to check required functionality

### Part 1
- The main class for the game board is `Board` - it provides a constructor to initialize the NxN field along with K bombs
- We keep also `BoardCell` class - a struture where we keep all necessary information about the cell and it's state
- Locations of the bombs are not kept separately since it seems no need for doing that for this specific task. Instead, we keep a `Map<String, BoardCell> cells` structure where a key is a convenience around cell coordinates ("0-0" stands for (0,0)), and value contains all the information about a cell. Using this structure we need ~0(1) time to lookup the state of any cell. If we decide to keep bombs separately for whatever reason we could just place it as a separate structure within `Board` class (`Set` for instance) 
- Number of the bombs in all adjacent cells is kept under `adjBombs` field of `BoardCell` class
- Flag `isOpen` represents the state of a cell - if it's already open or not

### Part 2
- For the bomb location generation we use a separate class `BombGenerator`
- The method that does the magic is `BombGenerator.randomBombKeys`. It guarantees uniform randomness of selected locations for the bombs and guarantees equal probability of each board cell to be chosen as a bomb location. Please, refer to the method documentation to get more details about the underlying algorithm

### Part 3
- We calculate adjacent bomb numbers for each cell inside `Board.initBoard` method (along with bombs generation). Please, refer to the method documentation for more details

### Part 4
- `Board` class comes with the method `click` which imitates the clink on a cell. It returns a list of `BoardCell` instances that were open. Each instance contains the coordinates along with the number of adjacent bombs needed by a client to represent the results of the click. Please refer to the code documentation for more details