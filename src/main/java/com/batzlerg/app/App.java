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
    final String BASE_DIR = "./static/"; // todo: configurable
    HttpServer server = HttpServer.create(new InetSocketAddress(PORT_NUMBER), 0);

    server.createContext("/", (HttpExchange exchange) -> {
      URI uri = exchange.getRequestURI();
      String filePath = uri.getPath();
      OutputStream os = exchange.getResponseBody();

      // todo: regex extension whitelist?
      // html file or extension-less
      try {
        String name = new File(uri.getPath()).getName();
        // alias "/" to index.html; todo: configurable
        if (name.length() == 0) {
          name = "index";
        }

        File file = new File(BASE_DIR, name.concat(".html"));

        if (file.exists()) {
          exchange.getResponseHeaders().set("Content-Type", "text/html");
          exchange.sendResponseHeaders(200, file.length());

          os.write(Files.readAllBytes(file.toPath()));
          os.close();
        } else {
          throw new IOException("file does not exist");
        }
      } catch (IOException e) {
        File errPage = new File(BASE_DIR, "404.html"); // todo: configurable

        System.err.println(e);

        exchange.sendResponseHeaders(404, 0);

        os.write(Files.readAllBytes(errPage.toPath()));
        os.close();
      }
    });
    server.setExecutor(null);
    server.start();
    System.out.print("Listening on port ");
    System.out.println(PORT_NUMBER);
  }
}
