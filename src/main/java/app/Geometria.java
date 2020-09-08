package app;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/** Stores geographical information */
public class Geometria {
  private final DoubleProperty lat;
  private final DoubleProperty lng;
  private final DoubleProperty perimetro;
  private final DoubleProperty area;

  public Geometria(double lat, double lng, double perimetro, double area) {
    this.lat = new SimpleDoubleProperty(lat);
    this.lng = new SimpleDoubleProperty(lng);
    this.perimetro = new SimpleDoubleProperty(perimetro);
    this.area = new SimpleDoubleProperty(area);
  }

  public Geometria(double lat, double lng) {
    this(lat, lng, 0, 0);
  }

  public StringProperty getCoordinate() {
    return new SimpleStringProperty(lat.get() + "," + lng.get());
  }

  public Double getLat() {
    return this.lat.get();
  }

  public Double getLng() {
    return this.lng.get();
  }

  public DoubleProperty getPerimetro() {
    return perimetro;
  }

  public DoubleProperty getArea() {
    return area;
  }

  @Override
  public String toString() {
    return this.getCoordinate().get();
  }

  @Override
  public boolean equals(Object comparedObject) {
    if (this == comparedObject) {
      return true;
    }
    if (!(comparedObject instanceof Geometria)) {
      return false;
    }
    Geometria comparedGeometry = (Geometria) comparedObject;
    return this.lat == comparedGeometry.lat
        && this.lng == comparedGeometry.lng
        && this.perimetro == comparedGeometry.perimetro
        && this.area == comparedGeometry.area;
  }
}
