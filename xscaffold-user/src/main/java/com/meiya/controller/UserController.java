package com.meiya.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiya.convert.UserConverter;
import com.meiya.entity.po.UserPo;
import com.meiya.entity.req.UserReq;
import com.meiya.entity.PageResult;
import com.meiya.result.Result;
import com.meiya.service.UserService;
import com.meiya.util.RedisDistributedLockUtil;
import com.meiya.util.RedisUtil;
import com.meiya.utils.ExportWordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
}
