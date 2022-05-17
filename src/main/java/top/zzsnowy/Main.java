package top.zzsnowy;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import top.zzsnowy.microservice.InternalAllCall;
import top.zzsnowy.microservice.InternalMethodCall;
import top.zzsnowy.microservice.MSCall;
import top.zzsnowy.microservice.MSFileInfo;
import top.zzsnowy.util.ExcelUtil;
import top.zzsnowy.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
public class Main {
    public static void main(String[] args) throws IOException, InvalidFormatException {
        String projectPath = "C:\\Users\\zhao\\Desktop\\F\\aaZ\\MSA\\xx\\依赖数据\\Springcloud";
        String outPath = "C:\\Users\\zhao\\Desktop\\F\\aaZ\\MSA\\xx\\依赖矩阵\\Springcloud";
        int versionNum = 23;
        String msListPath = "src/main/resources/Springcloud";
        List<String> msList = FileUtil.readFileByLine(msListPath);
        for(int i = 3; i <= versionNum; i ++){
            String excelPath = projectPath + "\\v" + i + "服务内部文件信息.xlsx";
            File excel = new File(excelPath);
            Workbook wb = new XSSFWorkbook(excel);
            Sheet sheet;
            for(int j = 0; j < msList.size(); j ++){
                String msName = msList.get(j);
                if(msName.length() > 31){
                    msName = msName.substring(0, 31);
                }
                sheet = wb.getSheet(msName);
                if(sheet == null){
                    log.info("该微服务的信息不存在：" + msName);
                    continue;
                }
                MSFileInfo msFileInfo = new MSFileInfo(sheet);
                //printInternalMethodCall(projectPath, i, msName, msFileInfo, outPath);
                //printInternalAllCall(projectPath, i, msName, msFileInfo, outPath);
                printMSCall(projectPath, i, msList, outPath);
            }
        }
    }

    private static void printInternalMethodCall(String projectPath, int version, String ms, MSFileInfo msFileInfo, String outPath) throws IOException, InvalidFormatException {
        String excelPath = projectPath + "\\v" + version + "服务内部纯方法调用依赖关系.xlsx";
        File excel = new File(excelPath);
        Workbook wb = new XSSFWorkbook(excel);
        Sheet sheet = wb.getSheet(ms);
        InternalMethodCall internalMethodCall = new InternalMethodCall(sheet, msFileInfo);
        outPath = outPath + "\\服务内部纯方法依赖矩阵\\v" + version + sheet.getSheetName();
        ExcelUtil.toMatrix(internalMethodCall, outPath);
    }

    private static void printInternalAllCall(String projectPath, int version, String ms, MSFileInfo msFileInfo, String outPath) throws IOException, InvalidFormatException {
        String excelPath = projectPath + "\\v" + version + "服务内部全部调用依赖关系.xlsx";
        File excel = new File(excelPath);
        Workbook wb = new XSSFWorkbook(excel);
        Sheet sheet = wb.getSheet(ms);
        InternalAllCall internalAllCall = new InternalAllCall(sheet, msFileInfo);
        outPath = outPath + "\\服务内部全部依赖矩阵\\v" + version + sheet.getSheetName();
        ExcelUtil.toMatrix(internalAllCall, outPath);
    }

    private static void printMSCall(String projectPath, int version, List<String> msList, String outPath) throws IOException, InvalidFormatException {
        String excelPath = projectPath + "\\v" + version + "服务间调用依赖关系.xlsx";
        File excel = new File(excelPath);
        Workbook wb = new XSSFWorkbook(excel);
        Sheet sheet = wb.getSheet("data");
        String fileInfoPath = projectPath + "\\v" + version + "服务内部文件信息.xlsx";
        File fileInfoExcel = new File(fileInfoPath);
        Workbook fileInfoWb = new XSSFWorkbook(fileInfoExcel);
        MSCall msCall = new MSCall(sheet, fileInfoWb, msList);
        outPath = outPath + "\\服务间依赖矩阵(包含服务内中介调用)\\v" + version + sheet.getSheetName();
        ExcelUtil.toMatrix(msCall, outPath);
    }
}
