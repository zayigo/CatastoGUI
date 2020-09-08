package app;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/** User input validation */
public class SearchUtils {
  private final ComboBox<Comune> comboBox;
  private final TextField textField;
  private String comune;
  private String particella;

  public SearchUtils(ComboBox<Comune> comboBox, TextField textField) {
    this.comboBox = comboBox;
    this.textField = textField;
    this.comune = "";
    this.particella = "";
  }

  public boolean checkComune() {
    try {
      this.comune =
          String.valueOf(comboBox.getSelectionModel().getSelectedItem().getCodComune().get())
              .trim();
    } catch (Exception e) {
      this.comune = "";
    }
    return this.comune.isEmpty();
  }

  public boolean checkParticella() {
    this.particella = textField.getText().trim();
    return this.particella.isEmpty();
  }

  public String getComune() {
    return this.comune;
  }

  public String getParticella() {
    return this.particella;
  }
}
