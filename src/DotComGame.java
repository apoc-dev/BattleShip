package src;

import java.util.*;

public class DotComGame {

  final int dotcom_length = 5;
  final int field_size = 10;
  final int dotcom_numbers = 20;

  final ArrayList<DotCom> Dotcoms = new ArrayList<>();

  TerminalColours colours = new TerminalColours();

  public static void main(String[] args) {
        long start = System.currentTimeMillis();
        DotComGame game = new DotComGame();
        game.prepareGame();
        System.out.println(System.currentTimeMillis() - start+ " milliseconds to prepare");
        game.startGame();

    }

    public void printGame(){

        for (int row = 0; row < field_size; row++)
        {
            System.out.println();

            for (int column = 0; column < field_size; column++)
            {
                boolean column_printed = false;
                System.out.print("|");
                for (DotCom dotcom : Dotcoms){


                    ArrayList<String> cells = dotcom.getCells();

                    for (String cell : cells){

                        if ( cell.equals((toAlphabetic(row)) + Integer.toString(column))){

                            System.out.print(dotcom.getColour()+"X");
                            System.out.print(colours.ANSI_RESET);
                            column_printed = true;
                        }
                        }

                }
                if (!column_printed){
                    System.out.print("O");
                }
            }

            System.out.print("|");
        }
        System.out.println();
        System.out.println();
    }

    public void prepareGame(){

        if (!(field_size*field_size/dotcom_length >= dotcom_numbers)){
            System.out.println("Error: Game numbers are invalid");
            System.exit(0);
        }

        String dotcomColour = colours.randomTerminalColour();
        for (int i = 0; i < dotcom_numbers; i++) {
            Dotcoms.add(new DotCom(colours.randomTerminalColour()));
        }

        ArrayList<String> toTest;
        int leftToPlace = dotcom_numbers;

        for (DotCom dotcom : Dotcoms){
            System.out.println("generating cells for "+dotcom);

            while(true){

                toTest = generateCells();

                if (!checkDotcoms(toTest)){

                    if(backtrackDotcoms(toTest, leftToPlace)){
                        dotcom.setDotCom(toTest);
                        System.out.println("success for "+dotcom+" "+toTest);
                        System.out.println();
                        leftToPlace--;
                        break;
                    }
                }
            }
        }
    }

    private boolean backtrackDotcoms(ArrayList<String> toBacktrack, int dotcomsToPlace){
        boolean backtrack_result = false;
        ArrayList<String> cells;
        ArrayList<ArrayList<String>> givenCells = new ArrayList<>();

        for (int i = 0; i < (dotcom_numbers - dotcomsToPlace); i++) {
            givenCells.add(Dotcoms.get(i).getCells());
        }

        //System.out.println("backtracking: "+toBacktrack);
        //System.out.println("given cells before backtrack: "+ givenCells);
        givenCells.add(toBacktrack);
        dotcomsToPlace--;

        for (int i = 0; i < dotcomsToPlace; i++) {

            for (int j = 0; j < 1000; j++) {

                cells = generateCells();

                // check if cells lap over already chosen cells
                for (ArrayList<String> list : givenCells) {

                    if (Collections.disjoint(list, cells)) {
                        backtrack_result = true;

                    } else if (!Collections.disjoint(list, cells)) {
                        backtrack_result = false;
                        break;
                    }
                }

                if(backtrack_result) {
                    givenCells.add(cells);
                    break;
                }
            }
        }

        if (dotcomsToPlace == 0) { //base-case
            backtrack_result = true;
        }

        //System.out.println("given cells after backtrack: "+ givenCells);
        return backtrack_result;
    }

    public boolean checkDotcoms(ArrayList<String> cells){

        boolean result = false;

        for (DotCom dotcom : Dotcoms){

            if (!Collections.disjoint(cells, dotcom.getCells())){
                result = true;
            }
        }
        return result;
    }

    public ArrayList<String> generateCells(){
        ArrayList<String> cells = new ArrayList<>();

        int alignment = (int) (Math.random() * 2);

        if (alignment == 0){ // x graph
            int x = (int) (Math.random() * (field_size - dotcom_length +1));
            int y = (int) (Math.random() * field_size);
      
            //build dotcom with specified length in "dotcom_length"
            for (int i = 0; i < dotcom_length; i++) {
                cells.add( toAlphabetic(y) + (x+i));
            }

        }
        if (alignment == 1){ // y graph
            int x = (int) (Math.random() * field_size);
            int y = (int) (Math.random() * (field_size - dotcom_length +1));
      
            //build dotcom with specified length in "dotcom_length"
            for (int i = 0; i < dotcom_length; i++) {
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
            Iterator<DotCom> itr = Dotcoms.iterator();

            while (itr.hasNext()){

                DotCom dotcom = itr.next();

                String result = dotcom.checkYourself(tip);
                System.out.println(result);

                if (result.equals("submerged")){
                    itr.remove();
                }
            }

            totalTips++;
            if (Dotcoms.isEmpty()){
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
