package app;

import java.io.IOException;
import java.net.URISyntaxException;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

public class ComboBoxUtils {

  private final ComboBox<Comune> comboBox;
  private final Scraper scraper;
  private String value;
  private ObservableList<Comune> comuni;

  public ComboBoxUtils(ComboBox<Comune> comboBox) throws URISyntaxException {
    this.comboBox = comboBox;
    this.value = "";
    this.scraper =
        new Scraper("http://mobilekat.provincia.tn.it/mobilekat/api/comune/Get?partialName=");
    comuni = null;
  }

  public void installQueryHandler() {
    this.comboBox.addEventHandler(
        KeyEvent.KEY_RELEASED,
        event -> {
          this.value = this.comboBox.getEditor().getText();
          this.comboBox.getItems().clear();
          Thread taskThread =
              new Thread(
                  () -> {
                    if (!value.isEmpty()) {
                      scraper.setParams(value);
                    }
                    try {
                      comuni = Comune.parseString(scraper.getResponse());
                    } catch (IOException | InterruptedException | URISyntaxException e) {
                      e.printStackTrace();
                    }
                    System.out.println("Risultato: " + comuni);
                    event.consume();
                    Platform.runLater( // instructions to run on the main Thread
                        () -> {
                          comboBox.setVisibleRowCount(4);
                          comboBox.show();
                          comboBox.setItems(comuni);
                        });
                  });
          taskThread.start();
        });
  }

  /** Custom StringCoverter to display Comune instances as Combobox entries
   * supports both Comune to String and String to Comune */
  public void installConverter() {
    StringConverter<Comune> stringConverter =
        new StringConverter<>() {
          @Override
          public String toString(Comune comune) {
            if (comune == null) {
              return "";
            } else {
              return comune.getNomeComune().get();
            }
          }

          @Override
          public Comune fromString(String string) {
            if (!string.equals("")) {
              System.out.println(string);
              FilteredList<Comune> filteredList = new FilteredList<>(comuni);
              filteredList.setPredicate(comune -> comune.getNomeComune().get().equals(string));
              System.out.println(filteredList);
              return filteredList.get(0);
            }
            return null;
          }
        };
    this.comboBox.setConverter(stringConverter);
  }
}
