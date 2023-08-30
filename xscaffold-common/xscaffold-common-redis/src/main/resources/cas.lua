local key = KEYS[1]
local oldValue = ARGV[1]
local newValue = ARGV[2]

-- 获取redis中存储的值 若值不存在或者旧值等于存储的值 则将新值设置到redis中 否则不设置
local redisValue = redis.call('get',key)
if(redisValue == false or tonumber(redisValue) == tonumber(oldValue) )
then
    redis.call('set',key,newValue)
    return true
else
    return false
end