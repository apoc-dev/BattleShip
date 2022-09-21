package src;

import javax.swing.*;
import java.util.*;

public class BattleShip {

  final int shipLength = 4;
  final int fieldSize = 5;
  final int shipNumber = 3;

  final boolean DEBUG = true;
  final boolean DEBUG_BACKTRACKING = false;
  final boolean START_GAME = false;


  final ArrayList<Ship> Ships = new ArrayList<>();

  TerminalColours color = new TerminalColours();

  public static void main(String[] args) {

      long start = System.currentTimeMillis();

        BattleShip game = new BattleShip();
        game.prepareGame();

        System.out.println(System.currentTimeMillis() - start+ " milliseconds to prepare");

        if(game.START_GAME)
            game.startGame();

        if(!game.START_GAME)
            game.printGame();

    }

    public void printGame(){

        for (int row = 0; row < fieldSize; row++)
        {
            System.out.println();

            for (int column = 0; column < fieldSize; column++)
            {
                boolean columnPrinted = false;
                System.out.print("|");
                for (Ship ship : Ships){
                    ArrayList<String> cells;

                    if (DEBUG){
                        cells = ship.getCells();
                    }else{
                        cells = ship.getCells_hit();
                    }


                    for (String cell : cells){

                        if ( cell.equals((toAlphabetic(row)) + Integer.toString(column))){

                            System.out.print(ship.getColour()+"X");
                            System.out.print(color.ANSI_RESET);
                            columnPrinted = true;
                        }
                        }

                }
                if (!columnPrinted){
                    System.out.print("O");
                }
            }

            System.out.print("|");
        }
        System.out.println();
        System.out.println();
    }

    public void prepareGame(){

        if (!(fieldSize*fieldSize/shipLength >= shipNumber)){
            System.out.println("Error: Game numbers are invalid");
            System.exit(0);
        }

        String shipColor = color.randomTerminalColor();
        for (int i = 0; i < shipNumber; i++) {
            Ships.add(new Ship(color.randomTerminalColor()));
        }

        ArrayList<String> toTest;
        int leftToPlace = shipNumber;

        for (Ship ship : Ships){
            if (DEBUG)
                System.out.println("generating cells for "+ship);

            while(true){

                toTest = generateCells();

                if (!checkShips(toTest)){

                    if(backtrackShips(toTest, leftToPlace)){
                        ship.setShips(toTest);

                        if (DEBUG){
                            System.out.println("success for "+ship+" "+toTest);
                            System.out.println();
                        }

                        leftToPlace--;
                        break;
                    }
                }
            }
        }
    }

    private boolean backtrackShips(ArrayList<String> toBacktrack, int shipsToPlace){
        boolean backtrackResult = false;
        ArrayList<String> cells;
        ArrayList<ArrayList<String>> givenCells = new ArrayList<>();

        for (int i = 0; i < (shipNumber - shipsToPlace); i++) {
            givenCells.add(Ships.get(i).getCells());
        }
        if(DEBUG_BACKTRACKING){
            System.out.println("backtracking: "+toBacktrack);
            System.out.println("given cells before backtrack: "+ givenCells);
        }
        givenCells.add(toBacktrack);
        shipsToPlace--;

        for (int i = 0; i < shipsToPlace; i++) {

            for (int j = 0; j < 1000; j++) {

                cells = generateCells();

                // check if cells lap over already chosen cells
                for (ArrayList<String> list : givenCells) {

                    if (Collections.disjoint(list, cells)) {
                        backtrackResult = true;

                    } else if (!Collections.disjoint(list, cells)) {
                        backtrackResult = false;
                        break;
                    }
                }

                if(backtrackResult) {
                    givenCells.add(cells);
                    break;
                }
            }
        }

        if (shipsToPlace == 0) { //base-case
            backtrackResult = true;
        }

        if(DEBUG_BACKTRACKING)
            System.out.println("given cells after backtrack: "+ givenCells);
        return backtrackResult;
    }

    public boolean checkShips(ArrayList<String> cells){

        boolean result = false;

        for (Ship ship : Ships){

            if (!Collections.disjoint(cells, ship.getCells())){
                result = true;
            }
        }
        return result;
    }

    public ArrayList<String> generateCells(){
        ArrayList<String> cells = new ArrayList<>();

        int alignment = (int) (Math.random() * 2);

        if (alignment == 0){ // x graph
            int x = (int) (Math.random() * (fieldSize - shipLength +1));
            int y = (int) (Math.random() * fieldSize);
      
            //build ship with specified length in "ship_length"
            for (int i = 0; i < shipLength; i++) {
                cells.add( toAlphabetic(y) + (x+i));
            }

        }
        if (alignment == 1){ // y graph
            int x = (int) (Math.random() * fieldSize);
            int y = (int) (Math.random() * (fieldSize - shipLength +1));
      
            //build ship with specified length in "ship_length"
            for (int i = 0; i < shipLength; i++) {
                cells.add( toAlphabetic(y+i) + (x));
            }

        }
        return cells;
    }

    public void startGame(){
        int totalTips = 0;
        boolean alive = true;

        while (alive){
            printGame();
            Scanner myScanner = new Scanner(System.in);  // Create a Scanner object
            System.out.println("Tip?");

            String tip = myScanner.nextLine().toUpperCase();
            Iterator<Ship> itr = Ships.iterator();

            while (itr.hasNext()){

                Ship ship = itr.next();

                String result = ship.checkYourself(tip);
                System.out.println(result);

                if (result.equals("submerged")){
                    itr.remove();
                }
            }

            totalTips++;
            if (Ships.isEmpty()){
                alive = false;
                System.out.println(totalTips + " tips needed");
            }

        }

    }

    //not my code
    public static String toAlphabetic(int i) {

        // negative numbers
        if( i<0 ) {
            return "-"+toAlphabetic(-i-1);
        }

        // toAlphabetic function
        int quot = i/26;
        int rem = i%26;
        char letter = (char)((int)'A' + rem);

        if( quot == 0 ) {
            return ""+letter;
        } else {
            return toAlphabetic(quot-1) + letter;
        }
    }
}
