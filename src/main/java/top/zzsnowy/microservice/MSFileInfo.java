package top.zzsnowy.microservice;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@Data
public class MSFileInfo {
    Map<String, String> map = new HashMap<>();

    public MSFileInfo(Sheet sheet) {
        int firstRowNum = sheet.getFirstRowNum();
        int lastRowNum = sheet.getLastRowNum();
        for(int i = firstRowNum + 1; i <= lastRowNum; i ++){
            Row row = sheet.getRow(i);
            if(row.getCell(1).toString().equals("FEIGN")){
                log.info("FEIGN类型跳过:" + row.getCell(0).toString());
                continue;
            }
            map.put(row.getCell(0).toString(), row.getCell(1).toString());
        }
    }
}
