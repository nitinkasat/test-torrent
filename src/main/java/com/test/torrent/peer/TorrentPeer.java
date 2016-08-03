package com.test.torrent.peer;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.io.FileUtils;

import com.turn.ttorrent.client.Client;
import com.turn.ttorrent.client.SharedTorrent;

public class TorrentPeer {

  public static void main(String[] args) {
    validateArguments(args);
    String torrentFolderDirectory = args[0];
    String filesToShare = args[1];
    startPeer(torrentFolderDirectory, filesToShare);
  }

  private static void startPeer(String torrentFolderDirectoryPath, String filesToShare) {
    try {
      FilenameFilter filter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
          return name.endsWith(".torrent");
        }
      };
      File torrentFolderDirectory = new File(torrentFolderDirectoryPath);
      for (File file : torrentFolderDirectory.listFiles(filter)) {
        SharedTorrent sharedTorrent =
            new SharedTorrent(FileUtils.readFileToByteArray(file), new File(filesToShare), true);
        InetAddress localHost = InetAddress.getLocalHost();
        Client client = new Client(localHost, sharedTorrent);
        client.addObserver(new Observer() {
          @Override
          public void update(Observable observable, Object data) {
            Client client = (Client) observable;
            float progress = client.getTorrent().getCompletion();
            System.out.println(progress + "% file uploaded.");
          }
        });
        client.share();
        client.waitForCompletion();
      }
    } catch (IOException | NoSuchAlgorithmException exception) {
      throw new RuntimeException(exception);
    }
  }

  private static void validateArguments(String[] args) {
    if (args.length < 1) {
      System.out.println(
          "Missing required argument of path to folder where torrent files are kept. Please provide the same and try again.");
      System.exit(1);
    }
  }

}
