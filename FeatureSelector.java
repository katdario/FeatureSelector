import java.io.*;
import java.util.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class FeatureSelector {
    Vector<Vector<String>> data = new Vector<Vector<String>>();

    public static void main(String[] args) {

        String fileName = "small-test-dataset.txt";

        Vector<String> features = new Vector<>();
        for(int i=1; i<=4; i++){
            features.add(String.valueOf(i));
        }
        Node best = greedySearch(features);
    }

    //=============================================
    // Greedy Search
    //=============================================

    public static Node greedySearch(Vector<String> features){
        Node state = new Node();
        Node bestFeatureSet = new Node();
        DecimalFormat decFor = new DecimalFormat("0.0");
        decFor.setRoundingMode(RoundingMode.UP); //used to round up the accuracy

        System.out.println("Using no features and \"random\" evaluation, I get an accuracy of " +
                decFor.format(state.getAccuracy()) + "%");
        System.out.println("\nBeginning search.\n");

        for(int i=0; i< features.size(); ++i){
            state = greedyChoice(state, features);
            if(state.getAccuracy() > bestFeatureSet.getAccuracy()){
                bestFeatureSet = state;
            }
        }

        System.out.print("Finished Search!! The best feature subset is ");
        bestFeatureSet.printFeatures();
        System.out.println(", which has an accuracy of " + decFor.format(bestFeatureSet.getAccuracy()));
        return bestFeatureSet;
    }

    public static Node greedyChoice(Node state, Vector<String> allFeatures){
        Node choice = new Node();
        double bestAccuracy = 0;

        Vector<String> currentFeatures = new Vector<>();
        state.copyFeatures(currentFeatures);

        DecimalFormat decFor = new DecimalFormat("0.0");
        decFor.setRoundingMode(RoundingMode.UP); //used to round up the accuracy

        for(int i=0; i<allFeatures.size(); ++i){
            String currFeature = allFeatures.get(i);
            if(!currentFeatures.contains(currFeature)){ //makes sure feature is not already in the current state
                Node newNode = new Node(state.getFeatures()); //create new node with state's features
                newNode.addFeature(currFeature); //add new feature

                if(newNode.getAccuracy() > bestAccuracy){
                    choice = newNode;
                    bestAccuracy = newNode.getAccuracy();
                }

                //print
                System.out.print("Using features ");
                newNode.printFeatures();
                System.out.println(" accuracy is " + decFor.format(newNode.getAccuracy()) + "%");
            }
        }
        System.out.print("\nFeature set ");
        choice.printFeatures();
        System.out.println(" was best, with accuracy of " + decFor.format(choice.getAccuracy()) + "%\n");
        return choice;
    }

    //=============================================
    // Accuracy calculations
    //=============================================

    public static double calcAccuracy(Vector<String> features){
        double correctTests = 0;

        return correctTests/ features.size();
    }

}


