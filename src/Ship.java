package src;

import java.util.ArrayList;

public class Ship {

    String colour;
    ArrayList<String> cells = new ArrayList<>();
    ArrayList<String> cells_hit = new ArrayList<>();


    Ship(String col){
        colour = col;
    }

    public void setShips(ArrayList<String> cells){
        this.cells = cells;
    }

    public String getColour(){
        return colour;
    }

    public ArrayList<String> getCells(){
        return this.cells;
    }

    public ArrayList<String> getCells_hit(){
        return this.cells_hit;
    }

    public String checkYourself(String stringTip){

        String result = "Miss";

        if (this.cells.contains(stringTip)){
            result = "Hit";
            this.cells_hit.add(stringTip);
            this.cells.remove(stringTip);
        }

        if (this.cells.isEmpty()) {
            result = "submerged";
        }
        return result;
    }

}
