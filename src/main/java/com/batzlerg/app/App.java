package com.batzlerg.app;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class App {
  public static void main(String args[]) throws Exception {
    final int PORT_NUMBER = 8000;
    HttpServer server = HttpServer.create(new InetSocketAddress(PORT_NUMBER), 0);

    server.createContext("/test", new MyHandler());
    server.setExecutor(null);
    server.start();
    System.out.print("Listening on port ");
    System.out.println(PORT_NUMBER);
  }

  static class MyHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange t) throws IOException {
      String response = "This is the response";
      t.sendResponseHeaders(200, response.length());
      OutputStream os = t.getResponseBody();
      os.write(response.getBytes());
      os.close();
    }
  }
}
