package com.test.torrent.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;

import com.turn.ttorrent.common.Torrent;

public class TorrentFileGenerator {

  public static void main(String[] args) {
    validateArguments(args);
    String sourceFilePath = args[0];
    URI trackerUri = URI.create(args[1]);
    String createdBy = args[2];
    String outputDirectory = args[3];
    Torrent torrent;
    try {
      torrent = Torrent.create(new File(sourceFilePath), trackerUri, createdBy);
      FileOutputStream output =
          new FileOutputStream(outputDirectory + File.separator + "output.torrent");
      torrent.save(output);
      output.flush();
      output.close();
    } catch (NoSuchAlgorithmException | InterruptedException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void validateArguments(String[] args) {
    if (args.length < 4) {
      System.out.println(
          "Missing required arguments of source file path, tracker URI, created by and output directory. Please provide them and try again.");
      System.exit(1);
    }
  }

}
