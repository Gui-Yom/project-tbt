package lorganisation.projecttbt.experiments;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.*;
import java.util.Collections;
import java.util.Iterator;

public class AssetsListing {

    public static void main(String[] args) {

        try {

            URI uri = AssetsListing.class.getClassLoader().getResource("assets").toURI();
            URI uri2 = AssetsListing.class.getProtectionDomain().getCodeSource().getLocation().toURI();
            System.out.println(uri);
            System.out.println(uri2);
            URI uri3 = new URL("jar:" + uri2 + "!/").toURI();
            System.out.println(uri3);

            try (FileSystem fs = getFileSystem(uri3)) {

                PathMatcher matcher = fs.getPathMatcher("glob:**.json");

                for (Iterator<Path> it = Files.walk(fs.getPath("assets"), 1).iterator(); it.hasNext(); ) {

                    Path path = it.next();

                    if (matcher.matches(path))
                        System.out.println(path);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static FileSystem getFileSystem(URI uri) throws IOException {

        try {
            return FileSystems.getFileSystem(uri);
        } catch (FileSystemNotFoundException e) {
            return FileSystems.newFileSystem(uri, Collections.<String, String>emptyMap());
        }
    }
}
