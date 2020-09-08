package app;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;

/** Converts Jackson JsonNode to Comune */
public class ComuneDeserializer extends StdDeserializer<Comune> {

  public ComuneDeserializer() {
    this(null);
  }

  public ComuneDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public Comune deserialize(JsonParser jp, DeserializationContext dc) throws IOException {
    JsonNode node = jp.getCodec().readTree(jp);
    int codComune = Integer.parseInt(node.get("cod_comune_cat").textValue());
    String nomeComune = node.get("nome_comune").textValue();
    double lat = Double.parseDouble(node.get("lat").textValue());
    double lng = Double.parseDouble(node.get("lon").textValue());
    return new Comune(codComune, nomeComune, lat, lng);
  }
}
