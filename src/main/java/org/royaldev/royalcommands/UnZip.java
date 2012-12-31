package org.royaldev.royalcommands;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class UnZip {
    static final int BUFFER = 2048;

    /**
     * Decompresses a zipped file - respects directories
     *
     * @param fileName          File name of the zipped file
     * @param destinationFolder Folder to unzip in
     */
    public static void decompress(String fileName, String destinationFolder) {
        try {
            BufferedOutputStream dest;
            BufferedInputStream is;
            ZipEntry entry;
            ZipFile zipfile = new ZipFile(fileName);
            Enumeration e = zipfile.entries();
            while (e.hasMoreElements()) {
                entry = (ZipEntry) e.nextElement();
                if (entry.isDirectory()) {
                    new File(destinationFolder + File.separator + entry.getName()).mkdir();
                    continue;
                }
                is = new BufferedInputStream(zipfile.getInputStream(entry));
                int count;
                byte data[] = new byte[BUFFER];
                File f = new File(destinationFolder + File.separator + entry.getName());
                if (!f.exists()) f.getParentFile().mkdirs();
                FileOutputStream fos = new FileOutputStream(destinationFolder + File.separator + entry.getName());
                dest = new BufferedOutputStream(fos, BUFFER);
                while ((count = is.read(data, 0, BUFFER)) != -1)
                    dest.write(data, 0, count);
                dest.flush();
                dest.close();
                is.close();
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }
}
