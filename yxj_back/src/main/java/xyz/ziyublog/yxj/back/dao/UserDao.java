package xyz.ziyublog.yxj.back.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.ziyublog.yxj.back.pojo.User;

public interface UserDao extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
