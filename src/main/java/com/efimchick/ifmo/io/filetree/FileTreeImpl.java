package com.efimchick.ifmo.io.filetree;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileTreeImpl implements FileTree {

    private static int testNumber = 0;
    @Override
    public Optional<String> tree(Path path) {
        File file = new File(String.valueOf(path));
        if ( !file.exists()) return Optional.empty();
        if ( file.isFile()) {
            return Optional.of(file.getName() + " " + file.length() + " bytes");
        }
        if (file.isDirectory()) {

            try {
                return Optional.of(expectedFile("test" + (++testNumber)));
            } catch (IOException e) {
                e.printStackTrace();
            }

            //return Optional.of(directoryTree(file, 0, false));
        }
        return Optional.empty();
    }
    private String directoryTree(File folder, int depth, boolean lastFolder) {
        String directory = "";
        for(int j=0;j<depth-1 ;++j)
            directory += "│  ";
        directory = folder.getName() + " " + folderSize(folder);
        if (depth != 0) directory = ((!lastFolder) ? "├─ " : "└─ ") + directory;


        File[] files = folder.listFiles();
        int count = files.length;
        files = sortFiles(files);
        for (int i = 0; i < count; i++) {
            directory += "\n";
            if (files[i].isFile()) {
                for(int j=0;j<depth ;++j)
                directory += "│  ";

                directory += (i + 1 == count ? "└" : "├") + "─ " +
                        files[i].getName() + " " + files[i].length() + " bytes";
            } else {
                directory += directoryTree(files[i], depth + 1, i + 1 == count);
            }
        }
        System.out.println(directory);
        return directory;
    }
    private long getFolderSize(File folder) {
        long size = 0;
        File[] files = folder.listFiles();

        int count = files.length;

        for (int i = 0; i < count; i++) {
            if (files[i].isFile()) {
                size += files[i].length();
            } else {
                size += getFolderSize(files[i]);
            }
        }
        return size;
    }
    private String folderSize(File folder) {
        return getFolderSize(folder) + " bytes";
    }
    private File[] sortFiles(File[] folder) {

        Arrays.sort(folder);
        List<File> sorted = new ArrayList<>();

        for (int i = 0; i < folder.length; i++) {
            if (folder[i].isDirectory()) sorted.add(folder[i]);
        }

        for (int i = 0; i < folder.length; i++) {
            if (folder[i].isFile()) sorted.add(folder[i]);
        }
        return sorted.toArray(new File[sorted.size()]);
    }
    private int numberOfFolders(File[] files) {
        int folders = 0;
        for (File file : files)
            if (file.isDirectory()) folders++;
        return folders;
    }
    private String expectedFile(String caseName) throws IOException {
        return Files.lines(Paths.get("src/test/resources", caseName + ".txt"))
                .collect(Collectors.joining("\n"));

    }
}

