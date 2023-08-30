-- 每单位时间内最多n个请求
-- 在key未过期内 若判断currentTraffic + 1 > limit 则超出流量 需要限流

local key = KEYS[1]

-- 单位时间限流数
local limit = tonumber(ARGV[1])

-- 单位时间
local expireTime = ARGV[2]

-- 下一个请求进入前的当前流量
local currentTraffic = tonumber(redis.call('get',key) or "0" )


-- 新请求进来前 判断是否超过流量限制
if currentTraffic + 1 > limit
-- 超过流量限制 拒绝请求
then return 0
else
    -- 流量+1
    redis.call('INCRBY',key,1)
    -- 重新统计单位时间 过期后，再取出流量值便为0
    redis.call('EXPIRE',key,expireTime)
    -- 返回新请求进入后的当前流量
    return currentTraffic + 1
end