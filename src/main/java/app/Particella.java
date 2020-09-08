package app;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.text.WordUtils;

@JsonDeserialize(using = ParticellaDeserializer.class)
public class Particella extends Geometria {
  private final StringProperty codParticella;
  private final StringProperty localita;
  private final Comune comune;

  public Particella(
      int codComune,
      String nomeComune,
      String codParticella,
      String localita,
      double lat,
      double lng,
      double perimetro,
      double area) {
    super(lat, lng, perimetro, area);
    this.comune = new Comune(codComune, nomeComune);
    this.codParticella = new SimpleStringProperty(codParticella);
    this.localita = new SimpleStringProperty(WordUtils.capitalizeFully(localita));
  }

  public static ObservableList<Particella> parseString(String json) throws IOException {
    List<Particella> particelle =
        new ObjectMapper().readValue(json, new TypeReference<ArrayList<Particella>>() {});
    return FXCollections.observableList(particelle);
  }

  public Comune getComune() {
    return this.comune;
  }

  public StringProperty getCodParticella() {
    return this.codParticella;
  }

  public StringProperty getLocalita() {
    return this.localita;
  }

  @Override
  public String toString() {
    return "Particella: "
        + this.codParticella
        + " Comune: "
        + this.comune.getNomeComune()
        + " Coords: "
        + this.getCoordinate()
        + " Area: "
        + this.getArea()
        + " Perimetro: "
        + this.getPerimetro();
  }

  @Override
  public boolean equals(Object comparedObject) {
    if (this == comparedObject) {
      return true;
    }
    if (!(comparedObject instanceof Particella)) {
      return false;
    }
    Particella part = (Particella) comparedObject;
    return super.equals(part)
        && this.comune.equals(part.comune)
        && this.localita.equals(part.localita)
        && this.codParticella.equals(part.codParticella);
  }
}
