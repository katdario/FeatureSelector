import java.io.*;
import java.util.*;
import java.util.Random;

public class Node {
    private Vector<String> features = new Vector<String>();
    private double accuracy;
    private int rangeMin = 0;
    private int rangeMax = 100;

    Random rand = new Random();

    public Node(){
        accuracy = rangeMin + (rangeMax - rangeMin) * rand.nextDouble();
    }

    public Node(Vector<String> newFeatures){
        //copy newFeatures to this features
        for(int i=0; i< newFeatures.size(); ++i){
            features.add(newFeatures.get(i));
        }

        accuracy = rangeMin + (rangeMax - rangeMin) * rand.nextDouble();
    }

    public Vector<String> getFeatures(){
        Vector<String> newFeat = new Vector<>();
        copyFeatures(newFeat);
        return newFeat;
    }

    public double getAccuracy(){
        return accuracy;
    }

    public void addFeature(String newFeature){
        features.add(newFeature);
    }

    public void copyFeatures(Vector<String> dest){
        if(dest.isEmpty()){
            for(int i=0; i<features.size(); ++i){
                dest.add(features.get(i));
            }
        }
    }

    public void printFeatures(){ //prints in following format: "{feat1, feat2, ... , featn}"
        System.out.print("{");
        for(int i=0; i<features.size(); ++i){
            if(i==0)
                System.out.print(features.get(i));
            else
                System.out.print(", " + features.get(i));
        }
        System.out.print("}");
    }

    public void updateAccuracy(double newAccuracy){
        accuracy = newAccuracy;
    }
}
