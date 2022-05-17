package top.zzsnowy.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    public static List<String> readFileByLine(String fileName) throws IOException {
        List<String> list = new ArrayList<>();
        FileInputStream inputStream = new FileInputStream(fileName);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String str = bufferedReader.readLine();
        while (str != null) {
            //System.out.println(str);
            list.add(str);
            str = bufferedReader.readLine();
        }
        inputStream.close();
        bufferedReader.close();
        return list;
    }
}
