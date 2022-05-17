package top.zzsnowy.demo;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
/**
 * @author zzsnowy
 * @date 2022/4/14
 */
public class Micro2Interface2 {
    private static class Node{
        String source;
        String target;
        String version;
        public Node(String source, String target, String version) {
            this.source = source;
            this.target = target;
            this.version = version;
        }
        public Node(String source, String target) {
            this.source = source;
            this.target = target;
        }
        public Node(String target) {
            this.target = target;
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
            Node node = (Node) o;
            return Objects.equals(source, node.source) && Objects.equals(target, node.target) && Objects.equals(version, node.version);
        }

        @Override
        public int hashCode() {
            return Objects.hash(source, target, version);
        }
    }
    public static void main(String[] args) throws IOException, InvalidFormatException {
        //数据读取，修改dataPath读入不同的依赖文件。
        String dataPath = "C:\\Users\\zhao\\Desktop\\F\\aaZ\\MSA\\xx\\开源微服务项目数据\\开源微服务项目数据\\4-6\\04-依赖数据\\wanxin-p2p依赖详细信息.xlsx";
        File excel = new File(dataPath);
        Workbook wb = new XSSFWorkbook(excel);
        Sheet sheet = wb.getSheetAt(0);     //读取sheet 0
        int firstRowIndex = 1;   //第一行是列名，所以不读
        int lastRowIndex = sheet.getLastRowNum();
        Map<Node, Integer> map = new HashMap<>();

        getWanxinp2pMap(sheet, firstRowIndex, lastRowIndex, map);
        for (Map.Entry<Node, Integer> entry : map.entrySet()) {
            //System.out.println("Key = " + entry.getKey().getTarget() + "; Value = " + entry.getValue());//为了算出target接口
            //System.out.println("Key = " + entry.getKey().getSource() + "," + entry.getKey().getTarget() +
              //      "; Value = " + entry.getValue());//为了算出有多少source-target对
            //if(entry.getKey().getVersion().equals("2"))
            System.out.println("Key = " + entry.getKey().getSource() + "," + entry.getKey().getTarget()
                    + "," +entry.getKey().getVersion()+ "; Value = " + entry.getValue());
        }


    }

    private static void getWanxinp2pMap(Sheet sheet, int firstRowIndex, int lastRowIndex, Map<Node, Integer> map) throws IOException, InvalidFormatException {
        Map<String, String> targetInterfaceMap = new HashMap<>();
//        targetInterfaceMap.put("mall-search.FeignSearchService.search(java", "EsProductController");
//        targetInterfaceMap.put("mall-admin.UmsAdminService.loadUserByUsername(java", "UmsAdminController");
//        targetInterfaceMap.put("mall-portal.UmsMemberService.loadUserByUsername(java", "UmsMemberController");
//        targetInterfaceMap.put("mall-portal.FeignPortalService.list()", "OmsCartItemController");
//        targetInterfaceMap.put("mall-portal.FeignPortalService.login(java", "UmsMemberController");
//        targetInterfaceMap.put("mall-auth.AuthService.getAccessToken(java", "AuthController");
//        targetInterfaceMap.put("mall-admin.FeignAdminService.getList()", "PmsBrandController");
//        targetInterfaceMap.put("mall-admin.FeignAdminService.login(com", "UmsAdminController");
//        for (Map.Entry<String, String> entry : targetInterfaceMap.entrySet()) {
//            System.out.println("Key = " + entry.getKey() + "; Value = " + entry.getValue());
//        }
        targetInterfaceMap.put("com.wanxin.transaction.agent.ConsumerApiAgent.getBalance", "com.wanxin.consumer.controller.ConsumerController");
        targetInterfaceMap.put("com.wanxin.uaa.agent.AccountApiAgent.login","com.wanxin.account.controller.AccountController");
        targetInterfaceMap.put("com.wanxin.repayment.agent.DepositoryAgentApiAgent.confirmRepayment","com.wanxin.depository.controller.DepositoryAgentController");
        targetInterfaceMap.put("com.wanxin.transaction.agent.DepositoryAgentApiAgent.createProject", "com.wanxin.depository.controller.DepositoryAgentController");
        targetInterfaceMap.put("com.wanxin.transaction.agent.ConsumerApiAgent.getCurrentLoginConsumer", "com.wanxin.consumer.controller.ConsumerController");
        targetInterfaceMap.put("com.wanxin.repayment.agent.DepositoryAgentApiAgent.userAutoPreTransaction", "com.wanxin.depository.controller.DepositoryAgentController");
        targetInterfaceMap.put("com.wanxin.transaction.agent.DepositoryAgentApiAgent.modifyProjectStatus", "com.wanxin.depository.controller.DepositoryAgentController");
        targetInterfaceMap.put("com.wanxin.consumer.agent.DepositoryAgentApiAgent.createRechargeRecord", "com.wanxin.depository.controller.DepositoryAgentController");
        targetInterfaceMap.put("com.wanxin.transaction.agent.DepositoryAgentApiAgent.confirmLoan", "com.wanxin.depository.controller.DepositoryAgentController");
        targetInterfaceMap.put("com.wanxin.consumer.agent.DepositoryAgentApiAgent.createWithdrawRecord", "com.wanxin.depository.controller.DepositoryAgentController");
        targetInterfaceMap.put("com.wanxin.consumer.agent.DepositoryAgentApiAgent.createConsumer", "com.wanxin.depository.controller.DepositoryAgentController");
        targetInterfaceMap.put("com.wanxin.consumer.agent.AccountApiAgent.register", "com.wanxin.account.controller.AccountController");
        targetInterfaceMap.put("com.wanxin.transaction.agent.ContentSearchApiAgent.queryProjectIndex", "com.wanxin.search.controller.ContentSearchController");
        targetInterfaceMap.put("com.wanxin.transaction.agent.DepositoryAgentApiAgent.userAutoPreTransaction", "com.wanxin.depository.controller.DepositoryAgentController");
        for(int rIndex = firstRowIndex ; rIndex <= lastRowIndex; rIndex++) {   //遍历行
            Row row = sheet.getRow(rIndex);
            String sourceMicro = row.getCell(5).toString();
            String targetMicro = row.getCell(7).toString();
            Cell sourceCell = row.getCell(6);
            Cell targetCell = row.getCell(8);
            String[] sourceStr,targetStr;
            sourceStr = sourceCell.toString().split("\\(");
            targetStr = targetCell.toString().split("\\(");
            String source,target;
//            if(sourceStr[5].equals("impl")){
//                source = sourceMicro + "." + sourceStr[6];
//            }else{
//                source = sourceMicro + "." + sourceStr[5];
//            }
//            if(targetStr[5].equals("getAccessToken(java")){
//                target = targetMicro + "." + targetStr[4] + "." + targetStr[5];
//            }else{
//                target = targetMicro + "." + targetStr[5] + "." + targetStr[6];
//            }

            source = sourceMicro + "+" + sourceStr[0].split("\\.")[0] + "." + sourceStr[0].split("\\.")[1] +
            "." + sourceStr[0].split("\\.")[2] + "." + sourceStr[0].split("\\.")[3] + "." + sourceStr[0].split("\\.")[4];
            target = targetInterfaceMap.get(targetStr[0]);
            target = targetMicro + "+" + target;
            String version = row.getCell(3).toString();
            //Node node = new Node(target);//为了算出target接口
            //Node node = new Node(source, target);//为了算出有多少source-target对
            Node node = new Node(source, target, version);
            if(map.containsKey(node)){
                map.put(node, map.get(node) + 1);
            }else{
                map.put(node, 1);
            }
        }
        String outPath = "C:\\Users\\zhao\\Desktop\\F\\aaZ\\MSA\\xx\\依赖数据\\wanxin-p2p\\v";

        for(int i = 1; i <= 4; i ++){
            int tnt = 1;
            String wbPath = outPath + i + "服务间调用依赖关系.xlsx";
            FileInputStream excelFileInputStream = new FileInputStream(wbPath);
            XSSFWorkbook wbOut = new XSSFWorkbook(excelFileInputStream);
            excelFileInputStream.close();
            XSSFSheet sheetOut = wbOut.getSheet("data");
            for (Map.Entry<Node, Integer> entry : map.entrySet()) {
                //System.out.println("Key = " + entry.getKey().getTarget() + "; Value = " + entry.getValue());//为了算出target接口
                //System.out.println("Key = " + entry.getKey().getSource() + "," + entry.getKey().getTarget() +
                //      "; Value = " + entry.getValue());//为了算出有多少source-target对
                if(entry.getKey().getVersion().equals(Integer.toString(i))){
                    Row row = sheetOut.createRow(tnt);
                    row.createCell(0).setCellValue(entry.getKey().getSource().split("\\+")[0]);
                    row.createCell(1).setCellValue(entry.getKey().getSource().split("\\+")[1]);
                    row.createCell(2).setCellValue(entry.getKey().getTarget().split("\\+")[0]);
                    row.createCell(3).setCellValue(entry.getKey().getTarget().split("\\+")[1]);
                    row.createCell(4).setCellValue(entry.getValue().toString());
                    tnt++;
                }

            }
            FileOutputStream excelFileOutPutStream = new FileOutputStream(wbPath);
            wbOut.write(excelFileOutPutStream);     //写入到指定的文件
            excelFileOutPutStream.flush();
            excelFileOutPutStream.close();
        }

    }

}
