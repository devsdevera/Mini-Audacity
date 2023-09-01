// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP102/112 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP-102-112 - 2022T1, Assignment 9
 * Name:
 * Username:
 * ID:
 */

import ecs100.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.awt.Color;

/**
 * This program reads waveform data from a file and displays it
 * The program will also do some analysis on the data
 * The user can also edit the data - deleting, duplicating, and adding 
 *
 * The methods you are to complete all focus on the ArrayList of data.
 * It is related to assignment 3 which analysed temperature levels
 *
 * CORE
 *  display:            displays the waveform.
 *  read:               reads numbers into an ArrayList.
 *  showSpread:         displays the maximum and minimum values of the waveform.
 *  increaseRegion:     increases all the values in the selected region by 10%.
 *  decreaseRegion:     decreases all the values in the selected region by 10%.
 *  doubleFrequency:    removes every second value from the waveform.
 *
 * COMPLETION
 *  highlightPeaks:     puts small green circles around all the peaks in the waveform.
 *  displayDistortion:  shows in red the distorted part of the signal.
 *  deleteRegion:       deletes the selected region of the waveform

 * CHALLENGE
 *  duplicateRegion:    duplicates the selected region of the waveform
 *  displayEnvelope:    displays the envelope.
 *  save:               saves the current waveform values into a file.
 *  ....                allows more editing
 *                       
 */

public class WaveformAnalyser{

    // Constants: 
    public static final int ZERO_LINE = 300;    // dimensions of the graph for the display method
    public static final int GRAPH_LEFT = 10;
    public static final int GRAPH_WIDTH = 800;
    public static final int GRAPH_RIGHT = GRAPH_LEFT + GRAPH_WIDTH;

    public static final double THRESHOLD = 200;  // threshold for the distortion level
    public static final int CIRCLE_SIZE = 10;    // size of the circles for the highlightPeaks method

    // Fields 
    private ArrayList<Double> waveform;
    
    private ArrayList<Double> peaks;
    private ArrayList<Double> troughs;
    
    // the field to hold the ArrayList of values

    private int regionStart = 0; // The index of the first value in the selected region
    private int regionEnd;       // The index one past the last value in the selected region

    /**
     * Set up the user interface
     */
    public void setupGUI(){
        UI.setMouseListener(this::doMouse);   
        //core
        UI.addButton("Display", this::display);
        UI.addButton("Read Data", this::read);
        UI.addButton("Show Spread", this::showSpread);
        UI.addButton("Increase region", this::increaseRegion);
        UI.addButton("Decrease region", this::decreaseRegion);
        UI.addButton("Double frequency", this::doubleFrequency);
        //completion
        UI.addButton("Peaks", this::highlightPeaks);
        UI.addButton("Distortion", this::displayDistortion);
        UI.addButton("Delete", this::deleteRegion);
        //challenge
        UI.addButton("Duplicate", this::duplicateRegion);
        UI.addButton("Envelope", this::displayEnvelope);
        UI.addButton("Save", this::save);

        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(900, 650);
    }

    /**
     * [CORE]
     * Displays the waveform as a line graph,
     * Draw the axes
     * Plots a line graph of all the points with a blue line between
     *  each pair of adjacent points
     * The n'th value in waveform is at
     *    x-position is GRAPH_LEFT + n
     *    y-position is ZERO_LINE - the value
     * Don't worry if the data goes past the end the window
     */
    public void display(){
        if (this.waveform == null){ //there is no data to display
            UI.println("No waveform to display");
            
            return;
        }
        UI.clearGraphics();

        // draw x axis (showing where the value 0 will be)
        UI.setColor(Color.black);
        UI.drawLine(GRAPH_LEFT, ZERO_LINE, GRAPH_RIGHT, ZERO_LINE); 

        // plot points: blue line between each pair of values
        /*# YOUR CODE HERE */
        
        //UI.println(waveform);
        
        UI.setColor(Color.blue);
        
        for (int i = 0; i < waveform.size() - 1; i++ ){
            
            UI.drawLine(GRAPH_LEFT + i, ZERO_LINE - waveform.get(i), GRAPH_LEFT + i + 1, ZERO_LINE - waveform.get(i+1));
            
        }
        
        
        
        this.displayRegion();  // Displays the selected region, if any
    }

    /**
     * [CORE]
     * Clears the panes, 
     * Asks user for a waveform file (eg waveform1.txt)
     * The files consist of a sequence of numbers.
     * Creates an ArrayList stored in the waveform field, then
     * Reads data from the file into the ArrayList
     * calls display.
     */
    public void read(){
        UI.clearPanes();
        String fname = UIFileChooser.open();
        this.waveform = new ArrayList<Double>();   // create an empty list in the waveform field
        
        this.peaks = new ArrayList<Double>();
        this.troughs = new ArrayList<Double>();
        /*# YOUR CODE HERE */
        
        // uses a scanner to scan the file and add the value to the waveform arraylist. 
        
        try {
            Scanner scan = new Scanner(Path.of(fname));
            
            while (scan.hasNext()){
                
                double value = scan.nextDouble();
                
                //UI.println(value);
                
                waveform.add(value);
            }
            
            scan.close();        
                                    
            }catch (IOException e){UI.println("File Failure:" + e);}

        UI.printMessage("Read " + this.waveform.size() + " data points from " + fname);

        this.regionStart = 0;
        this.regionEnd = this.waveform.size();
        this.display();
    }

    /**
     * Displays the selected region by a red line on the axis
     */
    public void displayRegion(){
        UI.setColor(Color.red);
        UI.setLineWidth(3);
        UI.drawLine(GRAPH_LEFT+this.regionStart, ZERO_LINE, GRAPH_LEFT+this.regionEnd-1, ZERO_LINE);
        UI.setLineWidth(1);
    }

    /**
     * [CORE]
     * The spread is the difference between the maximum and minimum values of the waveform.
     * Finds the maximum and minimum values of the waveform, then
     * Displays the spread by drawing two horizontal lines on top of the waveform: 
     *   one green line for the maximum value, and
     *   one red line for the minimum value.
     */
    public void showSpread() {
        if (this.waveform == null){ //there is no data to display
            UI.println("No waveform to display");
            return;
        }
        this.display();
        /*# YOUR CODE HERE */
        
        /** Finding the maximum */
        
        double maximum = 0;
        double minimum = 99999999;
        
        // assumes that the value is always less than 99999999
        
        for (int i = 0; i < waveform.size(); i++ ){
            
            double value = waveform.get(i);
            if (value > maximum){
                maximum = value;
            }
            
            if (value < minimum){
                minimum = value;
            }
            
        }
        
        UI.println(maximum);
        UI.println(minimum);
        
        
        UI.setColor(Color.green);
        UI.drawLine(GRAPH_LEFT, ZERO_LINE - maximum, GRAPH_RIGHT, ZERO_LINE - maximum);
        
        UI.setColor(Color.red);
        UI.drawLine(GRAPH_LEFT, ZERO_LINE - minimum, GRAPH_RIGHT, ZERO_LINE - minimum);

    }

    /**
     * [CORE]
     * Increases the values in the selected region of the waveform by 10%.
     * (The selected region is initially the whole waveform, but the user can drag the
     *  mouse over part of the graph to select a smaller region).
     * The selected region goes from the index in the regionStart field to the index
     *  in the regionEnd field.
     */
    public void increaseRegion() {
        if (this.waveform == null){ //there is no waveform to process
            UI.println("No waveform");
            return;
        }
        /*# YOUR CODE HERE */
        
        
        for (int i = regionStart; i < regionEnd; i++){
            
            waveform.set(i, waveform.get(i) * 1.1);
            
            // I think this set method is really cool, it gets the value and changes it a wee bit
            
        }
        

        this.display();
    }

    /**
     * [CORE]
     * Decreases the values in the selected region of the waveform by 10%.
     * (The selected region is initially the whole waveform, but the user can drag the
     *  mouse over part of the graph to select a smaller region).
     * The selected region goes from the index in the regionStart field to the index
     *  in the regionEnd field.
     */
    public void decreaseRegion() {
        if (this.waveform == null){ //there is no waveform to process
            UI.println("No waveform");
            return;
        }
        /*# YOUR CODE HERE */
        
        for (int i = regionStart; i < regionEnd; i++){
            
            waveform.set(i, waveform.get(i) * 0.9);
            
            // this does the same thing as increase region but it decreases it by 10& rather than increases it. 
            
        }


        this.display();
    }

    /**
     * [CORE]
     * Double the frequency of the waveform by removing every second value in the list.
     * Resets the selected region to the whole waveform
     */
    public void doubleFrequency() {
        if (this.waveform == null){ //there is no waveform to process
            UI.println("No waveform");
            return;
        }
        /*# YOUR CODE HERE */
        
        //UI.println(regionStart);
        //UI.println(regionEnd);
        
        // WHY IS THIS NOT CHALLENGE? IT DEALS WITH THE COMPLICATIONS OF REMOVE METHOD
        
        int difference = regionEnd - regionStart;
        
        if((difference) %2 == 0){ // Even 
            
            int end = regionStart + (difference / 2);
            
            for (int i = regionStart + 1; i < end; i++){
            
                waveform.remove(i);
            
                // when you remove, everything else shifts. it removes the second one for me automatically, no need to backstep i
            
            }
        }
        else {
            int end = regionStart + ((difference + 1) / 2);
            
            for (int i = regionStart + 1; i < end; i++){
            
                waveform.remove(i);
            
                // when you remove, everything else shifts. it removes the second one for me automatically, no need to backstep i
            
            }
        }
        
        regionStart = 0;
        regionEnd = waveform.size();
        
        


        this.display();
    }

    /**
     * [COMPLETION]
     * Plots the peaks with small green circles. 
     *    A peak is defined as a value that is greater than or equal to both its
     *    neighbouring values.
     * Note the size of the circle is in the constant CIRCLE_SIZE
     */
    public void highlightPeaks() {
        this.display();     //use display if displayDistortion isn't complete
        /*# YOUR CODE HERE */
        
        for (int i = 1; i < waveform.size() - 1; i++ ){
            
            double previous = waveform.get(i - 1);
            
            double current = waveform.get(i);
            
            double next = waveform.get(i + 1);
            
            
            //UI.drawLine(GRAPH_LEFT + i, ZERO_LINE - waveform.get(i), GRAPH_LEFT + i + 1, ZERO_LINE - waveform.get(i+1));
            
            if (current >= previous && current >= next){
                
                UI.setColor(Color.green);
                UI.drawOval(GRAPH_LEFT + i - (CIRCLE_SIZE / 2), ZERO_LINE - waveform.get(i) - (CIRCLE_SIZE / 2), CIRCLE_SIZE, CIRCLE_SIZE);
                
                // the code above just draw a little green circle if the condtion holds true. 
            }
            
        }
        
        

    }

    /**
     * [COMPLETION]  [Fancy version of display]
     * Display the waveform as a line graph. 
     * Draw a line between each pair of adjacent points
     *   * If neither of the points is distorted, the line is BLUE
     *   * If either of the two end points is distorted, the line is RED
     * Draw the horizontal lines representing the value zero and thresholds values.
     * Uses THRESHOLD to determine distorted values.
     * Uses GRAPH_LEFT and ZERO_LINE for the dimensions and positions of the graph.
     * [Hint] You may find Math.abs(int a) useful for this method.
     * You may assume that all the values are between -250 and +250.
     */
    public void displayDistortion() {
        if (this.waveform == null){ //there is no data to display
            UI.println("No waveform to display");
            return;
        }
        UI.clearGraphics();

        // draw zero axis
        UI.setColor(Color.black);
        UI.drawLine(GRAPH_LEFT, ZERO_LINE, GRAPH_LEFT + this.waveform.size() , ZERO_LINE); 

        // draw thresholds
        /*# YOUR CODE HERE */
        
        UI.setColor(Color.green);
        UI.drawLine(GRAPH_LEFT, ZERO_LINE - THRESHOLD, GRAPH_RIGHT, ZERO_LINE - THRESHOLD);
        
        UI.setColor(Color.green);
        UI.drawLine(GRAPH_LEFT, ZERO_LINE + THRESHOLD, GRAPH_RIGHT, ZERO_LINE + THRESHOLD);
        
        // THRESHOLD = 200;
        
        for (int i = 0; i < waveform.size() - 1; i++ ){
            
            UI.setColor(Color.blue);
                
            UI.drawLine(GRAPH_LEFT + i, ZERO_LINE - waveform.get(i), GRAPH_LEFT + i + 1, ZERO_LINE - waveform.get(i+1));
            
            // this is one way to code it which was to graph it regularly first as blue but then if it meets the condition, redraws it and the previous line in red. 
            
            if (waveform.get(i) > THRESHOLD || waveform.get(i) < (-1 * THRESHOLD)){
                UI.setColor(Color.red);
                
                UI.drawLine(GRAPH_LEFT + i, ZERO_LINE - waveform.get(i), GRAPH_LEFT + i + 1, ZERO_LINE - waveform.get(i+1));
                
                UI.drawLine(GRAPH_LEFT + i - 1, ZERO_LINE - waveform.get(i -1), GRAPH_LEFT + i, ZERO_LINE - waveform.get(i));
    
            }
            
        }


        this.displayRegion();
    }

    /**
     * [COMPLETION]
     * Removes the selected region from the waveform
     * selection should be reset to be the whole waveform
     * redisplays the waveform
     */
    public void deleteRegion(){
        /*# YOUR CODE HERE */
        
        for (int i = regionStart; i < regionEnd; i++){
            
            waveform.remove(regionStart);
            
            // the reason why we remove regionstart is because when we remove, everything gets shifted down, so we are just removing the same index until regionEnd. 
            
            //waveform.remove(i);
            //i--;
            
            // when you remove, everything else shifts. it removes the second one for me automatically, no need to backstep i
            
        }
        
        regionStart = 0;
        regionEnd = waveform.size();
        
        this.display();
    }

    /**
     * [CHALLENGE]
     * If there is a selected region, then add a copy of that section to the waveform,
     * immediately following the selected region
     * selection should be reset to be the whole waveform
     * redisplay the waveform
     */
    public void duplicateRegion(){
        /*# YOUR CODE HERE */
        
        for (int i = regionStart; i < regionEnd; i++){
            
            
            waveform.add(regionEnd, waveform.get(i));
            
            // this is a unique way to add which specifies an index to add to, and the value
            
            //waveform.remove(i);
            //i--;
    
            // when you remove, everything else shifts. it removes the second one for me automatically, no need to backstep i
            
        }
    
        regionStart = 0;
        regionEnd = waveform.size();
        
        this.display();
    }

    /**
     * [CHALLENGE]
     * Displays the envelope (upper and lower) with GREEN lines connecting all the peaks.
     *    A peak is defined as a point that is greater than or equal to *both* neighbouring points.
     */
    public void displayEnvelope(){
        if (this.waveform == null){ //there is no data to display
            UI.println("No waveform to display");
            return;
        }
        this.display();  // display the waveform,

        /*# YOUR CODE HERE */
        
        for (int i = 1; i < waveform.size() - 1; i++ ){
            
            double previous = waveform.get(i - 1);
            
            double current = waveform.get(i);
            
            double next = waveform.get(i + 1);
            
            
            //UI.drawLine(GRAPH_LEFT + i, ZERO_LINE - waveform.get(i), GRAPH_LEFT + i + 1, ZERO_LINE - waveform.get(i+1));
            
            if (current >= previous && current >= next){
                
                UI.println("Index: " + i);
                UI.println("Value: " + waveform.get(i));
                UI.println();
                
                
                double Index = i; 
            
                peaks.add(Index);
                peaks.add(waveform.get(i));
                
                
                //Try to store the value and store its position in the waveform. 
            }
            if (current <= previous && current <= next){
                
                UI.println("Index: " + i);
                UI.println("Value: " + waveform.get(i));
                UI.println();
                
                double Index = i; 
                
                troughs.add(Index);
                troughs.add(waveform.get(i));
                
                
                
            }
            
        }
        
        UI.println(peaks);
        UI.println(peaks.size());
        UI.println(troughs);
        UI.println(troughs.size());
        
        
        for (int i = 0; i < (peaks.size() - 2) / 2; i ++){
            UI.setColor(Color.green);
            
            UI.drawLine(GRAPH_LEFT + peaks.get((2*i)), ZERO_LINE - peaks.get((2*i) + 1), GRAPH_LEFT + peaks.get((2*i) + 2), ZERO_LINE - peaks.get((2*i) + 3));
            
        }
        
        for (int i = 0; i < (troughs.size() - 2) / 2; i ++){
            UI.setColor(Color.green);
            
            UI.drawLine(GRAPH_LEFT + troughs.get((2*i)), ZERO_LINE - troughs.get((2*i) + 1), GRAPH_LEFT + troughs.get((2*i) + 2), ZERO_LINE - troughs.get((2*i) + 3));
            
        }
        
        peaks.clear();
        troughs.clear();
        
        //UI.drawLine(GRAPH_LEFT + peaks.get(0), ZERO_LINE - peaks.get(0 + 1), GRAPH_LEFT + peaks.get(0 + 2), ZERO_LINE - peaks.get(0 + 3));
        //UI.drawLine(GRAPH_LEFT + peaks.get(2), ZERO_LINE - peaks.get(2 + 1), GRAPH_LEFT + peaks.get(2 + 2), ZERO_LINE - peaks.get(2 + 3));
        
        //UI.drawLine(GRAPH_LEFT + peaks.get(3), ZERO_LINE - peaks.get(3 + 1), GRAPH_LEFT + peaks.get(3 + 2), ZERO_LINE - peaks.get(3 + 3));

    }

    /**
     * [CHALLENGE]
     * Saves the current waveform values into a file
     */
    public void save(){
        /*# YOUR CODE HERE */
        
        try {
            
            String fname = UIFileChooser.save("File to save to");
            
            PrintStream scan = new PrintStream(fname);
            
            // similar to scanner but rather than reading, it write back to the file. 
            
            for (int i = 0; i < waveform.size(); i++){
                
                if(waveform.get(i) != null){
                    
                    scan.println(waveform.get(i));
                    
                }
            }
            
            scan.close();
                           
        }catch (IOException e){UI.println("File Failure:" + e);}

    }

    /**
     * Lets user select a region of the waveform with the mouse
     * and deletes that section of the waveform.
     */
    public void doMouse(String action, double x, double y){
        int index = (int)x-GRAPH_LEFT;
        if (action.equals("pressed")){
            this.regionStart = Math.max(index, 0);
        }
        else if (action.equals("released")){
            if (index < this.regionStart){
                this.regionEnd = this.regionStart;
                this.regionStart = Math.max(index,this.waveform.size());
            }
            else {
                this.regionEnd = Math.min(index,this.waveform.size());
            }
            this.display();
        }

    }

    /**
     * Make a "triangular" waveform file for testing the other methods
     */
    public void makeTriangleWaveForm(){
        this.waveform = new ArrayList<Double>();
        for (int cycle=0; cycle<10; cycle++){
            for (int i=0; i<15; i++){this.waveform.add(i*18.0);}
            for (int i=15; i>-15; i--){this.waveform.add(i*18.0);}
            for (int i=-15; i<0; i++){this.waveform.add(i*18.0);}
        }
        this.regionStart = 0;
        this.regionEnd = this.waveform.size();
    }

    public static void main(String[] args){
        WaveformAnalyser wav = new WaveformAnalyser();
        wav.setupGUI();
        wav.makeTriangleWaveForm();
    }
}
