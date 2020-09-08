package app;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class TableUtils {
  private final TableView<Particella> table;

  public TableUtils(TableView<Particella> table) {
    this.table = table;
  }

  public void installCopyPasteHandler() {
    this.table.setOnKeyPressed(new TableKeyEventHandler());
  }

  /** Copies latitude and longitude to clipboard on ctrl+C */
  public void copyText() {
    String elementTableSelected =
        this.table.getSelectionModel().getSelectedItem().getCoordinate().get();
    final Clipboard clipboard = Clipboard.getSystemClipboard();
    final ClipboardContent content = new ClipboardContent();
    content.putString(elementTableSelected);
    clipboard.setContent(content);
    System.out.println("Copiato: " + elementTableSelected);
  }

  /** Center map on the marker of the selected row */
  public void installClickHandler(JavaBridge javaBridge) {
    this.table.addEventHandler(
        MouseEvent.MOUSE_RELEASED,
        event -> {
          if (this.table.getItems().size() != 0) {
            String lat = this.table.getSelectionModel().getSelectedItem().getLat().toString();
            String lng = this.table.getSelectionModel().getSelectedItem().getLng().toString();
            javaBridge.centerOn(lat, lng);
          }
          event.consume();
        });
  }

  public void configureTable(String text) {
    Label placeholderLabel = new Label(text);
    placeholderLabel.setTextFill(Color.web("#A9A9A9"));
    placeholderLabel.setFont(new Font(20));
    this.table.setPlaceholder(placeholderLabel);
  }

  private class TableKeyEventHandler implements EventHandler<KeyEvent> {
    KeyCodeCombination copyKeyCodeCombination =
        new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY);

    public void handle(final KeyEvent keyEvent) {
      if (copyKeyCodeCombination.match(keyEvent)) {
        if (keyEvent.getSource() instanceof TableView) {
          copyText();
          keyEvent.consume();
        }
      }
    }
  }
}
