package cn.seu.edu.LANComm.simulator;

import java.io.*;

public class ReadDataFile {

    public static float[] readData(String filePath) {

        try(BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)))) {
            String[] dataString = reader.readLine().split(" ");
            float[] data = new float[dataString.length];
            for(int i = 0; i < dataString.length; i++) {
                data[i] = Float.parseFloat(dataString[i]);
            }
            return data;

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String filePath = "C:\\Users\\Administrator\\Desktop\\TxHost\\TxHost\\src\\data\\sinData.txt";
        float[] data = ReadDataFile.readData(filePath);
        show(data);

    }

    private static void show(float[] data) {
        for(int i = 0; i < data.length; i++) {
            if(i != data.length - 1) {
                System.out.print(data[i] + " ");
            } else {
                System.out.print(data[i]);
            }
        }
        System.out.println();
        System.out.println(data.length);
    }

}

