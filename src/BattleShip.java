package src;

import java.util.*;

public class BattleShip {

    final int shipLength = 6;
    final int fieldSize = 10;
    final int numberOfShipsToPlace = 16;

    final boolean DEBUG = true;
    final boolean DEBUG_BACKTRACKING = false;
    final boolean START_GAME = false;

    final ArrayList<Ship> ships = new ArrayList<>();

    TerminalColours color = new TerminalColours();

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        BattleShip game = new BattleShip();
        game.prepareGame();

        System.out.println(System.currentTimeMillis() - start + " milliseconds to prepare");

        if (game.START_GAME)
            game.startGame();

        if (!game.START_GAME)
            game.printGame();

    }

    public void printGame() {

        for (int row = 0; row < fieldSize; row++) {
            System.out.println();

            for (int column = 0; column < fieldSize; column++) {
                boolean columnPrinted = false;
                System.out.print("|");
                for (Ship ship : ships) {
                    ArrayList<String> cells;

                    if (DEBUG) {
                        cells = ship.getCells();
                    } else {
                        cells = ship.getCells_hit();
                    }

                    for (String cell : cells) {

                        if (cell.equals((toAlphabetic(row)) + Integer.toString(column))) {

                            System.out.print(ship.getColor() + "X");
                            System.out.print(color.ANSI_RESET);
                            columnPrinted = true;
                        }
                    }

                }
                if (!columnPrinted) {
                    System.out.print("O");
                }
            }

            System.out.print("|");
        }
        System.out.println();
        System.out.println();
    }

    public void prepareGame() {

        if (!(fieldSize * fieldSize / shipLength >= numberOfShipsToPlace)) {
            System.out.println("Error: Game numbers are invalid");
            System.exit(0);
        }

        for (int i = 0; i < numberOfShipsToPlace; i++) {
            ships.add(new Ship(color.randomTerminalColor()));
        }
        
        if(!placeShips(numberOfShipsToPlace)){
            System.out.println("Ship placement failed. Terminating.");
            System.exit(1);
        }


    }

    private boolean placeShips(int shipsToPlace){
        
        int maxDepth = 100;
        int maxBacktrackTries = 10;
        int currentBacktrackIndex = 0;
        int currentDepth = 0;
        int currentShipIndex = 0;
        int backtrackIndex = 1;
        ArrayList<String> randomCells;

        while(currentShipIndex < shipsToPlace){

            randomCells = generateCells();

            if(!isCollision(randomCells)){
                ships.get(currentShipIndex).setCells(randomCells);
                currentShipIndex++;
                currentDepth = 0;
            }else{
                currentDepth++;
            }

            if(currentDepth >= maxDepth){
                if(currentBacktrackIndex > maxBacktrackTries){
                    backtrackIndex++;
                    currentBacktrackIndex = 0;
                    currentShipIndex = currentShipIndex - backtrackIndex;
                }else{
                    currentShipIndex--;
                    currentBacktrackIndex++;
                }
                if(currentShipIndex < 0){
                    currentShipIndex = 0;
                }
                resetShips(currentShipIndex);
                currentDepth = 0;
            }

        }
        
        return true;

    }

    private void resetShips(int newEnd){
        for(int shipIndex = newEnd; shipIndex < numberOfShipsToPlace; shipIndex++){
            ships.get(shipIndex).setCells(new ArrayList<String>());
        }
    }



    public boolean isCollision(ArrayList<String> cells) {

        for (Ship ship : ships) {

            if (!Collections.disjoint(cells, ship.getCells())) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> generateCells() {
        ArrayList<String> cells = new ArrayList<>();

        int alignment = (int) (Math.random() * 2);

        if (alignment == 0) { // x graph
            int x = (int) (Math.random() * (fieldSize - shipLength + 1));
            int y = (int) (Math.random() * fieldSize);

            // build ship with specified length in "ship_length"
            for (int i = 0; i < shipLength; i++) {
                cells.add(toAlphabetic(y) + (x + i));
            }

        }
        if (alignment == 1) { // y graph
            int x = (int) (Math.random() * fieldSize);
            int y = (int) (Math.random() * (fieldSize - shipLength + 1));

            // build ship with specified length in "ship_length"
            for (int i = 0; i < shipLength; i++) {
                cells.add(toAlphabetic(y + i) + (x));
            }

        }
        return cells;
    }

    public void startGame() {
        int totalTips = 0;
        boolean alive = true;

        while (alive) {
            printGame();
            Scanner myScanner = new Scanner(System.in); // Create a Scanner object
            System.out.println("Tip?");

            String tip = myScanner.nextLine().toUpperCase();
            Iterator<Ship> itr = ships.iterator();

            while (itr.hasNext()) {

                Ship ship = itr.next();

                String result = ship.checkYourself(tip);
                System.out.println(result);

                if (result.equals("submerged")) {
                    itr.remove();
                }
            }

            totalTips++;
            if (ships.isEmpty()) {
                alive = false;
                System.out.println(totalTips + " tips needed");
            }

        }

    }

    // not my code
    public static String toAlphabetic(int i) {

        // negative numbers
        if (i < 0) {
            return "-" + toAlphabetic(-i - 1);
        }

        // toAlphabetic function
        int quot = i / 26;
        int rem = i % 26;
        char letter = (char) ((int) 'A' + rem);

        if (quot == 0) {
            return "" + letter;
        } else {
            return toAlphabetic(quot - 1) + letter;
        }
    }
}
