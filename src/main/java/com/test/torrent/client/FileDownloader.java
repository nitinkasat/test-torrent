package com.test.torrent.client;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
import java.util.Observable;
import java.util.Observer;

import com.turn.ttorrent.client.Client;
import com.turn.ttorrent.client.SharedTorrent;

public class FileDownloader {

  public static void main(String[] args) {
    validateArguments(args);
    String torrentFileLocation = args[0];
    String outputDirectory = args[1];
    downloadFile(torrentFileLocation, outputDirectory);
    System.out.println("File downloaded successfully and saved in " + outputDirectory);
  }

  private static void validateArguments(String[] args) {
    if (args.length < 2) {
      System.out.println(
          "Missing required argument of path to torrent file and output directory to save downloaded file to. Please provide the same and try again.");
      System.exit(1);
    }
  }

  private static void downloadFile(String torrentFileLocation, String outputDirectory) {
    Client client = null;
    try {
      client = new Client(InetAddress.getLocalHost(),
          SharedTorrent.fromFile(new File(torrentFileLocation), new File(outputDirectory)));
      client.download();
      client.addObserver(new Observer() {
        @Override
        public void update(Observable observable, Object data) {
          Client client = (Client) observable;
          float progress = client.getTorrent().getCompletion();
          System.out.println(progress + "% file downloaded.");
        }
      });
      client.waitForCompletion();
    } catch (NoSuchAlgorithmException | IOException exception) {
      throw new RuntimeException(exception);
    } finally {
      if (client != null) {
        client.stop();
      }
    }
  }

}
