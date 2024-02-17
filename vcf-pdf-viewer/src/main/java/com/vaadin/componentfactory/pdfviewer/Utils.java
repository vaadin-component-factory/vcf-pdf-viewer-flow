/*-
 * #%L
 * Pdf Viewer
 * %%
 * Copyright (C) 2021 Vaadin Ltd
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.vaadin.componentfactory.pdfviewer;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static sun.font.FontUtilities.isWindows;

public class Utils {

    /**
     * If the provided resource cannot be found it also checks these directories: <br>
     * - workingDir <br>
     * - workingDir + "/src/main/java" <br>
     * - workingDir + "/src/test/java" <br>
     *
     * @param path expected relative path to a file inside the current jar. Example: help.txt or /help.txt
     */
    public static InputStream getResource(String path) throws IOException {
        String fullPath = (path.startsWith("/") ? path : "/" + path);
        InputStream in = Utils.class.getResourceAsStream(fullPath);
        if (in != null) return in;
        String p1 = System.getProperty("user.dir") + fullPath;
        File f = new File(p1);
        if (f.exists()) return Files.newInputStream(f.toPath());
        String p2 = System.getProperty("user.dir") + "/src/main/java" + fullPath;
        f = new File(p2);
        if (f.exists()) return Files.newInputStream(f.toPath());
        String p3 = System.getProperty("user.dir") + "/src/test/java" + fullPath;
        f = new File(p3); // Support JUnit tests
        if (f.exists()) return Files.newInputStream(f.toPath());
        String classpath = System.getProperty("java.class.path");
        Exception e = null;
        try {
            if (isWindows) fullPath = fullPath.replace("/", "\\");
            String[] dirs = isWindows ? classpath.split(";") : classpath.split(":");
            for (String _dir : dirs) {
                File dir = new File(_dir);
                Iterator<File> it = FileUtils.iterateFiles(dir, null, true);
                while (it.hasNext()) {
                    f = it.next();
                    if (f.isFile() && f.getAbsolutePath().endsWith(".jar")) {
                        try (ZipFile zipFile = new ZipFile(f)) {
                            Enumeration<? extends ZipEntry> entries = zipFile.entries();
                            while (entries.hasMoreElements()) {
                                ZipEntry entry = entries.nextElement();
                                if (!entry.isDirectory() && path.endsWith(entry.getName())) {
                                    return zipFile.getInputStream(entry);
                                }
                            }
                        }
                    } else {
                        if (f.isFile() && f.getAbsolutePath().endsWith(fullPath)) {
                            return Files.newInputStream(f.toPath());
                        }
                    }
                }
            }
        } catch (Exception e1) {
            e = e1;
        }
        System.err.println("Failed to find resource \"" + fullPath + "\", searched: " + p1 + " and " + p2 + " and " + p3 + " and class paths recursively: " + classpath + ".");
        e.printStackTrace();
        return null;
    }

}
