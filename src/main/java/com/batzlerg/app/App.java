package com.batzlerg.app;

import java.io.IOException;
import java.io.OutputStream;
import java.io.File;
import java.nio.file.Files;
import java.net.InetSocketAddress;
import java.net.URI;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class App {
  public static void main(String args[]) throws Exception {
    final int PORT_NUMBER = 8000;
    final String BASE_DIR = "./static/";
    HttpServer server = HttpServer.create(new InetSocketAddress(PORT_NUMBER), 0);

    server.createContext("/", (HttpExchange exchange) -> {
      URI uri = exchange.getRequestURI();
      String filePath = uri.getPath();
        System.out.println(System.getProperty("user.dir"));

      if (filePath.contains(".html")) {
        System.out.println('1');
        // read file
        try {
          String name = new File(uri.getPath()).getName();
          File file = new File(BASE_DIR, name);
          System.out.println(name);

          if (file.exists()) {
            exchange.getResponseHeaders().set("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, file.length());

            OutputStream os = exchange.getResponseBody();
            os.write(Files.readAllBytes(file.toPath()));
            os.close();
          } else {
            throw new IOException("file does not exist");
          }
        } catch (IOException e) {
          System.err.println("File not found: " + e);
          exchange.sendResponseHeaders(404, 0);
        }
      }
      // todo: flesh out
      exchange.sendResponseHeaders(404, 0);
    });
    server.setExecutor(null);
    server.start();
    System.out.print("Listening on port ");
    System.out.println(PORT_NUMBER);
  }
}
