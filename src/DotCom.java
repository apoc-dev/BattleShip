package src;

import java.util.ArrayList;

public class DotCom {

    ArrayList<String> cells = new ArrayList<>();

    public void setDotCom(ArrayList<String> cells){
        this.cells = cells;
    }

    public ArrayList<String> getCells(){
        return this.cells;
    }

    public String checkYourself(String stringTip){

        String result = "Miss";

        if (this.cells.contains(stringTip)){
            result = "Hit";

            this.cells.remove(stringTip);
        }

        if (this.cells.isEmpty()) {
            result = "submerged";
        }
        return result;
    }

}
