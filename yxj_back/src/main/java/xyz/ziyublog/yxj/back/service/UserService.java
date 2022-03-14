package xyz.ziyublog.yxj.back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import xyz.ziyublog.yxj.back.dao.UserDao;
import xyz.ziyublog.yxj.back.pojo.User;

import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    private RedisTemplate redisTemplate;


    public User getUserByUsername(String username){
        ValueOperations<String, User> operations = redisTemplate.opsForValue();
        String key = "username_" + username;
        //判断redis中是否有键为key的缓存
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            User user = operations.get(key);
            System.out.println("从缓存中获得数据："+user.getUsername());
            System.out.println("------------------------------------");
            return user;
        } else {
            User user = userDao.findByUsername(username);
            System.out.println("查询数据库获得数据："+user.getUsername());
            System.out.println("------------------------------------");
            // 写入缓存
            operations.set(key, user, 5, TimeUnit.HOURS);
            return user;
        }
    }

    public User getUserByID(int id){
        ValueOperations<String, User> operations = redisTemplate.opsForValue();
        String key = "userId_" + id;
        //判断redis中是否有键为key的缓存
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            User user = operations.get(key);
            System.out.println("从缓存中获得数据");
            System.out.println("------------------------------------");
            return user;
        } else {
            User user = userDao.findById(id);
            System.out.println("查询数据库获得数据"+user.getUsername());
            System.out.println("------------------------------------");
            // 写入缓存
            operations.set(key, user, 5, TimeUnit.HOURS);
            return user;
        }
    }

    public boolean isExist(String username){
        User user = getUserByUsername(username);
        return user!=null;
    }

    public void addUser(User user){
        userDao.save(user);
    }
}
