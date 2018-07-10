package cn.seu.edu.LANComm.dsp;

import java.io.*;

/**
 * Created by Administrator on 2018/2/25.
 */
public class ReadDataFile {
    public static void main(String[] args) throws FileNotFoundException, IOException{
        String filePath = "F:\\seuLANComm\\sinData.txt";
        File file = new File(filePath);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String[] dataString = reader.readLine().split(" ");
        float[] data = new float[dataString.length];
        for (int i = 0; i < data.length; i++) {
            data[i] = Float.parseFloat(dataString[i]);
            System.out.print(data[i] + " ");
        }
        System.out.println();
        System.out.println(data.length);

    }
}
