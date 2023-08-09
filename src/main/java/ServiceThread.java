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

import java.nio.channels.*;
import java.util.*;
import java.io.*;
import java.nio.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.lang.reflect.*;
import java.lang.Object;

class ServiceThread implements Runnable
{

    private final Socket socket;
    private Object handler = null;
    private LinkedHashMap<String, Object> handlers = null;
    private int so_timeout = 20000;
    private boolean debug = false;

    @SuppressWarnings("unchecked")
    public ServiceThread(Socket socket, int so_timeout, LinkedHashMap handlers, boolean debug)
    {
        this.socket = socket;
        this.debug = debug;
        this.handlers = handlers;
        this.so_timeout = so_timeout;
    }

    @Override
    public void run() {
        try {
            boolean bad_request = false;

            this.socket.setSoTimeout(this.so_timeout);

            // The incoming stream
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Character output: for headers
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            // Binary output for requested data
            BufferedOutputStream dataOut = new BufferedOutputStream(socket.getOutputStream());

            // read the request first to avoid connection reset errors
            int line_counter = 1;
            HttpRequest httpRequest = new HttpRequest();

            // setting the remote_address
            String remote_address=(((InetSocketAddress) this.socket.getRemoteSocketAddress()).getAddress()).toString().replace("/","");
            httpRequest.setRemoteAddress(remote_address);

            // reading the incoming headers
            while (true) {
                String requestLine = in.readLine();
                if (requestLine == null || requestLine.length() == 0) {
                    break;
                } else {
                    // first line should be the request
                    if(line_counter == 1) {
                       String[] parts = requestLine.split(" ");
                       // TODO: if not 3 parts, invalid HTTP request
                       String method = requestLine.substring(0, requestLine.indexOf(' '));
                       httpRequest.setMethod(method);
                       String query = parts[1];
                       httpRequest.setQuery(query);
                    } else {
                       httpRequest.appendHeaderParameter(requestLine);
                    }
                }
                line_counter++;
            }


            // determin if there is a content length set
            Integer content_length = null;
              try {
                  content_length = Integer.parseInt(httpRequest.getHeader("content-length"));
              } catch(Exception e) {
                  // if there is no content length header, we fail silently and move on
              }
              if(content_length == null) {
                  content_length = 0;
              }

            String post_body = null;

            // we only handle the post body if content length is set
            if(content_length > 0) {

                // Reading the post body if required
                if(httpRequest.getMethod().equals("POST") || httpRequest.getMethod().equals("PUT") || httpRequest.getMethod().equals("PATCH")) {
                    char[] cl = new char[content_length];
                        for (int i = 0; i < content_length; i++) {
                            char[] one = new char[1];
                            in.read(one);
                            if(String.valueOf(one[0]) == null) {
                               break;
                            } else {
                               cl[i] = one[0];
                            }
                        }
                        post_body = String.valueOf(cl);
                        httpRequest.setPostdata(post_body);
                    // if application/x-www-form-urlencoded
                    if(httpRequest.getMethod().equals("POST")) {
                        if(httpRequest.getHeader("content-type").startsWith("application/x-www-form-urlencoded")) {
                            httpRequest.appendParameters(HttpToolkit.parsePostUrlEncoded(post_body));
                        }
                    }
                }

            } // end post body handling


            // for debugging only
            if(debug) {
                System.out.println("POSTDATA:\n" + post_body);
                System.out.println("PARAMETERS:\n" + httpRequest.getParameters());
            }


            // the routing decision
            String handler_path = httpRequest.getPath();
            Iterator it = this.handlers.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                String test_path = (String) pair.getKey();
                if(handler_path.startsWith(test_path)) {
                    this.handler = (Object) pair.getValue();
                    break;
                }
            }


            // preparing the response
            HttpResponse response = new HttpResponse();

            // run the selected handler
            if(this.handler != null) {
              try {
                  Class[] parameterTypes = new Class[] {HttpRequest.class};
                  Object[] arguments = new Object[] { httpRequest };
                  Method m = this.handler.getClass().getMethod("handle", parameterTypes);
                  response = (HttpResponse) m.invoke(this.handler, arguments);
              } catch(Exception e) {
                  Thread t = Thread.currentThread();
                  t.getUncaughtExceptionHandler().uncaughtException(t, e);
              }
            }

            String response_body = response.getPayload() + "\n";
            int response_content_length = (int) response_body.getBytes("UTF-8").length;

            // TODO: use response content type
            String contentMimeType = "text/html";

            // sending HTTP Headers
            out.println("HTTP/1.1 " + HttpStatus.HTTP_STATUS.get(response.getHttpStatus()));
            // adding custom HTTP headers
            Iterator ith = response.getHeaders().entrySet().iterator();
            while (ith.hasNext()) {
                Map.Entry pair = (Map.Entry) ith.next();
                out.println(pair.getKey() + ": " + pair.getValue());
            }
            out.println("Content-type: " + response.getContentType());
            out.println("Content-length: " + response_content_length);
            out.println();
            out.flush();

            // sending body
            dataOut.write(response_body.getBytes(), 0, response_content_length);
            dataOut.flush();
            this.socket.close();
        } catch (Exception e) {
              Thread t = Thread.currentThread();
              t.getUncaughtExceptionHandler().uncaughtException(t, e);
        }

    }

}

