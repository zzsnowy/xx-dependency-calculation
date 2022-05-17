package top.zzsnowy.microservice;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import top.zzsnowy.model.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Data
public class MSCall extends Call{

    public MSCall(Sheet sheet, Workbook fileInfoWb, List<String> msList) {
        int firstRowNum = sheet.getFirstRowNum();
        int lastRowNum = sheet.getLastRowNum();
        Map<String, String> map1, map2;
        for(int i = firstRowNum + 1; i <= lastRowNum; i ++){
            Row row = sheet.getRow(i);
            String sourceMS = row.getCell(0).toString();
            String targetMS = row.getCell(2).toString();
            String source = row.getCell(1).toString();
            String target = row.getCell(3).toString();
            String weight = row.getCell(4).toString();
            if(!msList.contains(sourceMS) || !msList.contains(targetMS)){
                log.info("计算服务间调用，不属于考虑范围的服务跳过:" + sourceMS + "->" + targetMS);;
                continue;
            }
            if(sourceMS.length() > 31){
                sourceMS = sourceMS.substring(0, 31);
            }
            if(targetMS.length() > 31){
                targetMS = targetMS.substring(0, 31);
            }
            map1 = new MSFileInfo(fileInfoWb.getSheet(sourceMS)).getMap();
            map2 = new MSFileInfo(fileInfoWb.getSheet(targetMS)).getMap();
            if(!map1.containsKey(source) || !map2.containsKey(target)){
                log.info("计算服务间调用，内部信息map中不存在的FEIGN类型跳过:" + source + " ->" + target);
                continue;
            }
            if(sourceMS.equals(targetMS)){
                SameMSAPI2Map.put(new Node(source, target), Integer.valueOf(weight));
                continue;
            }
            if(map1.get(source).equals("API") && map2.get(target).equals("API")){
                API2Map.put(new Node(source, target), Integer.valueOf(weight));
                continue;
            }
            if(map1.get(source).equals("CELL") && map2.get(target).equals("CELL")){
                Cell2Map.put(new Node(source, target), Integer.valueOf(weight));
                continue;
            }
            APICellMap.put(new Node(source, target), Integer.valueOf(weight));
        }
    }
}
