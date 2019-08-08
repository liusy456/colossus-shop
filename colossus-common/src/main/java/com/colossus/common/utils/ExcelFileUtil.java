package com.colossus.common.utils;


import com.colossus.common.exception.ServiceException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ExcelFileUtil {


    public static <T> List<T> readDataFromFirstSheet(File file, Map<String, String> colAttrMapping, Class<T> returnObjectType) {
        List<Map<String, Object>> mapData = readDataFromFirstSheet(file, colAttrMapping.keySet());
        List<T> results = new ArrayList<>();
        for(Map<String,Object> data:mapData){
            T obj;
            try {
                data = convertFromColToAttr(data,colAttrMapping);
                obj = returnObjectType.newInstance();
                BeanUtilsBean.getInstance().getConvertUtils().register(false,false,0);
                BeanUtils.populate(obj, data);
            }catch (Exception e){
                throw new ServiceException("处理Excel数据出现错误",e);
            }
            results.add(obj);
        }

        return results;
    }

    /**
     * 把Map数据的Key从表格名称转换为对象属性名称
     * @param data
     * @param colAttrMapping
     * @return
     */
    private static Map<String,Object> convertFromColToAttr(Map<String, Object> data, Map<String, String> colAttrMapping) {
          Map<String,Object> converted =  new HashMap<>();
          for(Map.Entry<String,String> entry:colAttrMapping.entrySet()){
              converted.put(entry.getValue(),data.get(entry.getKey()));
          }
          return converted;
    }


    public static List<Map<String, Object>> readDataFromFirstSheet(File file, Set<String> colNames) {
        Workbook wb;
        try {
            wb = WorkbookFactory.create(file);
        } catch (Exception e) {
            throw new ServiceException("解析Excel文件出现错误",e);
        }
        Sheet sheet = wb.getSheetAt(0);
        FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
        Map<String,Integer> headerMap = getHeaderMap(colNames,sheet);
        List<Map<String,Object>> dataList = new ArrayList<>();
        int totalRows = sheet.getLastRowNum();
        for(int i=1;i<=totalRows;i++){
            Row row = sheet.getRow(i);
            if(row==null){
                continue;
            }
            LinkedHashMap<String,Object> dataValue = new LinkedHashMap<>();
            Set<Map.Entry<String, Integer>> entries = headerMap.entrySet();
            for(Map.Entry<String, Integer> entry:entries){
                Cell cell = evaluator.evaluateInCell(row.getCell(entry.getValue()));

                Object value;
                if(cell==null){
                    value = null;
                }else {
                    CellType cellType = cell.getCellTypeEnum();
                    switch (cellType) {
                        case FORMULA:
                            throw new ServiceException(String.format("不支持带公式的Excel导入，提示：第%s行%s列",cell.getRow().getRowNum()+1,cell.getColumnIndex()+1));
                        case STRING:
                            value = StringUtils.trim(cell.getStringCellValue());
                            break;
                        case BOOLEAN:
                            value = cell.getBooleanCellValue();
                            break;
                        case NUMERIC:
                            value = new BigDecimal(new Double(cell.getNumericCellValue()).toString()).toString(); //同时兼容整数和小数转换,不影响精确度
                            break;
                        default:
                            value = "";


                    }
                }
                dataValue.put(entry.getKey(),value);
            }
//            if(filter!=null && filter.skip(dataValue)){
//                continue;
//            }
            dataList.add(dataValue);
        }

        return dataList;
    }

    private boolean skip(LinkedHashMap<String, Object> rowData) {
        Object firstValue = rowData.values().iterator().next();
        if(firstValue==null){
            return true;
        }
        if(StringUtils.isBlank(firstValue.toString())){
            return true;
        }
        return false;
    }
    public static File generateExcel(List dataList, LinkedHashMap<String, String> colMapping,String fileName) {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet1 = wb.createSheet("Sheet1");
        HSSFRow row = sheet1.createRow(0);
        int i=0;
        for(String colName : colMapping.keySet()){
            HSSFCell cell = row.createCell(i++);
            cell.setCellValue(colName);
        }
        int rowNum = 1;
        for(Object data:dataList){
            row = sheet1.createRow(rowNum++);
            Map<String,String> dataMap;
            try {
                dataMap = BeanUtils.describe(data);
            }  catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new ServiceException("生成Excel文件时出现错误",e);
            }
            i=0;
            for(Map.Entry<String,String> col:colMapping.entrySet()){
                String value ="";
                if(StringUtils.isNotEmpty(col.getValue())){
                    value = dataMap.get(col.getValue());
                }
                HSSFCell cell = row.createCell(i++);
                cell.setCellValue(value);
            }
        }
        for(i=0;i<colMapping.size();i++) {
            sheet1.autoSizeColumn(i);
        }

        File tempFile;
        try {
            tempFile = File.createTempFile("tembin-" + fileName + "-", ".xls");
            FileOutputStream outputStream = new FileOutputStream(tempFile);
            wb.write(outputStream);
            outputStream.close();
        } catch (IOException e) {
            throw new ServiceException("生成Excel文件时出现错误",e);
        }

        return tempFile;
    }

    public static void writeDataToHSSFWorkbook(List dataList, LinkedHashMap<String, String> colMapping, HSSFWorkbook wb, String sheetName) {
        HSSFSheet sheet1 = wb.createSheet(sheetName);
        HSSFRow row = sheet1.createRow(0);
        int i=0;
        for(String colName : colMapping.keySet()){
            HSSFCell cell = row.createCell(i++);
            cell.setCellValue(colName);
        }
        int rowNum = 1;
        for(Object data:dataList){
            row = sheet1.createRow(rowNum++);
            Map<String,Object> dataMap;
            dataMap = AppUtil.entityToMap(data);

            i=0;
            for(Map.Entry<String,String> col:colMapping.entrySet()){
                Object value = null;
                if(StringUtils.isNotEmpty(col.getValue())){
                    value = dataMap.get(col.getValue());
                }
                HSSFCell cell = row.createCell(i++);
                if(value instanceof String){
                    cell.setCellValue((String) value);
                }else if(value instanceof Date){
                    cell.setCellValue((Date) value);
                }else if(value instanceof Number){
                    cell.setCellValue(((Number) value).doubleValue());
                }else {
                    if(value==null){
                        cell.setCellValue("");
                    }else {
                        cell.setCellValue(value.toString());
                    }
                }
            }
        }
        for(i=0;i<colMapping.size();i++) {
            sheet1.autoSizeColumn(i);
        }
    }

    private static Map<String,Integer> getHeaderMap(Set<String> headers, Sheet sheet) {
        Row row = sheet.getRow(0);
        if(row==null){
            throw new ServiceException("Excel首行为空");
        }
        short cols = row.getLastCellNum();
        Map<String,Integer> results = new HashMap<>();
        headers = new HashSet<>(headers);
        for(Iterator<String> it = headers.iterator();it.hasNext();){
            String header = it.next();
            for(int i=0;i<cols;i++){
                Cell cell = row.getCell(i);
                if(StringUtils.equalsIgnoreCase(header, StringUtils.trimToEmpty(cell.getStringCellValue()))){
                    results.put(header,i);
                    it.remove();
                    break;
                }
            }
        }
        if(headers.size()>0){
            throw new IllegalArgumentException(String.format("表格格式不正确，缺少列：%s", StringUtils.join(headers,",")));
        }
        return results;
    }
}
