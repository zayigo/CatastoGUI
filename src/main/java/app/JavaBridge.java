package app;

import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.scene.control.TableView;
import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

/** Java to JS and JS to Java bridge */
public class JavaBridge {
  TableView<Particella> table;
  WebEngine webEngine;

  public JavaBridge(TableView<Particella> table, WebEngine webEngine) {
    this.table = table;
    this.webEngine = webEngine;
  }

  public static void installBridge(JavaBridge javaBridge) {
    // Fix for new garbage collection
    WebEngine webEngine = javaBridge.getWebEngine();
    webEngine
        .getLoadWorker()
        .stateProperty()
        .addListener(
            (ov, oldState, newState) -> {
              if (newState == Worker.State.SUCCEEDED) {
                JSObject win = (JSObject) webEngine.executeScript("window");
                win.setMember("bridge", javaBridge);
              }
            });
  }

  public void setTable(TableView<Particella> table) {
    this.table = table;
  }

  public void toConsole(String msg) {
    System.out.println("JS to Java: " + msg);
  }

  public void focusRow(String query) {
    for (int i = 0; i < table.getItems().size(); i++) {
      String stringValue = table.getItems().get(i).getCodParticella().get();
      if (stringValue.equals(query)) {
        System.out.println("Focus riga:  " + i);
        table.requestFocus();
        table.getSelectionModel().select(i);
        table.scrollTo(i);
      }
    }
  }

  public void addMarkers(ObservableList<Particella> particelle) {
    StringBuilder jsFunction = new StringBuilder("addMarkers([");
    for (Particella part : particelle) {
      String obj =
          String.format(
              "{ lat: %s, lng: %s, title: '%s' },",
              part.getLat().toString(), part.getLng().toString(), part.getCodParticella().get());
      jsFunction.append(obj);
    }
    jsFunction.append("])");
    this.runScript(jsFunction.toString());
  }

  public void centerOn(String lat, String lng) {
    String jsFunction = String.format("centerOn(%s, %s)", lat, lng);
    this.runScript(jsFunction);
  }

  public void runScript(String script) {
    System.out.println("Java to JS: " + script);
    this.webEngine.executeScript(script);
  }

  public WebEngine getWebEngine() {
    return this.webEngine;
  }
}
