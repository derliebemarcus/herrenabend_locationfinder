package org.reevolution.herrenabend;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.Random;

@SuppressWarnings("serial")
public class GeoCoordinates extends Path2D.Double {

   double width, height;

   public GeoCoordinates() {

      // start of path
      moveTo(52.510945, 13.295245);

      // draw the polygon surrounding all places of interest
      lineTo(52.499974, 13.306918);
      lineTo(52.486491, 13.322625);
      lineTo(52.484766, 13.344941);
      lineTo(52.486413, 13.360226);
      lineTo(52.48508, 13.385332);
      lineTo(52.490986, 13.395588);
      lineTo(52.489078, 13.406446);
      lineTo(52.486282, 13.42387);
      lineTo(52.468142, 13.429878);
      lineTo(52.472639, 13.455155);
      lineTo(52.493599, 13.460863);
      lineTo(52.503318, 13.46833);
      lineTo(52.5134, 13.475111);
      lineTo(52.523585, 13.464768);
      lineTo(52.529433, 13.454297);
      lineTo(52.539771, 13.43992);
      lineTo(52.544364, 13.426273);
      lineTo(52.549427, 13.41357);
      lineTo(52.542433, 13.393486);
      lineTo(52.543425, 13.379495);
      lineTo(52.54225, 13.366621);
      lineTo(52.536273, 13.344777);
      lineTo(52.533506, 13.328469);
      lineTo(52.5134, 13.322418);
      lineTo(52.510945, 13.295245);

   }

   public Point2D.Double getRandomCoordinates() {

      System.out.println("max x: " + getBounds2D().getMaxX());
      System.out.println("max y: " + getBounds2D().getMaxY());
      System.out.println("min x: " + getBounds2D().getMinX());
      System.out.println("min y: " + getBounds2D().getMinY());

      width = getBounds2D().getMaxX() - getBounds2D().getMinX();
      height = getBounds2D().getMaxY() - getBounds2D().getMinY();
      System.out.println("width: " + width);
      System.out.println("height: " + height);

      Point2D.Double retVal = null;
      boolean outside = true;

      while (outside) {

         Random rc = new Random();

         Point2D.Double randCoord1 = new Point2D.Double();
         Point2D.Double randCoord2 = new Point2D.Double();

         randCoord1.x = getBounds2D().getMaxX() - (rc.nextDouble() * width);
         randCoord2.x = getBounds2D().getMinX() + (rc.nextDouble() * width);

         randCoord1.y = getBounds2D().getMaxY() - (rc.nextDouble() * height);
         randCoord2.y = getBounds2D().getMinY() + (rc.nextDouble() * height);

         // test if coords are inside area
         if (contains(randCoord1)) {
            System.out.println("hit 1: " + randCoord1.toString());
            retVal = randCoord1;
            outside = false;
            break;
         }
         if (contains(randCoord2)) {
            System.out.println("hit 2: " + randCoord2.toString());
            retVal = randCoord1;
            outside = false;
            break;
         }
      }
      return retVal;
   }
   

}
