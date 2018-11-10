package com.gil.smsexporter;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Utils {
    public static String SaveFileToDocsDirectory(String filename, String content) throws IOException {
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File myFile = new File(folder, filename);
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(myFile), UTF_8);
        writer.write(content);
        writer.close();

        return myFile.getPath();
    }
}
