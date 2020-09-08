package app;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;

/** Converts Jackson JsonNode to Particella */
public class ParticellaDeserializer extends StdDeserializer<Particella> {

  public ParticellaDeserializer() {
    this(null);
  }

  public ParticellaDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public Particella deserialize(JsonParser jp, DeserializationContext dc) throws IOException {
    JsonNode node = jp.getCodec().readTree(jp);
    int codComune = Integer.parseInt(node.get("cod_comune_cat").textValue());
    String nomeComune = node.get("comune").textValue();
    String codParticella = node.get("cod_particella").textValue();
    String localita = node.get("localita").textValue();
    double lat = Double.parseDouble(node.get("lat").textValue());
    double lng = Double.parseDouble(node.get("lon").textValue());
    double perimetro = Double.parseDouble(node.get("perimeter").textValue().replace(",", "."));
    double area = Double.parseDouble(node.get("area").textValue().replace(",", "."));
    return new Particella(
        codComune, nomeComune, codParticella, localita, lat, lng, perimetro, area);
  }
}
