package com.meiya.entity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author xiaopf
 */
@Data
public class PageResult<T> implements Serializable {
    /**
     * 总记录数
     */
    private Long total;

    /**
     * 总页数
     */
    private Long pages;

    /**
     * 当前页数
     */
    private Long current;

    /**
     * 每页条数
     */
    private Long size;

    /**
     * 记录
     */
    private List<T> records = Collections.emptyList();

    public void loadPageData(IPage<T> page){
        this.setTotal(page.getTotal());
        this.setPages(page.getPages());
        this.setCurrent(page.getCurrent());
        this.setSize(page.getSize());
        this.setRecords(page.getRecords());
    }


}
