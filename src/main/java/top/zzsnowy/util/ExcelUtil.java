package top.zzsnowy.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import top.zzsnowy.microservice.Call;
import top.zzsnowy.model.Node;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
@Slf4j
public class ExcelUtil {

    public static void toMatrix(Call call, String outPath) throws IOException {
        Map<Node, Integer> api2Map = call.getAPI2Map();
        Map<Node, Integer> cell2Map = call.getCell2Map();
        Map<Node, Integer> apiCellMap = call.getAPICellMap();
        Map<Node, Integer> sameMsAPI2Map = call.getSameMSAPI2Map();
        if(!api2Map.isEmpty()){
            //writeMatrix(api2Map, outPath + "接口间依赖.xlsx");
        }
        if(!cell2Map.isEmpty()){
            //writeMatrix(cell2Map, outPath + "实体类间依赖.xlsx");
        }
        if(!apiCellMap.isEmpty()){
            //writeMatrix(apiCellMap, outPath + "接口与实体类间依赖.xlsx");
        }
        if(!sameMsAPI2Map.isEmpty()){
            writeMatrix(sameMsAPI2Map, outPath + "服务内通过中介的接口间依赖.xlsx");
        }
    }

    private static void writeMatrix(Map<Node, Integer> map, String outPath) throws IOException {
        Set<String> sourceSet = new HashSet<>();
        Set<String> targetSet = new HashSet<>();
        for (Map.Entry<Node, Integer> entry : map.entrySet()) {
            sourceSet.add(entry.getKey().getSource());
            targetSet.add(entry.getKey().getTarget());
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
        for (Map.Entry<Node, Integer> entry : map.entrySet()) {
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
                log.error("依赖无法输出到矩阵：" + entry.getKey().getSource() + "," + entry.getKey().getTarget());
            }
        }
        File file = new File(outPath);
        FileOutputStream out = new FileOutputStream(file);  //创建输出流
        wbMatrix.write(out);     //写入到指定的文件
        wbMatrix.close(); // 关闭
        out.close();
    }
}
