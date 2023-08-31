package com.meiya.excel;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiya.entity.po.UserPo;
import com.meiya.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author xiaopf
 */
@Component
public class UserExcelExport extends BaseEasyExcelExport{

    @Resource
    private UserService userService;
    public void export(String fileName,Map<String,Object> conditions){
        super.exportExcel(fileName,conditions);
    }
    @Override
    protected void buildDataList(List<List<String>> dataList, Map<String, Object> conditions, Long pageNum, Long pageSize) {
        Page<UserPo> page = userService.page(new Page<>(pageNum, pageSize));
        List<UserPo> records = page.getRecords();
        if (CollectionUtils.isNotEmpty(records)){
            records.forEach(record -> {
                dataList.add(Arrays.asList(record.getId().toString(),record.getName()));
            });
        }
    }

    @Override
    protected List<List<String>> getExcelHead() {
        List<List<String>> heads = new ArrayList<>();
        heads.add(Collections.singletonList("用户编号"));
        heads.add(Collections.singletonList("用户名称"));
        return heads;
    }

    @Override
    protected Long eachWriteTotalCount() {
        return 5L;
    }

    @Override
    protected Long eachSheetTotalCount() {
        return 50L;
    }

    @Override
    protected Long dataTotalCount(Map<String, Object> conditions) {
        long count = userService.count();
        return count;
    }
}
