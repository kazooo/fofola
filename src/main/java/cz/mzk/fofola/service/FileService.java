package cz.mzk.fofola.service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.stream.Collectors;


public class FileService {

    public static final String logDirPath = "logs";
    private static final String pdfOutDirPath = "pdf_out";
    private static final String solrRespOutDirPath = "solr_out";
    private static final String checkDonatorOutDirPath = "check_donator_out";
    private static final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy-HH:mm");

    private static File createDirIfDoesntExist(String dirPath) {
        File directory = new File(dirPath);
        if (!directory.exists())
            directory.mkdirs();
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
        return getOutputFileNames(checkDonatorOutDirPath);
    }

    public static List<String> getSolrRespOutputFileNames() {
        return getOutputFileNames(solrRespOutDirPath);
    }

    public static void removeCheckDonatorOutputFile(String fileName) {
        getFile(checkDonatorOutDirPath, fileName).delete();
    }

    public static File getCheckDonatorOutputFile(String fileName) {
        return getFile(checkDonatorOutDirPath, fileName);
    }

    public static String getPDFOutFilePath(String filename) {
        createDirIfDoesntExist(pdfOutDirPath);
        return getFilePath(pdfOutDirPath, filename);
    }

    public static File getPDFOutputFile(String fileName) {
        return getFile(pdfOutDirPath, fileName);
    }

    public static void removePDFOutputFile(String fileName) {
        getFile(pdfOutDirPath, fileName).delete();
    }

    public static File getSolrRespOutputFile(String fileName) {
        return getFile(solrRespOutDirPath, fileName);
    }

    public static void removeSolrRespOutputFile(String fileName) {
        getFile(solrRespOutDirPath, fileName).delete();
    }

    private static File getFile(String path, String fileName) {
        return new File(path + "/" + fileName);
    }

    private static String getFilePath(String path, String fileName) {
        return path + "/" + fileName;
    }

    public static List<String> getOutputFileNames(String path) {
        File directory = createDirIfDoesntExist(path);
        File[] files = directory.listFiles();
        if (files == null)
            return new ArrayList<>();
        Arrays.sort(files, Comparator.comparingLong(File::lastModified));
        return Arrays.stream(files).map(File::getName).collect(Collectors.toList());
    }
}
