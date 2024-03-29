package com.kwonyoon.springexample.redis;

import com.google.common.collect.Lists;
import com.kwonyoon.springexample.entity.Point;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
public class PointDao {

    @Autowired
    RedisTemplate<String, Point> redisTemplate;

//    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Resource(name="pointRedisTemplate")
    SetOperations<String, Point> setOperations;

    public void add(String key, Point point) {
        setOperations.add(key, point);
    }

    public List<Point> get(String key) {
        Set<Point> values = setOperations.members(key);
        if (values == null)
            return Lists.newArrayList();
        List<Point> list = Lists.newArrayList(values);
        list.sort((o1, o2) -> o1.getX() == o2.getX() ? o1.getY() - o2.getY() : o1.getX() - o2.getX());
        return list;
    }

    public void del(String key){
        redisTemplate.unlink(key);
    }
}