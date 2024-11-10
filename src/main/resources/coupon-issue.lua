if redis.call('SISMEMBER', KEYS[1], ARGV[1]) == 1 then
    return 'duplicated'
end

if tonumber(ARGV[2]) > redis.call('SCARD', KEYS[1]) then
    redis.call('SADD', KEYS[1], ARGV[1])
    return 'success'
end

return 'invalid'