package app;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.text.WordUtils;

@JsonDeserialize(using = ComuneDeserializer.class)
public class Comune extends Geometria {
  private final IntegerProperty codComune;
  private final StringProperty nomeComune;

  public Comune(int codComune, String nomeComune, double lat, double lng) {
    super(lat, lng);
    this.codComune = new SimpleIntegerProperty(codComune);
    this.nomeComune = new SimpleStringProperty(WordUtils.capitalizeFully(nomeComune));
  }

  public Comune(int codComune, String nomeComune) {
    this(codComune, nomeComune, 0, 0);
  }

  public static ObservableList<Comune> parseString(String json) throws IOException {
    List<Comune> comuni =
        new ObjectMapper().readValue(json, new TypeReference<ArrayList<Comune>>() {});
    return FXCollections.observableList(comuni);
  }

  @Override
  public String toString() {
    return "Comune: "
        + this.nomeComune
        + " Codice: "
        + this.codComune
        + " Coords: "
        + this.getCoordinate();
  }

  public StringProperty getNomeComune() {
    return this.nomeComune;
  }

  public IntegerProperty getCodComune() {
    return this.codComune;
  }

  @Override
  public boolean equals(Object comparedObject) {
    if (this == comparedObject) {
      return true;
    }
    if (!(comparedObject instanceof Comune)) {
      return false;
    }
    Comune comune = (Comune) comparedObject;
    return super.equals(comune)
        && this.codComune == comune.codComune
        && this.nomeComune.equals(comune.nomeComune);
  }
}
