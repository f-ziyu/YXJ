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
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            User user = operations.get(key);
            return user;
        } else {
            User user = userDao.findByUsername(username);
            operations.set(key, user, 5, TimeUnit.HOURS);
            return user;
        }
    }

    public User getUserByID(int id){
        ValueOperations<String, User> operations = redisTemplate.opsForValue();
        String key = "userId_" + id;
        boolean hasKey = redisTemplate.hasKey(key);
        if(hasKey) {
            User user = operations.get(key);
            return user;
        } else {
            User user = userDao.findById(id);
            operations.set(key, user, 5, TimeUnit.HOURS);
            return user;
        }
    }


    public boolean isExist(String username){
        return userDao.existsByUsername(username);
    }

    public void addUser(User user){
        ValueOperations<String, User> operations = redisTemplate.opsForValue();
        String key = "username_" + user.getUsername();
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            redisTemplate.delete(key);
        }
        userDao.save(user);
        operations.set(key, user, 5, TimeUnit.HOURS);
    }
}
