package cz.mzk.fofola.process.utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.stream.Collectors;

public class FileUtils {

    public static final String logDirPath = "logs/";
    private static final String checkDonatorOutDirPath = "check_donator_out/";
    private static final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy-HH:mm");

    private static File createDirIfDoesntExist(String dirPath) {
        File directory = new File(dirPath);
        if (!directory.exists()){
            directory.mkdirs();
        }
        return directory;
    }

    public static String fileNameWithDateStampPrefix(String fileName) {
        return format.format(new Date()) + "-" + fileName;
    }

    public static FileHandler getLogFileHandler(String fileName) throws IOException {
        createDirIfDoesntExist(logDirPath);
        return new FileHandler(logDirPath + fileName);
    }

    public static File getCheckDonatorOutFile(String fileName) throws IOException {
        createDirIfDoesntExist(checkDonatorOutDirPath);
        File file = new File(checkDonatorOutDirPath + fileName);
        file.createNewFile();
        return file;
    }

    public static List<String> getCheckDonatorOutputFileNames() {
        File directory = createDirIfDoesntExist(checkDonatorOutDirPath);
        File[] files = directory.listFiles();
        if (files == null) return new ArrayList<>();
        Arrays.sort(files, Comparator.comparingLong(File::lastModified));
        return Arrays.stream(files).map(File::getName).collect(Collectors.toList());
    }

    public static void removeCheckDonatorOutputFile(String fileName) {
        File file = new File(checkDonatorOutDirPath + fileName);
        file.delete();
    }

    public static File getCheckDonatorOutputFile(String fileName) {
        return new File(checkDonatorOutDirPath + fileName);
    }
}
