package com.meiya.excel;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.excel.write.metadata.WriteWorkbook;
import com.meiya.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xiaopf
 */
@Slf4j
public abstract class BaseEasyExcelExport {

    /**
     * @param fileName   文件名
     * @param conditions 查询条件
     */
    protected void exportExcel(String fileName, Map<String, Object> conditions) {
        HttpServletResponse response = SpringContextUtils.getHttpServletResponse();
        //根据条件查询出来的数据库的总记录数
        Long totalCount = dataTotalCount(conditions);
        //每个sheet页需要写入的记录数
        Long sheetDataRows = eachSheetTotalCount();
        //每次需要写入的数据量
        Long writeDataRows = eachWriteTotalCount();
        //如果所有数据记录数小于每页需要写入的记录数 则每页记录数等于总记录数
        if (totalCount < sheetDataRows) {
            sheetDataRows = totalCount;
        }
        //如果每次需要写入的数据量大于sheet页的记录数 则每次需要写入的数据量等于sheet页的记录数
        if (writeDataRows > sheetDataRows) {
            writeDataRows = sheetDataRows;
        }
        doExport(response, fileName, conditions, totalCount, sheetDataRows, writeDataRows);
    }

    /**
     * 导出到excel
     *
     * @param response      响应
     * @param fileName      文件名
     * @param conditions    查询条件
     * @param totalCount    数据库总记录数
     * @param sheetDataRows 每个sheet页的记录数
     * @param writeDataRows 每次写入的记录数
     */
    private void doExport(HttpServletResponse response, String fileName, Map<String, Object> conditions, Long totalCount, Long sheetDataRows, Long writeDataRows) {
        try (
                OutputStream os = response.getOutputStream()
        ) {
            WriteWorkbook writeWorkbook = new WriteWorkbook();
            writeWorkbook.setOutputStream(os);
            writeWorkbook.setExcelType(ExcelTypeEnum.XLSX);
            ExcelWriter excelWriter = new ExcelWriter(writeWorkbook);
            WriteTable writeTable = new WriteTable();
            writeTable.setHead(getExcelHead());
            Triple<Long, Long, Long> computeTriple = compute(totalCount, sheetDataRows, writeDataRows);
            Long sheetNum = computeTriple.getLeft();
            Long oneSheetWriteCount = computeTriple.getMiddle();
            Long lastSheetWriteCount = computeTriple.getRight();
            List<List<String>> dataList = new ArrayList<>();
            //创建sheet
            for (int i = 0;i < sheetNum; i++){
                WriteSheet writeSheet = new WriteSheet();
                writeSheet.setSheetNo(i);
                writeSheet.setSheetName(sheetNum == 1 ? fileName : fileName + i);
                for (int j = 0;j < (i != sheetNum - 1 || i == 0 ? oneSheetWriteCount : lastSheetWriteCount); j++){
                    dataList.clear();
                    buildDataList(dataList, conditions, j + 1 + oneSheetWriteCount * i, writeDataRows);
                    excelWriter.write(dataList, writeSheet, writeTable);
                }
            }
            response.setHeader("Content-Disposition", "attachment;filename="
                    + new String((fileName).getBytes("gb2312"),
                    StandardCharsets.ISO_8859_1) + ".xlsx");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            excelWriter.finish();
            os.flush();
        } catch (Exception e) {
            log.error("导出excel到【{}】时发生异常:【{}】", fileName, e.getMessage(), e);
        }
    }

    /**
     * 构建每次查询数量
     * @param dataList 结果列表
     * @param conditions 查询条件
     * @param pageNum 分页 页数
     * @param pageSize 分页 条数
     */
    protected abstract void buildDataList(List<List<String>> dataList, Map<String, Object> conditions, Long pageNum, Long pageSize);

    /**
     * 计算需要的sheet数量、一般情况下每个sheet需要写入数据的次数、最后一个sheet需要写入数据的次数
     *
     * @param totalCount    总数据记录数
     * @param sheetDataRows 每个sheet页写入的记录数
     * @param writeDataRows 每次写入的记录数
     * @return 【需要的sheet数量、一般情况下每个sheet需要写入数据的次数、最后一个sheet需要写入数据的次数】的triple集
     */
    private Triple<Long, Long, Long> compute(Long totalCount, Long sheetDataRows, Long writeDataRows) {
        //需要的sheet数
        // 总90 每sheet30 【3页】 --- 总100 每sheet30 【4页】
        Long sheetNum = totalCount % sheetDataRows == 0 ?
                (totalCount / sheetDataRows) : (totalCount / sheetDataRows + 1);
        //每个sheet页需要写入数据的次数
        //总90 每sheet30 每次写入7 每个sheet页写28【4次】
        //总90 每sheet100 每个sheet写30【3次】 每个sheet写40 【3次】
        Long oneSheetWriteCount = totalCount > sheetDataRows ?
                (sheetDataRows / writeDataRows) :
                (totalCount % writeDataRows > 0 ? totalCount / writeDataRows + 1 : totalCount / writeDataRows);
        //最后一个sheet页需要写入数据的次数
        Long lastSheetWriteCount = totalCount % sheetDataRows == 0 ?
                (oneSheetWriteCount) :
                (totalCount % sheetDataRows % writeDataRows == 0 ?
                        (totalCount / sheetDataRows / writeDataRows) :
                        (totalCount / sheetDataRows / writeDataRows + 1));
        return Triple.of(sheetNum, oneSheetWriteCount, lastSheetWriteCount);

    }

    /**
     * 指定表头
     *
     * @return 表头
     */
    protected abstract List<List<String>> getExcelHead();

    /**
     * 每次需要写入的数据量
     *
     * @return 行数
     */
    protected abstract Long eachWriteTotalCount();

    /**
     * 每个sheet页写的数据量
     *
     * @return 行数
     */
    protected abstract Long eachSheetTotalCount();

    /**
     * 所有数据的行数
     *
     * @param conditions 查询条件
     * @return 行数
     */
    protected abstract Long dataTotalCount(Map<String, Object> conditions);
}
