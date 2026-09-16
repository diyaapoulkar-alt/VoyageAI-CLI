package com.vityarthi.voyageai;

import com.vityarthi.voyageai.web.WebServer;

/**
 * Main Web Server Application Entry Point for VoyageAI.
 * Launches the embedded HTTP server on port 8080.
 */
public class WebMain {

    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Invalid port specified. Defaulting to 8080.");
            }
        }

        try {
            WebServer webServer = new WebServer(port);
            webServer.start();
        } catch (Exception e) {
            System.err.println("❌ Error starting VoyageAI Web Server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
