package com.nicico.evaluation.utility;

import com.nicico.evaluation.enums.PersianColumnName;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ExcelGenerator<T> {

    @Getter
    @Setter
    public static class ExcelDownload {
        private String contentType;
        private String headerValue;
        private byte[] content;

        public ExcelDownload(byte[] content) {
            String currentDateTime = new SimpleDateFormat("yyyy/MM/dd___HH-mm-ss").format(new Date());
            this.contentType = "application/octet-stream";
            this.headerValue = "attachment; filename=data_" + currentDateTime + ".xlsx";
            this.content = content;
        }
    }

    private List<T> data;
    private Field[] dataFields;
    private final XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private XSSFCellStyle xssfBlankCellStyle;
    private XSSFCellStyle xssfHeaderCellStyle;
    private XSSFCellStyle xssfStringCellStyle;
    private XSSFCellStyle xssfNumberCellStyle;
    private XSSFCellStyle xssfTopRowTitleCellStyle;
    private XSSFCellStyle xssfNumberCommaCellStyle;
    private XSSFCellStyle xssfNumberCommaCellStyleSum;

    public ExcelGenerator() {
        workbook = new XSSFWorkbook();
    }

    public ExcelGenerator(List<T> data) {
        this.setData(data);
        workbook = new XSSFWorkbook();
        setCellStyles();
    }

    private void setCellStyles() {

        xssfTopRowTitleCellStyle = workbook.createCellStyle();
        xssfHeaderCellStyle = workbook.createCellStyle();
        xssfStringCellStyle = workbook.createCellStyle();
        xssfNumberCellStyle = workbook.createCellStyle();
        xssfNumberCommaCellStyle = workbook.createCellStyle();
        XSSFCellStyle xssfNumberComma3CellStyle = workbook.createCellStyle();
        xssfBlankCellStyle = workbook.createCellStyle();
        xssfNumberCommaCellStyleSum = workbook.createCellStyle();
        XSSFCellStyle xssfNumberComma3CellStyleSum = workbook.createCellStyle();

        // Double 3 dot format
        XSSFDataFormat xssfDataFormat = workbook.createDataFormat();
        short doubleDecimalDataFormat = xssfDataFormat.getFormat("#,##0.000");

        // topRowTitle
        xssfTopRowTitleCellStyle.setBorderLeft(BorderStyle.THIN);
        xssfTopRowTitleCellStyle.setBorderTop(BorderStyle.THIN);
        xssfTopRowTitleCellStyle.setBorderRight(BorderStyle.THIN);
        xssfTopRowTitleCellStyle.setBorderBottom(BorderStyle.THIN);
        xssfTopRowTitleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        xssfTopRowTitleCellStyle.setAlignment(HorizontalAlignment.CENTER);
        byte[] rgb = new byte[3];
        rgb[0] = (byte) 254; // red
        rgb[1] = (byte) 244; // green
        rgb[2] = (byte) 230; // blue
        XSSFColor customColor = new XSSFColor(rgb, new DefaultIndexedColorMap());
        xssfTopRowTitleCellStyle.setFillForegroundColor(customColor);
        xssfTopRowTitleCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Header Row
        xssfHeaderCellStyle.setBorderLeft(BorderStyle.THIN);
        xssfHeaderCellStyle.setBorderTop(BorderStyle.THIN);
        xssfHeaderCellStyle.setBorderRight(BorderStyle.THIN);
        xssfHeaderCellStyle.setBorderBottom(BorderStyle.THIN);
        xssfHeaderCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        xssfHeaderCellStyle.setAlignment(HorizontalAlignment.CENTER);

        // String Fields
        xssfStringCellStyle.setAlignment(HorizontalAlignment.RIGHT);
        xssfStringCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        xssfStringCellStyle.setBorderLeft(BorderStyle.THIN);
        xssfStringCellStyle.setBorderTop(BorderStyle.THIN);
        xssfStringCellStyle.setBorderRight(BorderStyle.THIN);
        xssfStringCellStyle.setBorderBottom(BorderStyle.THIN);

        // Standard Number Fields
        xssfNumberCellStyle.setAlignment(HorizontalAlignment.RIGHT);
        xssfNumberCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        xssfNumberCellStyle.setBorderLeft(BorderStyle.THIN);
        xssfNumberCellStyle.setBorderTop(BorderStyle.THIN);
        xssfNumberCellStyle.setBorderRight(BorderStyle.THIN);
        xssfNumberCellStyle.setBorderBottom(BorderStyle.THIN);
        xssfNumberCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));

        // Comma Separated Number Fields
        xssfNumberCommaCellStyle.setAlignment(HorizontalAlignment.RIGHT);
        xssfNumberCommaCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        xssfNumberCommaCellStyle.setBorderLeft(BorderStyle.THIN);
        xssfNumberCommaCellStyle.setBorderTop(BorderStyle.THIN);
        xssfNumberCommaCellStyle.setBorderRight(BorderStyle.THIN);
        xssfNumberCommaCellStyle.setBorderBottom(BorderStyle.THIN);
        xssfNumberCommaCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));

        // Comma Separated With 3 Dot Number Fields
        xssfNumberComma3CellStyle.setAlignment(HorizontalAlignment.RIGHT);
        xssfNumberComma3CellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        xssfNumberComma3CellStyle.setBorderLeft(BorderStyle.THIN);
        xssfNumberComma3CellStyle.setBorderTop(BorderStyle.THIN);
        xssfNumberComma3CellStyle.setBorderRight(BorderStyle.THIN);
        xssfNumberComma3CellStyle.setBorderBottom(BorderStyle.THIN);
        xssfNumberComma3CellStyle.setDataFormat(doubleDecimalDataFormat);

        // String Fields
        xssfBlankCellStyle.setAlignment(HorizontalAlignment.RIGHT);
        xssfBlankCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        xssfBlankCellStyle.setBorderTop(BorderStyle.THIN);

        // Comma Separated Number Fields
        xssfNumberCommaCellStyleSum.setAlignment(HorizontalAlignment.RIGHT);
        xssfNumberCommaCellStyleSum.setVerticalAlignment(VerticalAlignment.CENTER);
        xssfNumberCommaCellStyleSum.setBorderLeft(BorderStyle.THIN);
        xssfNumberCommaCellStyleSum.setBorderTop(BorderStyle.THIN);
        xssfNumberCommaCellStyleSum.setBorderRight(BorderStyle.THIN);
        xssfNumberCommaCellStyleSum.setBorderBottom(BorderStyle.THIN);
        xssfNumberCommaCellStyleSum.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
        xssfNumberCommaCellStyleSum.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        xssfNumberCommaCellStyleSum.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Comma Separated With 3 Dot Number Fields
        xssfNumberComma3CellStyleSum.setAlignment(HorizontalAlignment.RIGHT);
        xssfNumberComma3CellStyleSum.setVerticalAlignment(VerticalAlignment.CENTER);
        xssfNumberComma3CellStyleSum.setBorderLeft(BorderStyle.THIN);
        xssfNumberComma3CellStyleSum.setBorderTop(BorderStyle.THIN);
        xssfNumberComma3CellStyleSum.setBorderRight(BorderStyle.THIN);
        xssfNumberComma3CellStyleSum.setBorderBottom(BorderStyle.THIN);
        xssfNumberComma3CellStyleSum.setDataFormat(doubleDecimalDataFormat);
        xssfNumberComma3CellStyleSum.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        xssfNumberComma3CellStyleSum.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    public void setData(List<T> data) {
        this.data = data;
        this.getDataFields();
    }

    private void getDataFields() {
        T temp = data.get(0);
        dataFields = temp.getClass().getDeclaredFields();
    }

    private void createSheet(String sheetName) throws IOException {
        if (Objects.isNull(sheetName) || sheetName.trim().isEmpty()) {
            sheetName = this.data.get(0).getClass().getName();
            sheetName = sheetName.substring(sheetName.lastIndexOf(".") + 1);
        }
        sheet = workbook.createSheet(sheetName);
        sheet.setRightToLeft(Boolean.TRUE);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0,
                Arrays.stream(dataFields).filter(field -> !field.getName().equals("id")).toList().size() - 1));
    }

    private void writeFullHeader() throws IOException {
        Row row = sheet.createRow(0);
        row.setHeight((short) 1500);
        createCell(row, 0, null, xssfHeaderCellStyle);
        Drawing drawing = sheet.createDrawingPatriarch();
        insertImageToCell(workbook, "xlsx", 0, 0, dataFields.length, 1, drawing, "/image/header-logo.png");
    }

    private void writeSubFullHeader(String subFullHeader) {
        Row row = sheet.createRow(1);
        createCell(row, 0, subFullHeader, xssfTopRowTitleCellStyle);
    }

    private void writeHeader() {
        Row row = sheet.createRow(2);
        int count = 0;
        String fieldName = null;
        for (Field f : this.dataFields) {
            fieldName = f.getName().toLowerCase();
            if (fieldName.equals("id"))
                continue;
            createCell(row, count, PersianColumnName.getPersianColumnName(fieldName), xssfStringCellStyle);
            count++;
        }
    }

    private void write() throws IllegalArgumentException, IllegalAccessException {
        int rowCount = 3;
        for (T record : this.data) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            for (Field f : this.dataFields) {
                if (f.getName().toLowerCase().equals("id"))
                    continue;
                f.setAccessible(true);
                createCell(row, columnCount++, f.get(record), xssfStringCellStyle);
            }
        }
    }

    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
        Cell cell = row.createCell(columnCount);
        if (valueOfCell instanceof Integer) {
            cell.setCellValue((Integer) valueOfCell);
        } else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
        } else if (valueOfCell instanceof Boolean) {
            cell.setCellValue((Boolean) valueOfCell);
        } else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
        }
        cell.setCellStyle(style);
    }

    public void generateSheet(String sheetName, String subFullHeader) {
        try {
            createSheet(sheetName);
            writeFullHeader();
            writeSubFullHeader(subFullHeader);
            writeHeader();
            write();
        } catch (IllegalArgumentException | IllegalAccessException | IOException e) {
            e.printStackTrace();
        }
    }

    public void getExcelToResponse(ServletOutputStream outputStream) {
        try {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void insertImageToCell(Workbook workbook, String fileType, int colNum1, int rowNum1, int colNum2, int rowNum2, Drawing drawing,
                                  String imageName) throws IOException {

        InputStream is = this.getClass().getResourceAsStream(imageName);
        byte[] inputImageBytes = IOUtils.toByteArray(is);
        int inputImagePictureID = workbook.addPicture(inputImageBytes, Workbook.PICTURE_TYPE_PNG);
        is.close();

        ClientAnchor anchor = null;
        if (fileType.equals("xls")) {
            anchor = new HSSFClientAnchor();
        } else {
            anchor = new XSSFClientAnchor();
        }
        anchor.setCol1(colNum1);
        anchor.setRow1(rowNum1);
        anchor.setCol2(colNum2);
        anchor.setRow2(rowNum2);
        drawing.createPicture(anchor, inputImagePictureID);
    }

    public ByteArrayOutputStream getExcel() {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();) {
            workbook.write(byteStream);
            workbook.close();
            return byteStream;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
