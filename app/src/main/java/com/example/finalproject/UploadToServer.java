package com.example.finalproject;

/**
 * Created by pawan on 7/1/17.
 */


import java.net.Socket;
import java.net.URL;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class UploadToServer {
//    static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    public static boolean serverListening(String host, int port){
        Socket s = null;
        try{
            s = new Socket(host, port);
            return true;
        }
        catch (Exception e){
            return false;
        }
        finally{
            if(s != null)
                try {s.close();}
                catch(Exception e){}
        }
    }

    public static String uploadToServer(String file) throws IOException{
        String fileName = file;
        int serverResponseCode = 0;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(fileName);

        try {
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL url = new URL("http://192.168.57.1/UploadToServer.php");

            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("uploaded_file", fileName);

            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
                    + fileName + "\"" + lineEnd);

            dos.writeBytes(lineEnd);

            // create a buffer of  maximum size
            bytesAvailable = fileInputStream.available();

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {

                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            if(serverResponseCode == 200){
                System.out.println("server ok");
            }

            //close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();

        }
        catch (MalformedURLException ex) {
            System.out.println("Chck url");
            ex.printStackTrace();
        }
        catch (Exception e) {
            System.out.println("exception");

            e.printStackTrace();

        }
        System.out.println(serverResponseCode);
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            total.append(line).append('\n');
        }
        System.out.println("uploadFile Server Response is: " + total.toString());
        String [] result = total.toString().split(":");
        System.out.println(result[1]);

        return result[1];
    }
    public static void writeDataToFile(String fileName, double x, double y, double z, String gesture) throws IOException {
        FileWriter writer = null;

        try{
            writer = new FileWriter(new File(fileName), true);
            writer.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
            writer.append(',');
            writer.append(String.valueOf(x));
            writer.append(',');
            writer.append(String.valueOf(y));
            writer.append(',');
            writer.append(String.valueOf(z));

            if(!gesture.equals("0")) {
                writer.append(',');
                writer.append(gesture);
            }
            writer.append('\n');

            System.out.println("CSV file is created...");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            writer.flush();
            writer.close();
        }
    }
}
