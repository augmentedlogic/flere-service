/**
  Copyright (c) 2019 Wolfgang Hauptfleisch <dev@augmentedlogic.com>

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:

  The above copyright notice and this permission notice shall be included in all
  copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  SOFTWARE.
 **/
package com.augmentedlogic.flere.service;

import java.net.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.util.regex.*;
import static java.nio.file.Paths.get;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;


public class HttpToolkit
{


    public static String shortUUID() {
        UUID uuid = UUID.randomUUID();
        long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
        return Long.toString(l, Character.MAX_RADIX);
    }


    static String readFile(String path) throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded);
    }

    public static void byteWrite(String filename, byte[] data) throws Exception
    {

       try (FileOutputStream stream = new FileOutputStream(filename)) {
               stream.write(data);
       }

    }


    protected static Map<String, String> splitQuery(URL url) throws UnsupportedEncodingException
    {
      Map<String, String> query_pairs = new LinkedHashMap<String, String>();
      String query = url.getQuery();

      if(query != null) {
         String[] pairs = query.split("&");
         for (String pair : pairs) {
             String[] kv = pair.split("=");
             if(kv.length > 1) {
                query_pairs.put(URLDecoder.decode(kv[0], "UTF-8"), URLDecoder.decode(kv[1], "UTF-8"));
             }
         }
      }

      return query_pairs;
    }


    protected static Map<String, String> parsePostUrlEncoded(String s) throws UnsupportedEncodingException
    {
          Map<String, String> query_pairs = new LinkedHashMap<String, String>();
          String[] parts = s.split("&");
          for(String part : parts) {
              String[] keyval = part.split("=");
              if(keyval.length > 1) {
                if(keyval[0] != null && keyval[1] != null) {
                  query_pairs.put(URLDecoder.decode(keyval[0], "UTF-8"), URLDecoder.decode(keyval[1], "UTF-8"));
                }
              }
          }
       return query_pairs;
    }


    protected static String extractPath(URL url) throws Exception
    {
      String path = null;
      try {
        path = url.getPath();
      } catch(Exception e) {
          throw e;
      }
      return path;
    }

    protected static String extractQuerystring(URL url) throws Exception
    {
      String querystring = null;
      try {
         querystring = url.getQuery();
      } catch(Exception e) {
         throw e;
      }
      return querystring;
    }


}


