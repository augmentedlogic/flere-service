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

import java.util.*;

public class HttpResponse
{

     private String payload = "no handler found";
     private String content_type = "text/html";
     private int http_status = 404;
     private LinkedHashMap<String, String> headers = new LinkedHashMap<String,String>();


     protected String getPayload()
     {
         return this.payload;
     }

     protected int getHttpStatus()
     {
         return this.http_status;
     }

     protected String getContentType()
     {
         return this.content_type;
     }

     protected LinkedHashMap<String,String> getHeaders()
     {
         return this.headers;
     }


     /**
      *
      **/
     public void addHeader(String header_name, String header_value)
     {
        headers.put(header_name, header_value);
     }

     public void setBody(String body)
     {
        this.payload = body;
     }

     public void setHttpStatus(int http_status)
     {
        this.http_status = http_status;
     }

     public void setContentType(String content_type)
     {
        this.content_type = content_type;
     }


}
