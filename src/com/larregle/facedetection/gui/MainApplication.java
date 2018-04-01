package com.larregle.facedetection.gui;

import com.larregle.facedetection.detector.FaceDetector;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;


public class MainApplication extends Application {

    public static final String TITLE = "Face detection practice";
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private final Scene scene;
    private final BorderPane borderPane;
    private final FileChooser fileChooser;

    private ImageView imageView;
    private File imageFile;

    public MainApplication() {
        borderPane = new BorderPane();
        scene = new Scene(borderPane, WIDTH, HEIGHT);
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png"));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle(TITLE);
        borderPane.setTop(createMenuBar(primaryStage));
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private MenuBar createMenuBar(Stage stage) {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem loadItem = new MenuItem("Load Image");
        MenuItem detectItem = new MenuItem("Detect faces");
        MenuItem exitItem = new MenuItem("Exit");
        fileMenu.getItems().addAll(loadItem, detectItem, exitItem);

        loadItem.setOnAction(e -> {
            String path;
            try {
                imageFile = fileChooser.showOpenDialog(stage);
                if (imageFile != null) {
                    path = imageFile.toURI().toURL().toExternalForm();
                    Image image = new Image(path, 700, 500, false, false);
                    imageView = new ImageView();
                    imageView.setFitWidth(700);
                    imageView.setFitHeight(500);
                    imageView.setPreserveRatio(true);
                    imageView.setImage(image);
                    borderPane.setCenter(imageView);
                }
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }

        });

        detectItem.setOnAction(e -> {
            try {
                List<Rectangle> detections = FaceDetector.getInstance().detectFaces(imageFile);
                borderPane.getChildren().addAll(detections);
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }
        });

        exitItem.setOnAction(e -> {
            if (quitAlert().showAndWait().get().equals(ButtonType.OK)) {
                Platform.exit();
                System.exit(0);
            }
        });

        menuBar.getMenus().add(fileMenu);
        return menuBar;
    }



    private Alert quitAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure to quit?");
        return alert;
    }

}
