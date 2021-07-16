package cz.mzk.fofola.service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.stream.Collectors;

public class FileService {

    public static final String OUTPUTS = "outputs/";
    public static final String logDirPath = OUTPUTS + "logs";
    private static final String pdfOutDirPath = OUTPUTS + "pdf_out";
    private static final String solrRespOutDirPath = OUTPUTS + "solr_out";
    private static final String checkDonatorOutDirPath = OUTPUTS + "check_donator_out";
    private static final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss.sss");

    private static File createDirIfDoesntExist(final String dirPath) {
        final File directory = new File(dirPath);
        if (!directory.exists())
            directory.mkdirs();
        return directory;
    }

    public static String fileNameWithDateStampPrefix(final String fileName) {
        return format.format(new Date()) + "-" + fileName;
    }

    public static FileHandler getLogFileHandler(final String fileName) throws IOException {
        createDirIfDoesntExist(logDirPath);
        return new FileHandler(path(logDirPath, fileName));
    }

    public static List<String> getCheckDonatorOutputFileNames() {
        return getOutputFileNames(checkDonatorOutDirPath);
    }

    public static List<String> getSolrRespOutputFileNames() {
        return getOutputFileNames(solrRespOutDirPath);
    }

    public static File getSolrRespOutputFile(final String fileName) {
        return getFile(solrRespOutDirPath, fileName);
    }

    public static File getCheckDonatorOutputFile(final String fileName) {
        return getFile(checkDonatorOutDirPath, fileName);
    }

    public static String getLogFilePath(final String filename) {
        return path(logDirPath, filename);
    }

    public static String getPDFOutFilePath(final String filename) {
        return path(pdfOutDirPath, filename);
    }

    public static File getPDFOutputFile(final String fileName) {
        return getFile(pdfOutDirPath, fileName);
    }

    public static void removeCheckDonatorOutputFile(final String fileName) {
        getFile(checkDonatorOutDirPath, fileName).delete();
    }

    public static void removePDFOutputFile(final String fileName) {
        getFile(pdfOutDirPath, fileName).delete();
    }

    public static void removeSolrRespOutputFile(final String fileName) {
        getFile(solrRespOutDirPath, fileName).delete();
    }

    private static File getFile(final String path, final String fileName) {
        return new File(path(path, fileName));
    }

    private static List<String> getOutputFileNames(final String path) {
        final File directory = createDirIfDoesntExist(path);
        final File[] files = directory.listFiles();
        if (files == null)
            return new ArrayList<>();
        Arrays.sort(files, Comparator.comparingLong(File::lastModified));
        return Arrays.stream(files).map(File::getName).collect(Collectors.toList());
    }

    private static String path(final String... parts) {
        return String.join("/", parts);
    }
}
