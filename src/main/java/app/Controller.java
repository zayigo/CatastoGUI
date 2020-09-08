package app;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Controller {
  @FXML WebView webViewMap;
  @FXML ComboBox<Comune> txtComune;
  @FXML TextField txtParticella;
  @FXML TableView<Particella> resultTable;
  @FXML TableColumn<Particella, String> colComune;
  @FXML TableColumn<Particella, String> colLoc;
  @FXML TableColumn<Particella, String> colPart;
  @FXML TableColumn<Particella, String> colCoord;
  @FXML Hyperlink githubUrl;
  private JavaBridge jsBridge;
  private SearchUtils searchUtils;

  /** Handles search button event */
  public void search(ActionEvent event) throws URISyntaxException {
    final PseudoClass errorClass = PseudoClass.getPseudoClass("error");
    boolean checkComune = searchUtils.checkComune();
    boolean checkParticella = searchUtils.checkParticella();
    txtComune.pseudoClassStateChanged(errorClass, checkComune);
    txtParticella.pseudoClassStateChanged(errorClass, checkParticella);
    System.out.println(checkParticella);
    if (checkComune) {
      txtComune.requestFocus();
      System.out.println("Nessun comune scelto");
    } else if (checkParticella) {
      txtParticella.requestFocus();
      System.out.println("Nessuna particella scelta");
    }
    if (!checkComune && !checkParticella) {
      String codComune = searchUtils.getComune();
      String particella = searchUtils.getParticella();
      Scraper scraper =
          new Scraper(
              String.format(
                  "http://mobilekat.provincia.tn.it/mobilekat/api/particelle/Get?codComuneCat=%s&partialName=%s",
                  codComune, particella));
      Thread taskThread =
          new Thread(
              () -> {
                ObservableList<Particella> particelle = null;
                try {
                  particelle = Particella.parseString(scraper.getResponse());
                } catch (IOException | InterruptedException | URISyntaxException e) {
                  e.printStackTrace();
                }
                if (particelle == null) {
                  System.out.println("Errore download");
                } else {
                  System.out.println("Risultato: " + particelle);
                  ObservableList<Particella> finalParticelle = particelle;
                  Platform.runLater(
                      () -> {
                        jsBridge.setTable(resultTable);
                        jsBridge.addMarkers(finalParticelle);
                        resultTable.setItems(finalParticelle);
                      });
                }
                event.consume();
              });
      taskThread.start();
    }
  }

  @FXML
  private void initialize() throws URISyntaxException {
    // WebView Setup
    WebEngine webEngine = webViewMap.getEngine();
    jsBridge = new JavaBridge(resultTable, webEngine);
    JavaBridge.installBridge(jsBridge);
    URL url = Controller.class.getResource("map.html");
    webEngine.load(url.toExternalForm());
    // TableView Setup
    colComune.setCellValueFactory(data -> data.getValue().getComune().getNomeComune());
    colLoc.setCellValueFactory(data -> data.getValue().getLocalita());
    colPart.setCellValueFactory(data -> data.getValue().getCodParticella());
    colCoord.setCellValueFactory(data -> data.getValue().getCoordinate());
    TableUtils tableUtils = new TableUtils(resultTable);
    tableUtils.configureTable("Effettua una ricerca");
    tableUtils.installClickHandler(jsBridge);
    tableUtils.installCopyPasteHandler();
    // ComboBox setup
    ComboBoxUtils comboBoxUtils = new ComboBoxUtils(txtComune);
    comboBoxUtils.installConverter();
    // comboBoxUtils.installValueHandler();
    comboBoxUtils.installQueryHandler();
    searchUtils = new SearchUtils(txtComune, txtParticella);
    // Hyperlink setup
    githubUrl.setOnAction(
        e -> {
          if (Desktop.isDesktopSupported()) {
            try {
              Desktop.getDesktop().browse(new URI("https://github.com/zayigo/CatastoGUI"));
            } catch (IOException | URISyntaxException e1) {
              e1.printStackTrace();
            }
          }
        });
  }
}
