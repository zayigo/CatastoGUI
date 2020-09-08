package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage window) throws Exception {
    Parent layout = FXMLLoader.load(getClass().getResource("layout.fxml"));
    layout.getStylesheets().add(getClass().getResource("layout.css").toExternalForm());
    window.setScene(new Scene(layout));
    window.setMinWidth(530);
    window.setMinHeight(380);
    window.setTitle("CatastoGUI");
    window
        .getIcons()
        .addAll(
            new Image(getClass().getResourceAsStream("icons/icon64x64.png")),
            new Image(getClass().getResourceAsStream("icons/icon48x48.png")),
            new Image(getClass().getResourceAsStream("icons/icon32x32.png")),
            new Image(getClass().getResourceAsStream("icons/icon16x16.png"))
        );
    window.show();
  }
}
