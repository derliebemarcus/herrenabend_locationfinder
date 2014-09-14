package org.reevolution.herrenabend;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Date;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.JSONArray;
import org.json.JSONObject;

public class LocationFinder {

   boolean debug = true;
   
   // Google API Key
   final String API_KEY = "AIzaSyB_OWuqV_leBbD83iZG_HfnLfEmh-EHerM";
   final String UrlParameters = "&types=bar|cafe&radius=500&sensor=false&key=";
   final String OUTPUT = "json";
   final String LOCATION = "location=";
   final ArrayList<String> discardTypes = new ArrayList<String>();
   ArrayList<Location> locations = new ArrayList<Location>(); 

   String placesApiUrl = "https://maps.googleapis.com/maps/api/place/search/";

   public LocationFinder(String[] args) throws Exception {
      
      discardTypes.add("night_club");
      discardTypes.add("lodging");
      discardTypes.add("art_gallery");
      discardTypes.add("store");
      
      GeoCoordinates location = new GeoCoordinates();

      Point2D.Double newCoord = location.getRandomCoordinates();
      
       // call places API
       URL places = createPlacesUrl(newCoord);
       //URL places = new URL("https://maps.googleapis.com/maps/api/place/search/json?location=52.4891097761037,13.419420904414798&types=bar|cafe&radius=500&sensor=false&key=AIzaSyB_OWuqV_leBbD83iZG_HfnLfEmh-EHerM");
       HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
       public boolean verify(String string, SSLSession sslSession) {
       return true;
       }
       });
      
       // connect to places api
       HttpsURLConnection conn = (HttpsURLConnection) places.openConnection();
       debug("read timeout: " + conn.getReadTimeout());
       debug("request method: " + conn.getRequestMethod());
      
       //printSslStuff(conn);
       //printResponseStuff(conn);
      
       //get response and close connection
       String content = readPlacesContent(conn);
       conn.disconnect();
      
      
       //get locations
       extractLocations(content);
      
      //determine final herrenabend location
      //sendMail();
           
     

   }

   private void sendMail() throws Exception {
      Session s = Session.getInstance(System.getProperties());
      Transport t = s.getTransport("smtp");

      t.connect("antigone.aot.tu-berlin.de", "", "");
      MimeMessage myMimeMessage = new MimeMessage(s);
      // we could get address from current user here
      myMimeMessage.setFrom(new InternetAddress("herrenabend@herrenabend.org"));
      myMimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress("alexander.thiele@dai-labor.de"));
      myMimeMessage.setSubject("Herrenabend Vorschläge");
      myMimeMessage.setSentDate(new Date());
      StringBuffer mailContent = new StringBuffer();
      for (Location tmp : locations) {
         mailContent.append("==== result ====");
         mailContent.append("\n");
         mailContent.append(tmp.toString());
      }
      myMimeMessage.setContent(mailContent.toString(), "text/plain");
      t.sendMessage(myMimeMessage, myMimeMessage.getAllRecipients());
      debug("mail sent");
   }
   
   private int extractLocations(String response) throws Exception {

      //process json response
      JSONObject json = new JSONObject(response);
      JSONArray jsonHeader = json.getJSONArray("html_attributions");
      JSONArray jsonResults = json.getJSONArray("results");
      debug("header length: " + jsonHeader.length());
      debug("results length: " + jsonResults.length());
      debug("results status: " + json.getString("status"));
      
      if (jsonResults.length() == 0) return 0;
      
      boolean skip = false;
      for (int i=0;i<jsonResults.length();i++) {
         skip = false;
         JSONObject tmp = jsonResults.getJSONObject(i);
         debug("\n====== found result ======");
         //check types
         JSONArray types = tmp.getJSONArray("types");
         //debug("types: " + types.join(" | "));
         for (int j=0;j<types.length();j++) {
            //debug("- " + types.getString(j));
            if (discardTypes.contains(types.getString(j))) {
               debug("found unwanted type " + types.getString(j) + " - skipping entry");
               skip = true;
               break;
            }
         }
         if (skip) continue;
         Location loc = new Location();
         loc.setName(tmp.getString("name"));
         loc.setAddress(tmp.getString("vicinity"));
         loc.setTypes(types.join(" | "));
         
         //get geo coordinates
         JSONObject geoCoordinates = tmp.getJSONObject("geometry").getJSONObject("location");
         loc.setGeocode(new Point2D.Double(geoCoordinates.getDouble("lat"),geoCoordinates.getDouble("lng")));
         
         locations.add(loc);
         debug(loc.toString());
         
      }
      debug("found " + locations.size() + " possible locations");
      return locations.size();
   }
   
   
   private String readPlacesContent(HttpsURLConnection conn) throws Exception {
      StringBuffer json = new StringBuffer(); 
      BufferedReader content = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("UTF-8")));
      
      String line = null;
      // while(content.ready()) {
      while ((line = content.readLine()) != null) {
         //debug(line);
         json.append(line);
      }
      return(json.toString());
   }

   private URL createPlacesUrl(Point2D.Double coordinates) throws Exception {
      // construct places url
      StringBuffer placesUrl = new StringBuffer();
      placesUrl.append(placesApiUrl);
      placesUrl.append(OUTPUT);
      placesUrl.append("?");
      placesUrl.append(LOCATION);
      placesUrl.append(coordinates.x);
      placesUrl.append(",");
      placesUrl.append(coordinates.y);
      placesUrl.append(UrlParameters);
      placesUrl.append(API_KEY);

      debug("places url: " + placesUrl.toString());

      return (new URL(placesUrl.toString()));
   }

   private void printResponseStuff(HttpsURLConnection conn) throws Exception {
      debug("return code: " + conn.getResponseCode());
      debug("return message: " + conn.getResponseMessage());
      debug("content type: " + conn.getContentType());
      debug("content length: " + conn.getContentLength());
   }

   private void printSslStuff(HttpsURLConnection conn) throws Exception {
      // show ssl related stuff
      debug("Response Code : " + conn.getResponseCode());
      debug("Cipher Suite : " + conn.getCipherSuite());
      debug("\n");

      Certificate[] certs = conn.getServerCertificates();
      for (Certificate cert : certs) {
         debug("Cert Type : " + cert.getType());
         debug("Cert Hash Code : " + cert.hashCode());
         debug("Cert Public Key Algorithm : " + cert.getPublicKey().getAlgorithm());
         debug("Cert Public Key Format : " + cert.getPublicKey().getFormat());
         debug("\n");
      }
   }

   
   private void debug(String text) {
      if (debug) System.out.println(text);
   }
   
   /**
    * @param args
    */
   public static void main(String[] args) {
      // TODO Auto-generated method stub
      try {

         new LocationFinder(args);

      } catch (Exception e) {
         System.out.println("something went wrong! reason: " + e.getMessage());
         e.printStackTrace();
      }

   }

}
