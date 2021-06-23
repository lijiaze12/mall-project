package com.changgou.contrl;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class  Picccc{

    public static void main(String[] args) {
        String inFile = "http://image.haoke.com/images/2021/06/15/O1CN01q62QLW1xJKDCnTnHc_!!2210604626422.jpg";
        String outFile = "http://image.haoke.com/images/2021/06/16/O1CN01q62QLW1xJKDCnTnHc_!!2210604626422.jpg";
        FileInputStream input = null;
        FileOutputStream out = null;
        try {
            input = new FileInputStream(inFile);
            out = new FileOutputStream(outFile);
            byte[] b = new byte[input.available()];
            int len ;
            while((len = input.read(b)) != -1) {
                out.write(b, 0, len);
            }
            input.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}