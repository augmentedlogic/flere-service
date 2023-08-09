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
import java.net.*;

public class HttpRequest
{

    private HashMap<String, String> _requestHeaders;
    private String method = null;
    private String query = null;
    private String querystring = null;
    private String path = null;
    private String postdata = null;



    private String remote_address = null;
    private Map<String, String> parameters = new LinkedHashMap<String, String>();
    protected static final ArrayList<String> ALLOWED_METHODS = new ArrayList<String>(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));

    private boolean has_basic_auth_data;
    private String basic_auth_user = null;
    private String basic_auth_password = null;

    public HttpRequest() {
        _requestHeaders = new HashMap<String, String>();
    }


    /**
     *  removed all html from a string
     *  if string is null, null is returned
     **/
    private static String stripped(String html_string)
    {
        String no_html_string = null;
        if (html_string != null) {
            no_html_string = html_string.replaceAll("(\\<.*?\\>|&lt;.*?&gt;)", "").trim();
        }
        return no_html_string;
    }


    protected void appendHeaderParameter(String header) throws Exception
    {
        int idx = header.indexOf(":");
        if (idx == -1) {
            throw new Exception("Invalid Header Parameter: " + header);
        }
        this._requestHeaders.put(header.substring(0, idx).toLowerCase(), header.substring(idx + 1, header.length()).trim());
        if(header.substring(0, idx).toLowerCase().equals("authorization")) {
           String astring = header.substring(idx + 1, header.length()).trim();
           if(astring.contains("Basic")) {
             this.parseBasicAuth(astring);
           }
        }
    }


    protected void setMethod(String method)
    {
        this.method = method;
    }


    protected void setRemoteAddress(String remote_address)
    {
        this.remote_address = remote_address;
    }


    protected void setQuery(String query) throws Exception
    {
        try {
            if(query != null) {
                this.parameters = HttpToolkit.splitQuery(new URL("http://example.com" + query));
            }
            this.path = HttpToolkit.extractPath(new URL("http://example.com" + query));
            this.querystring = HttpToolkit.extractQuerystring(new URL("http://example.com" + query));
        } catch(Exception e) {
            throw e;
        }
        this.query = query;
    }

    protected void appendParameters(Map<String, String> aparams)
    {
        Iterator it = aparams.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            this.parameters.put((String) pair.getKey(), (String) pair.getValue());
            it.remove();
        }
    }

    protected void parseBasicAuth(String header_value)
    {

          String[] sparts = header_value.split(" ");
          String basic_auth = new String(Base64.getDecoder().decode(sparts[1]));
          String[] ba = basic_auth.split(":");
          this.basic_auth_user = ba[0];
          this.basic_auth_password = ba[1];
    }


    protected void setPostdata(String postdata)
    {
        this.postdata = postdata;
    }

    protected Map<String, String> getParameters()
    {
        return this.parameters;
    }

    /**
     *  methods exposed to the handler
     **/
    public String getMethod()
    {
        return this.method;
    }

    public String getPath()
    {
        return this.path;
    }

    public String getPathElement(int position)
    {
        String element = null;
        if(this.path != null) {
            String[] parts = this.path.split("/");
            if(parts.length >= position + 1) {
                element = parts[position];
            }
        }
        return element;
    }

    public String getPathElement(int position, String default_value)
    {
        String element = this.getPathElement(position);
        if(element == null) {
            element = default_value;
        }
        return element;
    }

    public String getQuery()
    {
        return this.querystring;
    }

    public String getPostdata()
    {
        return this.postdata;
    }

    public String getRemoteAddress()
    {
        return this.remote_address;
    }

    public String getBasicAuthUser()
    {
        return this.basic_auth_user;
    }

    public String getBasicAuthPassword()
    {
        return this.basic_auth_password;
    }

    public String getParameter(String key)
    {
        return this.parameters.get(key);
    }

    public String getString(String key, String default_value)
    {
        String s = this.getString(key);
        if(s == null) {
           s = default_value;
        }
        return s;
    }

    public String getString(String key)
    {
        return stripped(this.parameters.get(key));
    }


    public Double getDouble(String key, Double default_value)
    {
        Double d = default_value;
        try {
            d = new Double(this.parameters.get(key));
        } catch(Exception e) {
            d = default_value;
        }
        return d;
    }

    public Double getDouble(String key)
    {
        return this.getDouble(key, null);
    }


    public Integer getInteger(String key, Integer default_value)
    {
        Integer i = default_value;
        try {
            i = new Integer(this.parameters.get(key));
        } catch(Exception e) {
            i = default_value;
        }
        return i;
    }

    public Integer getInteger(String key)
    {
        return this.getInteger(key, null);
    }


    public String getHeader(String header)
    {
        return this._requestHeaders.get(header.toLowerCase());
    }


}

