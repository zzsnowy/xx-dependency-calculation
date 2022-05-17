package top.zzsnowy.microservice;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import top.zzsnowy.model.Node;

import java.util.Map;

@Slf4j
@Data
public class InternalAllCall extends Call{

    public InternalAllCall(Sheet sheet, MSFileInfo msFileInfo) {
        int firstRowNum = sheet.getFirstRowNum();
        int lastRowNum = sheet.getLastRowNum();
        Map<String, String> map = msFileInfo.getMap();
        for(int i = firstRowNum + 1; i <= lastRowNum; i ++){
            Row row = sheet.getRow(i);
            String source = row.getCell(0).toString();
            String target = row.getCell(1).toString();
            String weight = row.getCell(3).toString();
            if(!map.containsKey(source) || !map.containsKey(target)){
                log.info("计算内部全部调用，内部信息map中不存在的FEIGN类型跳过:" + source +
                        "->" + target);
                continue;
            }
            if(map.get(source).equals("API") && map.get(target).equals("API")){
                API2Map.put(new Node(source, target), Integer.valueOf(weight));
                continue;
            }
            if(map.get(source).equals("CELL") && map.get(target).equals("CELL")){
                Cell2Map.put(new Node(source, target), Integer.valueOf(weight));
                continue;
            }
            APICellMap.put(new Node(source, target), Integer.valueOf(weight));
        }
    }
}
