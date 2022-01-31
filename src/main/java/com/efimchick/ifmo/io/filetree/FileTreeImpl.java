package com.efimchick.ifmo.io.filetree;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FileTreeImpl implements FileTree {

    @Override
    public Optional<String> tree(Path path) {
        File file = new File(String.valueOf(path));
        if ( !file.exists()) return Optional.empty();
        if ( file.isFile()) {
            return Optional.of(file.getName() + " " + file.length() + " bytes");
        }
        if (file.isDirectory()) {
            System.out.println(directoryTree(file, new ArrayList<>()));
            return Optional.of(directoryTree(file, new ArrayList<>()));
        }
        return Optional.empty();
    }
    private String directoryTree(File folder, List<Boolean> lastFolders) {
        String directory = "";
        if (lastFolders.size() != 0)
            directory += (!(lastFolders.get(lastFolders.size() -1 )) ? "├─ " : "└─ ");
        directory += folder.getName() + " " + folderSize(folder);

        File[] files = folder.listFiles();
        int count = files.length;
        files = sortFiles(files);
        for (int i = 0; i < count; i++) {
            directory += "\n";
            for (Boolean lastFolder : lastFolders) {
                if (lastFolder) {
                    directory += "   ";
                } else {
                    directory += "│  ";
                }
            }
            if (files[i].isFile()) {
                directory += (i + 1 == count ? "└" : "├") + "─ " +
                        files[i].getName() + " " + files[i].length() + " bytes";
            } else {
                ArrayList<Boolean> list = new ArrayList<>(lastFolders);
                list.add(i+1 == count);
                directory += directoryTree(files[i], list);
            }
        }
        return directory;
    }
    private long getFolderSize(File folder) {
        long size = 0;
        File[] files = folder.listFiles();

        for (File file : files) {
            if (file.isFile()) {
                size += file.length();
            } else {
                size += getFolderSize(file);
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

        for (File value : folder) {
            if (value.isDirectory()) sorted.add(value);
        }

        for (File file : folder) {
            if (file.isFile()) sorted.add(file);
        }
        return sorted.toArray(new File[sorted.size()]);
    }
}
