/**
 * This program reads a text file of commands and arguments and opens a window with an image
 *
 * Date Last Modified: 10/21/2018
 *
 * @author Charles Vidro
 *
 * CS1131, Fall 2018
 * Lab Section 3
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Week7Program extends Application {
    private static double[] sceneSize = new double[2]; // An array for the sceneDimensions
    private static ArrayList<double[]> lineArrays = new ArrayList<>(); // Contains information about all lines
    private static ArrayList<double[]> circleArrays = new ArrayList<>(); // Contains information about all circles
    private static ArrayList<double[]> rectangleArrays = new ArrayList<>(); // Contains information about all rectangles
    private static ArrayList<double[]> textCoordinatesArrays = new ArrayList<>(); // Contains coordinate information for all text
    private static ArrayList<String> textText = new ArrayList<>(); // Contains all text strings
    private static String fileTitle; // The file title (args[0])

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set. The primary stage will be embedded in
     *                     the browser if the application was launched as an applet.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages and will not be embedded in the browser.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane pane = new Pane();

        // Add any lines
        for (int i = 0; i < lineArrays.size(); i++) {
            Line line = new Line(lineArrays.get(i)[0],
                    lineArrays.get(i)[1],
                    lineArrays.get(i)[2],
                    lineArrays.get(i)[3]);
            line.setStroke(Color.rgb(127, 244, 16));
            pane.getChildren().add(line);
        }

        // Add any circle
        for (int i = 0; i < circleArrays.size(); i++) {
            Circle circle = new Circle(circleArrays.get(i)[0],
                    circleArrays.get(i)[1],
                    circleArrays.get(i)[2]);
            circle.setFill(Color.TRANSPARENT);
            circle.setStroke(Color.rgb(127, 244, 16));
            pane.getChildren().add(circle);
        }

        // Add any rectangles
        for (int i = 0; i < rectangleArrays.size(); i++) {
            Rectangle rectangle = new Rectangle(rectangleArrays.get(i)[0],
                    rectangleArrays.get(i)[1],
                    rectangleArrays.get(i)[2],
                    rectangleArrays.get(i)[3]);
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setStroke(Color.rgb(127, 244, 16));
            pane.getChildren().add(rectangle);
        }

        // Add any text
        for (int i = 0; i < textCoordinatesArrays.size(); i++) {
            Text text = new Text(textCoordinatesArrays.get(i)[0],
                    textCoordinatesArrays.get(i)[1],
                    textText.get(i));

            text.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 15));
            text.setFill(Color.rgb(127, 244, 16));
            pane.getChildren().add(text);
        }

        Scene scene = new Scene(pane, sceneSize[0], sceneSize[1]);
        scene.setFill(Color.BLACK);
        primaryStage.setTitle(fileTitle);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Reads data from .txt file and sends data to correct methods.
     *
     * @param args the file name.
     *
     * @return void.
     */
    public void readFile(String args) throws Exception {
        File file = new File(args);
        fileTitle = args;
        Scanner scanner = null;

        try {
            scanner = new Scanner(file, "UTF-8");

            while (scanner.hasNext()) {
                String command = scanner.next();

                if (command.equals("END")) {
                    break;
                } else if (command.length() >= 2 && command.substring(0, 2).equals("//")) {
                    scanner.nextLine();
                } else if (command.equals("﻿SIZE")) {  // Specific text code to roses file
                    ArrayList<Double> elements = getElements(2, scanner.nextLine());
                    size(elements.get(0), elements.get(1));
                } else if (command.equals("SIZE")) {
                    ArrayList<Double> elements = getElements(2, scanner.nextLine());
                    size(elements.get(0), elements.get(1));
                } else if (command.equals("LINE")) {
                    ArrayList<Double> elements = getElements(4, scanner.nextLine());
                    line(elements.get(0), elements.get(1), elements.get(2), elements.get(3));
                } else if (command.equals("CIRCLE")) {
                    ArrayList<Double> elements = getElements(3, scanner.nextLine());
                    circle(elements.get(0), elements.get(1), elements.get(2));
                } else if (command.equals("RECTANGLE")) {
                    ArrayList<Double> elements = getElements(4, scanner.nextLine());
                    rectangle(elements.get(0), elements.get(1), elements.get(2), elements.get(3));
                } else if (command.equals("TEXT")) {
                    double x = 0.0;
                    double y = 0.0;
                    try {
                        if (scanner.hasNextDouble()) {
                            x = scanner.nextDouble();
                        } else {
                            throw new RuntimeException("Incorrect Number of Arguments");
                        }
                        if (scanner.hasNextDouble()) {
                            y = scanner.nextDouble();
                        } else {
                            throw new RuntimeException("Incorrect Number of Arguments");
                        }
                        if (scanner.hasNextDouble()) {
                            throw new RuntimeException("Incorrect Number of Arguments");
                        }
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                    text(x, y, scanner.nextLine());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
                System.out.println("File was successfully loaded!");
            }
        }

        Application.launch();
    }

    /**
     * Gathers elements and checks for syntax errors
     *
     * @param n the expected number of arguments.
     * @param line all arguments.
     *
     * @return a list of arguments.
     */
    private ArrayList<Double> getElements(int n, String line) {
        ArrayList<Double> elements = new ArrayList<>();

        // Add elements to list
        Scanner scan = null;
        try {
            scan = new Scanner(line);
            while (scan.hasNextDouble()) {
                elements.add(scan.nextDouble());
            }
            if (elements.size() == n) {
                return elements;
            } else {
                throw new RuntimeException("Incorrect Number of Arguments");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            if (scan != null) {
                scan.close();
            }
        }

        for (int i = elements.size(); i < n; i++) {
            elements.add(0.0);
        }
        return elements;
    }

    /**
     * Assigns size values to sceneSize array.
     *
     * @param length the length of the scene.
     * @param width the width of the scene.
     *
     * @return void.
     */
    private void size(double length, double width) {
        sceneSize[0] = length;
        sceneSize[1] = width;
    }

    /**
     * Assigns argument values to lineArrays.
     *
     * @param x1 first x coordinate.
     * @param y1 first y coordinate.
     * @param x2 second x coordinate.
     * @param y1 second y coordinate.
     *
     * @return void.
     */
    private void line(double x1, double y1, double x2, double y2) {
        double[] newLine = {x1, y1, x2, y2};
        lineArrays.add(newLine);
    }

    /**
     * Assigns argument values to circleArrays.
     *
     * @param x x coordinate.
     * @param y y coordinate.
     * @param radius the circle radius.
     *
     * @return void.
     */
    private void circle(double x, double y, double radius) {
        double[] newCircle = {x, y, radius};
        circleArrays.add(newCircle);
    }

    /**
     * Assigns argument values to lineArrays.
     *
     * @param x x coordinate.
     * @param y y coordinate.
     * @param width the rectangle width.
     * @param length the rectangle length.
     *
     * @return void.
     */
    private void rectangle(double x, double y, double width, double length) {
        double[] newRectangle = {x, y, width, length};
        rectangleArrays.add(newRectangle);
    }

    /**
     * Assigns argument values to textCoordinatesArrays and textText
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param text the text to be displayed
     *
     * @return void.
     */
    private void text(double x, double y, String text) {
        double[] newTextCoordinates = {x, y};
        textCoordinatesArrays.add(newTextCoordinates);
        textText.add(text);
    }

    public static void main(String[] args) throws Exception {
        Week7Program self = new Week7Program();
        self.readFile(args[0]);
    }
}