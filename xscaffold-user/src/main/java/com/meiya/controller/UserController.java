package com.meiya.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiya.convert.UserConverter;
import com.meiya.entity.po.UserPo;
import com.meiya.entity.req.UserReq;
import com.meiya.entity.PageResult;
import com.meiya.result.Result;
import com.meiya.service.UserService;
import com.meiya.util.LocalCacheUtil;
import com.meiya.util.MultiLevelCacheUtil;
import com.meiya.util.RedisDistributedLockUtil;
import com.meiya.util.RedisUtil;
import com.meiya.utils.ExportWordUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaopf
 */
@RestController
@RequestMapping("user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private RedisDistributedLockUtil redisDistributedLockUtil;

    @Resource
    private LocalCacheUtil<Long,PriceInfo> localCacheUtil;

    @Resource
    private MultiLevelCacheUtil<Long,PriceInfo> multiLevelCacheUtil;



    @Cacheable(cacheNames = "user",key = "'queryId' + #id")
    @GetMapping("/{id}")
    public UserPo getUserId(@PathVariable Long id){
        System.out.println("1");
        return userService.getById(id);
    }

    @PostMapping
    public Result<UserPo> addUser(@RequestBody UserReq userReq){
        UserPo userPo = UserConverter.INSTANCE.convertReqToPo(userReq);
        userService.save(userPo);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<UserPo> deleteUser(@PathVariable Long id){
        userService.removeById(id);
        return Result.ok();
    }

    @GetMapping
    public PageResult<UserPo> pageUser(@RequestBody UserReq userReq){
        IPage<UserPo> userPoPage = new Page<>(userReq.getPageIndex(),userReq.getPageSize());
        IPage<UserPo> page = userService.page(userPoPage);
        PageResult<UserPo> result = new PageResult<>();
        result.loadPageData(page);
        return result;
    }

    @GetMapping("/testRedis")
    public void testRedis(){
        redisUtil.set("aa","ok");
    }

    @GetMapping("/testLock")
    public void testRedisLock(){
        boolean lock = redisDistributedLockUtil.lock("xiao", 100L);
        System.out.println(lock);
    }

    @GetMapping("/testLog")
    public void testLog(){
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100 * 1000 ;i++){
            log.info("第{}条日志",i);
        }
        long end = System.currentTimeMillis();
        log.info("打印完成，耗时{}ms",end - start);
    }

    @GetMapping("/testWord")
    public void testWord() throws Exception {
        Map<String, Object> map = new HashMap<>(8);
        map.put("name","lili");
        map.put("mine","mine..");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(new Date());
        map.put("date",dateStr);
        ExportWordUtil.exportWord(map,"导出文件","wordTemplate.ftl");
    }

    @GetMapping("/testLocalCache")
    public void testLocalCache() {
        List<Long> idList = new ArrayList<>();
        idList.add(1L);
        idList.add(2L);
        Map<Long, PriceInfo> result = localCacheUtil.getResult(idList, "info:price", PriceInfo.class, this::getInfoPrice);
        System.out.println(result.size());
    }

    @GetMapping("/testMultiCache")
    public void testMultiCache(){
        List<Long> idList = new ArrayList<>();
        idList.add(1L);
        idList.add(2L);
        idList.add(3L);
        Map<Long, PriceInfo> result =
                multiLevelCacheUtil.getResult(idList, "info:price", PriceInfo.class, this::getInfoPrice, 3600, TimeUnit.SECONDS);
        System.out.println(result.size());
    }


    private Map<Long,PriceInfo> getInfoPrice(List<Long> idList)  {
        //select xx from xx where xx and id IN (...) 批量查询
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.info("业务执行时发生异常！");
        }
        //模拟返回查询结果
        Map<Long,PriceInfo> map = new HashMap<>();
        for (Long id : idList) {
            map.put(id, new PriceInfo(id, "30"));
        }
        return map;
    }

    @AllArgsConstructor
    static class PriceInfo{
        private Long id;
        private String price;
    }

    /**
     * 测试链路追踪 子线程
     */
    @GetMapping("/testTraceAsync")
    public void testTraceAsync(){
        log.info("async run方法执行前。。");
        CompletableFuture<String> futureResult = userService.asyncRun();
        futureResult.thenAccept(System.out::println);
        log.info("async run方法执行后。。");
    }
    /**
     * 测试链路追踪 同一线程
     */
    @GetMapping("/testTraceSync")
    public void testTraceSync(){
        log.info("sync run方法执行前。。");
        System.out.println(userService.syncRun());
        log.info("sync run方法执行后。。");
    }



}
