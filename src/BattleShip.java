package src;

import java.util.*;

public class BattleShip {

    final int shipLength = 5;
    final int fieldSize = 50;
    final int numberOfShipsToPlace = 200;

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
        
        boolean shipsPlaced = false;
        for (int run = 0; run < 10000; run++) {
            ships.get(0).setCells(generateCells());
            if(placeShips(numberOfShipsToPlace - 1,0)){
                shipsPlaced = true;
                break;
            }
        }

        if(!shipsPlaced){
            System.out.println("Ship placement failed. Terminating...");
            System.exit(1);
        }


    }

    private boolean placeShips(int shipsToPlace, int depth){
        if(shipsToPlace == 0){
            return true;
        }else if(shipsToPlace == numberOfShipsToPlace){
            return false;
        }
        int currentShip = numberOfShipsToPlace - shipsToPlace;

        if(depth < 50){
            ArrayList<String> randomCells = generateCells();
            if(!isCollision(randomCells)){
                ships.get(currentShip).setCells(randomCells);
                if(placeShips(shipsToPlace-1, 0)){
                    return true;
                }
            }else{
                if(placeShips(shipsToPlace, depth + 1)){
                    return true;
                }
            }
        }else{
            if(placeShips(shipsToPlace + 1, 0) && (shipsToPlace + 1) < numberOfShipsToPlace){
                return true;
            }
            
        }

        return false;
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
