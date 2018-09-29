package com.unionpay.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unionpay.info.TokenInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import java.io.IOException;

@Service
public class TokenUtils {

    @Autowired
    JedisSentinelPool jedisSentinelPool;

    // 存储临时令牌到redis中，存活期60秒
    public void setToken(String tokenId, TokenInfo tokenInfo) {
        ObjectMapper mapper = new ObjectMapper();
        String tokenInfoJson = null;
        try {
            tokenInfoJson = mapper.writeValueAsString(tokenInfo);
            Jedis jedis = jedisSentinelPool.getResource();
            boolean keyExist = jedis.exists(tokenId);
            // NX是不存在时才set， XX是存在时才set， EX是秒，PX是毫秒
            if (keyExist) {
                jedis.del(tokenId);
            }
            jedis.set(tokenId, tokenInfoJson, "NX", "EX", 600);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    //根据token键取TokenInfo
    public TokenInfo getToken(String tokenId) {
        Jedis jedis = jedisSentinelPool.getResource();
        ObjectMapper objectMapper = new ObjectMapper();
        TokenInfo tokenInfo = new TokenInfo();
        try {
            tokenInfo = objectMapper.readValue(jedis.get(tokenId), TokenInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tokenInfo;
    }

    //删除某个 token键值
    public void delToken(String tokenId) {
        Jedis jedis = jedisSentinelPool.getResource();
        boolean keyExist = jedis.exists(tokenId);
        // NX是不存在时才set， XX是存在时才set， EX是秒，PX是毫秒
        if (keyExist) {
            jedis.del(tokenId);
        }
    }

}
