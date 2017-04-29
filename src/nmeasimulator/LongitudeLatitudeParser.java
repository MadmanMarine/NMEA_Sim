/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nmeasimulator;

/**
 * Parses longitude or latitude [Ex: "75°36'12.20\"W"] to java double.
 * @author madma
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LongitudeLatitudeParser {
 private static String EXPRESSION = "^(?<deg>[-+0-9]+)[^0-9]+(?<min>[0-9]+)[^0-9]+(?<sec>[0-9.,]+)[^0-9.,ENSW]+(?<pos>[ENSW]*)$";

 /**
  * Parses the latitude and longitude to double
  * @param value
  * @return
  */
 public static double ParseLatLonValue(String value) {
  double result = Double.NaN;
  if (value.startsWith("\"") && value.endsWith("\"")) {
   value = value.substring(1, value.length() - 2).replace("\"\"", "\"");
  }
  Pattern pattern = Pattern.compile(EXPRESSION);
  Matcher matcher = pattern.matcher(value);
  double deg = Double.NaN;
  double min = Double.NaN;
  double sec = Double.NaN;
  char pos = '\0';
  if (matcher.matches()) {
   deg = Double.parseDouble(matcher.group("deg"));
   min = Double.parseDouble(matcher.group("min"));
   sec = Double.parseDouble(matcher.group("sec"));
   pos = matcher.group("pos").charAt(0);
   result = deg + (min / 60) + (sec / 3600);
   result = ((pos == 'S') || (pos == 'W')) ? -result : result;
  }
  return result;
 }
  
}
