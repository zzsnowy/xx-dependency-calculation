package top.zzsnowy.demo;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author zzsnowy
 * @date 2022/4/19
 * 获取每个版本的不同微服务间的接口调用矩阵
 */
public class M2MIntefaceCall {
    private static class VMFNode{
        String source;
        String target;
        String version;
        public VMFNode(String source, String target, String version) {
            this.source = source;
            this.target = target;
            this.version = version;
        }
        public String getSource() {
            return source;
        }
        public String getTarget() {
            return target;
        }
        public String getVersion() {
            return version;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            VMFNode node = (VMFNode) o;
            return Objects.equals(source, node.source) && Objects.equals(target, node.target) && Objects.equals(version, node.version);
        }

        @Override
        public int hashCode() {
            return Objects.hash(source, target, version);
        }
    }
    public static void main(String[] args) throws IOException, InvalidFormatException {
        //数据读取，修改dataPath读入不同的依赖文件。
        String dataPath = "C:\\Users\\zhao\\Desktop\\F\\aaZ\\MSA\\xx\\开源微服务项目数据\\开源微服务项目数据\\4-6\\04-依赖数据\\mall-swarm依赖详细信息.xlsx";
        File excel = new File(dataPath);
        Workbook wb = new XSSFWorkbook(excel);
        Sheet sheet = wb.getSheetAt(0);     //读取sheet 0
        //获取到每个版本中不同微服务间的每种接口调用的次数，放入map中
        Map<VMFNode, Integer> map = new HashMap<>();
        getMallSwarmMatrix(sheet, map);
        for (Map.Entry<VMFNode, Integer> entry : map.entrySet()) {
            //System.out.println("Key = " + entry.getKey().getSource() + "," + entry.getKey().getTarget() + "," +entry.getKey().getVersion() + " ; Value = " + entry.getValue());
        }

    }

    private static void getMallSwarmMatrix(Sheet sheet, Map<VMFNode, Integer> map) throws IOException {
        int firstRowIndex = 1;   //第一行是列名，所以不读
        int lastRowIndex = sheet.getLastRowNum();
        for(int rIndex = firstRowIndex ; rIndex <= lastRowIndex; rIndex++) {   //遍历行
            Row row = sheet.getRow(rIndex);
            //获取到不同微服务的调用方名字以及被调用方名字
            String sourceFile, targetFile;
            String[] sourceFilePath,targetFilePath;
            String sourceMFile, targetMFile;
            sourceFilePath = row.getCell(5).toString().split("\\.");
            targetFilePath = row.getCell(7).toString().split("\\.");
            sourceFile = sourceFilePath[5];
            targetFile = targetFilePath[5];
            if(sourceFile.equals("impl")){
                sourceFile = sourceFilePath[6];
            }
            //补充上targetFile排除的条件
            if(targetFile.equals("getAccessToken(java")){
                targetFile = targetFilePath[6];
            }
            sourceMFile = row.getCell(4).toString() + "." + sourceFile;
            //补充targetMFile的名字
            targetMFile = row.getCell(6).toString() + "." + targetFile;
            //获取到版本号以及每种调用的次数，并放入map中
            String version = row.getCell(2).toString();
            VMFNode node = new VMFNode(sourceMFile, targetMFile, version);
            if(map.containsKey(node)){
                map.put(node, map.get(node) + 1);
            }else{
                map.put(node, 1);
            }
        }
        //输出到矩阵中,i的范围要随项目而改。
        for(int i = 17; i <= 27; i ++){
            //首先获得行和列各有哪些MFile
            Set<String> sourceSet = new HashSet<>();
            Set<String> targetSet = new HashSet<>();
            for (Map.Entry<VMFNode, Integer> entry : map.entrySet()) {
                if(entry.getKey().getVersion().equals(Integer.toString(i))){
                sourceSet.add(entry.getKey().getSource());
                targetSet.add(entry.getKey().getTarget());
                }
            }
            XSSFWorkbook wbMatrix = new XSSFWorkbook();
            XSSFSheet sheetMatrix = wbMatrix.createSheet();
            Row rowMatrix = sheetMatrix.createRow(0);
            int tnt = 1;
            for(String value: sourceSet){
                Cell sourceCell = rowMatrix.createCell(tnt);
                sourceCell.setCellValue(value);
                tnt ++;
            }
            int cnt = 1;
            for(String value: targetSet){
                Cell targetCell = sheetMatrix.createRow(cnt).createCell(0);
                targetCell.setCellValue(value);
                cnt ++;
            }
            //将调用次数填到矩阵中
            for (Map.Entry<VMFNode, Integer> entry : map.entrySet()) {
                if(!entry.getKey().getVersion().equals(Integer.toString(i))){
                    continue;
                }
                int flag = 0;
                for(int j = 1; j < tnt; j ++){
                    for(int k = 1; k < cnt; k ++){
                        if(rowMatrix.getCell(j).toString().equals(entry.getKey().getSource())
                        && sheetMatrix.getRow(k).getCell(0).toString().equals(entry.getKey().getTarget())){
                            Cell tmp = sheetMatrix.getRow(k).createCell(j);
                            tmp.setCellValue(entry.getValue());
                            flag = 1;
                        }
                    }
                }
                if(flag == 0){
                    System.out.println("Key = " + entry.getKey().getSource() + "," + entry.getKey().getTarget() + "," +entry.getKey().getVersion() + " ; Value = " + entry.getValue());
                }
            }
            String outPath = "C:\\Users\\zhao\\Desktop\\F\\aaZ\\MSA\\xx\\微服务间接口调用\\mall-swarm\\"
                    + "v" + i + "接口调用.xlsx";
            File file = new File(outPath);
            FileOutputStream out = new FileOutputStream(file);  //创建输出流
            wbMatrix.write(out);     //写入到指定的文件
            wbMatrix.close(); // 关闭
            out.close();
        }
    }
}
