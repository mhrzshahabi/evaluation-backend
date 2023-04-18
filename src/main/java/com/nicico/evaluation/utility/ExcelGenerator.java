package com.nicico.evaluation.utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;

import java.lang.reflect.Field;

public class ExcelGenerator<T>{


    private List<T> data;
    private Field[] dataFields;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public ExcelGenerator() {
        workbook = new XSSFWorkbook();
    }

    public ExcelGenerator(List <T> data) {
        this.setData(data);
        workbook = new XSSFWorkbook();
    }

    public void setData(List <T> data) {
        this.data = data;
        this.getDataFields();
    }


    private void getDataFields() {
        T temp = data.get(0);
        dataFields = temp.getClass().getDeclaredFields();
    }
    private void createSheet(String sheetName) {
        if(Objects.isNull(sheetName) || sheetName.trim().isEmpty()) {
            sheetName = this.data.get(0).getClass().getName();
            sheetName = sheetName.substring(sheetName.lastIndexOf(".")+1);
        }
        sheet = workbook.createSheet(sheetName);
    }


    private void writeHeader() {
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        int count = 0;
        for(Field f : this.dataFields) {
            createCell(row, count, f.getName() , style);
            count++;
        }
    }
    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
        Cell cell = row.createCell(columnCount);
        if (valueOfCell instanceof Integer) {
            cell.setCellValue((Integer) valueOfCell);
        } else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
        } else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
        } else if (valueOfCell instanceof Boolean) {
            cell.setCellValue((Boolean) valueOfCell);
        }
        cell.setCellStyle(style);
        sheet.autoSizeColumn(columnCount, true);
    }


    private void write() throws IllegalArgumentException, IllegalAccessException  {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        for (T record : this.data) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            for(Field f : this.dataFields) {
                f.setAccessible(true);
                createCell(row, columnCount++, f.get(record), style);
            }
        }
    }

    public void generateSheet(String sheetName) {
        try {
            createSheet(sheetName);
            writeHeader();
            write();
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public void getExcelToResponse(ServletOutputStream outputStream)  {
        try{
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ByteArrayOutputStream getExcel() {
        try( ByteArrayOutputStream byteStream = new ByteArrayOutputStream();) {
            workbook.write(byteStream);
            workbook.close();
            return byteStream;
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
