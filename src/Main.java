import processing.core.PApplet;

public class Main {

    public static void main(String[] args){
        String[] processingArgs = {"HexSketch"};
        HexSketch mySketch = new HexSketch();
        PApplet.runSketch(processingArgs, mySketch);
       // PApplet.main(new String[] {"Sketch"});
    }
}
