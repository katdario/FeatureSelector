import java.io.*;
import java.util.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.lang.Math;

public class FeatureSelector {
    public static Vector<Vector<Double>> data = new Vector<Vector<Double>>();

    public static void main(String[] args) throws Exception{
        String fileName = "small-test-dataset.txt";
//        String fileName = "Large-test-dataset.txt";
//        String fileName = "CS170_Spring_2022_Large_data__20.txt";
//        String fileName = "CS170_Spring_2022_Small_data__20.txt";

        File file = new File(fileName);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while((line = reader.readLine()) != null){
            String[] lineArray = line.split(" ");   //splits the line into string array
            Vector<Double> lineVector = new Vector<>();   //converts string array to vectors
            for(int i=0; i<lineArray.length; ++i){
                if(!lineArray[i].equals(""))
                    lineVector.add(Double.parseDouble(lineArray[i]));
            }
            data.add(lineVector);
        }

        //prints the dataset
//        for(int i =0; i< data.size(); i++){
//            Vector<Double> dataLine = data.get(i);
//            for(int j=0; j<dataLine.size(); j++){
//                System.out.print(dataLine.get(j) + " ");
//            }
////            System.out.print(dataLine.get(1) );    //sample of how to get specific column
//            System.out.println();
//        }

        //Having string values of features {1,2,3,4, ... , n}
        Vector<String> features = new Vector<>();
        int numFeatures = data.get(0).size() - 1;
        for(int i=1; i<=numFeatures; i++){
            features.add(String.valueOf(i));
        }

        System.out.println("Type the number of algorithm you want to run: ");
        System.out.println("1. Forward Selection");
        System.out.println("2. Backward Elimination");
        System.out.print("\nYour choice: ");
        Scanner in = new Scanner(System.in);
        int choice = Integer.parseInt(in.nextLine());
        Node best;
        if(choice == 1)
            best = greedyForwardSearch(features);
        else
            best = greedyBackwardsSearch(features);
//        Node best = greedyForwardSearch(features);

        DecimalFormat decFor = new DecimalFormat("0.0");
        decFor.setRoundingMode(RoundingMode.UP);
        System.out.print("Finished Search!! The best feature subset is ");
        best.printFeatures();
        System.out.println(", which has an accuracy of " + decFor.format(best.getAccuracy()) + "%");
    }

    //=============================================
    // Greedy Search - Forward
    //=============================================

    public static Node greedyForwardSearch(Vector<String> features){
        Node state = new Node();
        Node bestFeatureSet = new Node();
        DecimalFormat decFor = new DecimalFormat("0.0");
        decFor.setRoundingMode(RoundingMode.UP); //used to round up the accuracy

        System.out.println("Using no features and \"random\" evaluation, I get an accuracy of " +
                decFor.format(state.getAccuracy()) + "%");
        System.out.println("\nBeginning search.\n");

        for(int i=0; i< features.size(); ++i){
            state = greedyForwardChoice(state, features);
            if(state.getAccuracy() > bestFeatureSet.getAccuracy()){
                bestFeatureSet = state;
            }
        }


        return bestFeatureSet;
    }

    public static Node greedyForwardChoice(Node state, Vector<String> allFeatures){
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

                //added this---------------------
                newNode.updateAccuracy(calcAccuracy(newNode.getFeatures()));

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
    // Greedy Search - Backwards
    //=============================================

    public static Node greedyBackwardsSearch(Vector<String> features){
        Node bestFeatures = new Node(features);
        Node state = new Node(features);
        state.updateAccuracy(calcAccuracy(state.getFeatures()));
        double bestAccuracy = state.getAccuracy();
        DecimalFormat decFor = new DecimalFormat("0.0");    //used to format doubles
        decFor.setRoundingMode(RoundingMode.UP); //used to round up the accuracy

        System.out.println("\nBeginning search.\n");

        System.out.print("Features ");
        state.printFeatures();
        System.out.println(" accuracy is " + decFor.format(state.getAccuracy()) + "%\n");

        while((state.getFeatures().size() != 0)){
            double stateAcc = state.getAccuracy();
            if(stateAcc > bestAccuracy){
                bestFeatures = state;
                bestAccuracy = stateAcc;
            }
            state = greedyBackwardsChoice(state.getFeatures());
        }
        return bestFeatures;
    }

    public static Node greedyBackwardsChoice(Vector<String> initialFeatures){
        Node initialNode = new Node(initialFeatures);
        Node bestNode = new Node();
        Vector<String> currentFeatures;
        double bestAccuracy = 0;
        double currentAccuracy;
        DecimalFormat decFor = new DecimalFormat("0.0");    //used to format doubles
        decFor.setRoundingMode(RoundingMode.UP); //used to round up the accuracy

        if(initialFeatures.size() == 1)
            return bestNode;
        for(int i=0; i<initialFeatures.size(); i++){
            //create a new node with feature i removed
//            Node state = new Node(initialFeatures);
            currentFeatures = initialNode.getFeatures();
            currentFeatures.remove(i);
            Node state = new Node(currentFeatures);
            currentAccuracy = calcAccuracy(currentFeatures);
            state.updateAccuracy(currentAccuracy);

            if(currentAccuracy > bestAccuracy){
                bestNode = state;
                bestAccuracy = currentAccuracy;
            }

            //print
            System.out.print("Using features ");
            state.printFeatures();
            System.out.println(" accuracy is " + decFor.format(state.getAccuracy()) + "%");
        }

        System.out.print("\nFeature set ");
        bestNode.printFeatures();
        System.out.println(" was best, with accuracy of " + decFor.format(bestAccuracy) + "%\n");

        return bestNode;
    }


    //=============================================
    // Accuracy calculations
    //=============================================

    public static double calcAccuracy(Vector<String> features){
        double correctTests = 0;
        int nearestRow;
        double classif;

        if(features.size() == 0){
            int rangeMin = 0;
            int rangeMax = 1;

            Random rand = new Random();
            return (rangeMin + (rangeMax - rangeMin) * rand.nextDouble()) ;
        }

        //create a new dataset with only the given features
        Vector<Vector<Double>> newDataSet = new Vector<>(); //this represents a new table with only the included features
        for(int i=0; i<data.size(); i++){
            Vector<Double> dataSetLine = new Vector<>();
            Vector<Double> origDataLine = data.get(i);  //original line
            for(int j=0; j< features.size(); j++){
                int column = Integer.parseInt(features.get(j));    //which column/feature we are extracting
                dataSetLine.add(origDataLine.get(column));
            }
            newDataSet.add(dataSetLine);
        }

        //calculating accuracy
        //we classify the left out (i) the same as nearest neighbor
        for(int i=0; i<newDataSet.size(); ++i){
            nearestRow = nearestInstanceRow(newDataSet, i);//find row of the nearest distance
            classif = data.get(nearestRow).get(0);  //get the first column in the nearest row(classification)
            if(classif == data.get(i).get(0))   //we classify the left out the same as nearest neighbor
                correctTests++;
        }

        return correctTests/ data.size() *100;
    }

    public static int nearestInstanceRow(Vector<Vector<Double>> dataSet, int leftOut){
        int nearestRow = 0;
        double nearestDist = Double.MAX_VALUE;  //we want the smallest value, so we start with largest
        double dist;
        Vector<Double> first = dataSet.get(leftOut);

        for(int i=0; i<dataSet.size(); i++){
            if(i != leftOut){
                Vector<Double> second = dataSet.get(i);
                dist = calcDist(first, second);
                if(dist < nearestDist){
                    nearestDist = dist;
                    nearestRow = i;
                }
            }
        }

        return nearestRow;
    }

    // distance formula is:
    //      sqrt( (x1-x2)^2 + (y1-y2)^2 + ... + (n1-n2)^2 )
    // where n is the number of features
    public static double calcDist(Vector<Double> first, Vector<Double> second){
        if(first.size() != second.size())
            return -1;

        double sum = 0;
        double diff;
        for(int i = 0; i<first.size(); ++i){
            diff = first.get(i) - second.get(i);    //feature difference: (x1-x2)
            sum += Math.pow(diff, 2);               //difference squared: (x1-x2)^2
        }

        return Math.sqrt(sum);
    }
}


