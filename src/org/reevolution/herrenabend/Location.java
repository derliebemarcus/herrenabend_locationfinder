package org.reevolution.herrenabend;

import java.awt.geom.Point2D;

public class Location {
   
   String name,address,types;  
   Point2D.Double geocode;
      
   
   public Location() {

   }
   
   
   public String getName() {
      return name;
   }
   public void setName(String name) {
      this.name = name;
   }
   public String getAddress() {
      return address;
   }
   public void setAddress(String address) {
      this.address = address;
   }
   public String getTypes() {
      return types;
   }
   public void setTypes(String types) {
      this.types = types;
   }
   public Point2D.Double getGeocode() {
      return geocode;
   }
   public void setGeocode(Point2D.Double geocode) {
      this.geocode = geocode;
   }
   
   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("name: ");
      sb.append(getName());
      sb.append("\n");
      sb.append("adress: ");
      sb.append(getAddress());
      sb.append("\n");
      sb.append("type: ");
      sb.append(getTypes());
      sb.append("\n");
      sb.append("location: ");
      sb.append(getGeocode().getX());
      sb.append(", ");
      sb.append(getGeocode().getY());
      sb.append("\n");
            
      return sb.toString();
   }
}
