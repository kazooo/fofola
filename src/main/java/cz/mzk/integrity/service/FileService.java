package cz.mzk.integrity.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileService {

    public static void writeUuidsIntoFile(String filename, List<String> uuids) throws IOException {
        createDirIfNotExist(filename);
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for (String uuid : uuids) {
            writer.write(uuid + "\n");
        }
        writer.close();
    }

    private static void createDirIfNotExist(String filename) {
        File file = new File(filename);
        String directoryName = file.getParent();
        File directory = new File(directoryName);
        if (!directory.exists()){
            directory.mkdir();
        }
    }

    public static ResponseEntity sendFile(String pathToFile) throws FileNotFoundException {
        File file = new File(pathToFile);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    public static void deleteDir(String pathToDir) {
        FileSystemUtils.deleteRecursively(new File(pathToDir));
    }

    public static void zipFolder(String pathToFolder, String outZipName) throws IOException {
        FileOutputStream fos = new FileOutputStream(outZipName);
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        File folderToZip = new File(pathToFolder);

        zipFile(folderToZip, folderToZip.getName(), zipOut);
        zipOut.close();
        fos.close();
    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }

        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }

        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;

        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }
}
