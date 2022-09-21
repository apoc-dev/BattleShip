package src;

import java.util.ArrayList;

public class Ship {

    String color;
    ArrayList<String> cells = new ArrayList<>();
    ArrayList<String> cells_hit = new ArrayList<>();

    Ship(String col) {
        color = col;
    }

    public void setCells(ArrayList<String> cells) {
        this.cells = cells;
    }

    public String getColor() {
        return color;
    }

    public ArrayList<String> getCells() {
        return this.cells;
    }

    public ArrayList<String> getCells_hit() {
        return this.cells_hit;
    }

    public String checkYourself(String stringTip) {

        String result = "Miss";

        if (this.cells.contains(stringTip)) {
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
