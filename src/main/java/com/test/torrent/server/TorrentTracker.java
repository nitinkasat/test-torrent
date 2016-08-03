package com.test.torrent.server;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.NoSuchAlgorithmException;

import com.turn.ttorrent.tracker.TrackedTorrent;
import com.turn.ttorrent.tracker.Tracker;

public class TorrentTracker {

  public static void main(String[] args) {
    validateArguments(args);
    String torrentFolderDirectory = args[0];
    startTracker(torrentFolderDirectory);
  }

  private static void startTracker(String torrentFolderDirectory) {
    Tracker tracker = null;
    try {
      tracker = new Tracker(new InetSocketAddress(6969));
      FilenameFilter filter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
          return name.endsWith(".torrent");
        }
      };
      for (File file : new File(torrentFolderDirectory).listFiles(filter)) {
        TrackedTorrent trackedTorrent = TrackedTorrent.load(file);
        tracker.announce(trackedTorrent);
      }
      tracker.start();
    } catch (IOException | NoSuchAlgorithmException exception) {
      if (tracker != null) {
        tracker.stop();
      }
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
