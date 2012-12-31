/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.royaldev.royalcommands.io.monitor;

import java.io.File;
import java.io.Serializable;

/**
 * {@link FileEntry} represents the state of a file or directory, capturing
 * the following {@link File} attributes at a point in time.
 * <ul>
 * <li>File Name (see {@link File#getName()})</li>
 * <li>Exists - whether the file exists or not (see {@link File#exists()})</li>
 * <li>Directory - whether the file is a directory or not (see {@link File#isDirectory()})</li>
 * <li>Last Modified Date/Time (see {@link File#lastModified()})</li>
 * <li>Length (see {@link File#length()}) - directories treated as zero</li>
 * <li>Children - contents of a directory (see {@link File#listFiles(java.io.FileFilter)})</li>
 * </ul>
 * <p/>
 * <h3>Custom Implementations</h3>
 * If the state of additional {@link File} attributes is required then create a custom
 * {@link FileEntry} with properties for those attributes. Override the
 * {@link #newChildInstance(File)} to return a new instance of the appropriate type.
 * You may also want to override the {@link #refresh(File)} method.
 *
 * @see FileAlterationObserver
 * @since 2.0
 */
public class FileEntry implements Serializable {

    static final FileEntry[] EMPTY_ENTRIES = new FileEntry[0];

    private final FileEntry parent;
    private FileEntry[] children;
    private final File file;
    private String name;
    private boolean exists;
    private boolean directory;
    private long lastModified;
    private long length;

    /**
     * Construct a new monitor for a specified {@link File}.
     *
     * @param parent The parent
     * @param file   The file being monitored
     */
    public FileEntry(FileEntry parent, File file) {
        if (file == null) {
            throw new IllegalArgumentException("File is missing");
        }
        this.file = file;
        this.parent = parent;
        this.name = file.getName();
    }

    /**
     * Refresh the attributes from the {@link File}, indicating
     * whether the file has changed.
     * <p/>
     * This implementation refreshes the <code>name</code>, <code>exists</code>,
     * <code>directory</code>, <code>lastModified</code> and <code>length</code>
     * properties.
     * <p/>
     * The <code>exists</code>, <code>directory</code>, <code>lastModified</code>
     * and <code>length</code> properties are compared for changes
     *
     * @param file the file instance to compare to
     * @return {@code true} if the file has changed, otherwise {@code false}
     */
    public boolean refresh(File file) {

        // cache original values
        boolean origExists = exists;
        long origLastModified = lastModified;
        boolean origDirectory = directory;
        long origLength = length;

        // refresh the values
        name = file.getName();
        exists = file.exists();
        directory = exists && file.isDirectory();
        lastModified = exists ? file.lastModified() : 0;
        length = exists && !directory ? file.length() : 0;

        // Return if there are changes
        return exists != origExists ||
                lastModified != origLastModified ||
                directory != origDirectory ||
                length != origLength;
    }

    /**
     * Create a new child instance.
     * <p/>
     * Custom implementations should override this method to return
     * a new instance of the appropriate type.
     *
     * @param file The child file
     * @return a new child instance
     */
    public FileEntry newChildInstance(File file) {
        return new FileEntry(this, file);
    }

    /**
     * Return the level
     *
     * @return the level
     */
    public int getLevel() {
        return parent == null ? 0 : parent.getLevel() + 1;
    }

    /**
     * Return the directory's files.
     *
     * @return This directory's files or an empty
     *         array if the file is not a directory or the
     *         directory is empty
     */
    public FileEntry[] getChildren() {
        return children != null ? children : EMPTY_ENTRIES;
    }

    /**
     * Set the directory's files.
     *
     * @param children This directory's files, may be null
     */
    public void setChildren(FileEntry[] children) {
        this.children = children;
    }

    /**
     * Return the file being monitored.
     *
     * @return the file being monitored
     */
    public File getFile() {
        return file;
    }

    /**
     * Return the file name.
     *
     * @return the file name
     */
    public String getName() {
        return name;
    }

    /**
     * Indicate whether the file existed the last time it
     * was checked.
     *
     * @return whether the file existed
     */
    public boolean isExists() {
        return exists;
    }

    /**
     * Indicate whether the file is a directory or not.
     *
     * @return whether the file is a directory or not
     */
    public boolean isDirectory() {
        return directory;
    }
}
