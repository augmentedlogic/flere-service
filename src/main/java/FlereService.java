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

public class FlereService
{

    private String bind_to = "localhost";
    private int port = 8080;
    private boolean debug = false;
    private int backlog = 100;
    private int so_timeout = 5000;
    private LinkedHashMap<String, Object> handlers = new LinkedHashMap<String, Object>();

    public FlereService(String bind_to, int port)
    {
        this.port = port;
        this.bind_to = bind_to;
    }

    public void addHandler(String path, Object handler)
    {
        this.handlers.put(path, handler);
    }

    public void addHandler(Object handler)
    {
        this.handlers.put("/", handler);
    }

    public void setBacklog(int backlog)
    {
        this.backlog = backlog;
    }

    public void setDebug(boolean debug)
    {
        this.debug = debug;
    }

    public void setTimeout(int so_timeout)
    {
        this.so_timeout = so_timeout;
    }

    public void start() throws Exception
    {
        try (ServerSocket serverSocket = new ServerSocket(this.port, this.backlog, InetAddress.getByName(this.bind_to))) {
            while (true) {
                int thread_count = java.lang.Thread.activeCount();
                Runnable runnable =  new ServiceThread(serverSocket.accept(), this.so_timeout, this.handlers, this.debug);
                Thread thread = new Thread(runnable);
                thread.start();
            }
        } catch (IOException e) {
            throw e;
        }
    }

}

