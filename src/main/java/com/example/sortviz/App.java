package com.example.sortviz;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application {
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;


    private Visualizer visualizer;
    private int[] data;
    private Canvas canvas;

    public void start(Stage stage) {
        canvas = new Canvas(WIDTH, 450);

        //difine the buttons
        Button btnGenerate = new Button("Generate");
        Button btnBubble = new Button("Bubble Sort");
        Button btnQuick = new Button("Quick Sort");
        Button btnMerge = new Button("Merge Sort");


        Button btnPlay = new Button("▶ Play");
        Button btnPause = new Button("⏸ Pause");
        Button btnStep = new Button("⏭ Step");


        Slider sizeSlider = new Slider(20, 800, 150);
        sizeSlider.setShowTickLabels(true);
        sizeSlider.setShowTickMarks(true);
        sizeSlider.setBlockIncrement(10);
        sizeSlider.setMajorTickUnit(200);
        Label lblSize = new Label("Size:");


        Slider speedSlider = new Slider(0, 100, 30);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(25);
        Label lblSpeed = new Label("Speed(ms/step):");

        //layout
        HBox topBar = new HBox(10, btnGenerate, new Separator(), btnBubble, btnQuick, btnMerge, new Separator(), btnPlay, btnPause, btnStep, new Separator(), lblSize, sizeSlider, new Separator(), lblSpeed, speedSlider);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER_LEFT);

        //buttons and sliders are on the top, canvas is on the center
        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(canvas);

        //set scene's title and size and show it
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setTitle("Sorting Algorithm Visualizer (JavaFX)");
        stage.setScene(scene);
        stage.show();


        data = Utils.randomArray((int) sizeSlider.getValue(), 10, 1000);
        visualizer = new Visualizer(canvas, data);
        visualizer.draw();


        btnGenerate.setOnAction(e -> {
            if (visualizer.isRunning()) visualizer.stop();
            data = Utils.randomArray((int) sizeSlider.getValue(), 10, 1000);
            visualizer.setData(data);
            visualizer.clearOperations();
            visualizer.draw();
        });


        sizeSlider.valueProperty().addListener((obs, oldV, newV) -> {
            if (visualizer.isRunning()) return;
            int n = newV.intValue();
            data = Utils.randomArray(n, 10, 1000);
            visualizer.setData(data);
            visualizer.clearOperations();
            visualizer.draw();
        });


        speedSlider.valueProperty().addListener((obs, oldV, newV) -> {
            visualizer.setDelayMs(newV.longValue());
        });


        btnBubble.setOnAction(e -> {
            if (visualizer.isRunning()) return;
            visualizer.loadOperations(SortingAlgorithms.bubbleOps(data));
        });
        btnQuick.setOnAction(e -> {
            if (visualizer.isRunning()) return;
            visualizer.loadOperations(SortingAlgorithms.quickOps(data));
        });
        btnMerge.setOnAction(e -> {
            if (visualizer.isRunning()) return;
            visualizer.loadOperations(SortingAlgorithms.mergeOps(data));
        });


        btnPlay.setOnAction(e -> visualizer.play());
        btnPause.setOnAction(e -> visualizer.pause());
        btnStep.setOnAction(e -> visualizer.step());
    }

    public void stop() {
        if (visualizer != null) visualizer.stop();
        Platform.exit();
    }


    public static void main(String[] args) {
        launch(args);
    }
}