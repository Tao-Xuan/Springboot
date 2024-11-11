package com.example.springboot_login_demo.service.serviceImpl;

import com.example.springboot_login_demo.domain.User;
import com.example.springboot_login_demo.repository.UserDao;
import com.example.springboot_login_demo.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    @Override
    public User loginService(String uname, String password) {
        // 如果账号密码都对则返回登录的用户对象，若有一个错误则返回null
        User user = userDao.findByUnameAndPassword(uname, password);
        // 校验角色是否合法
        if (user != null) {
            // 如果你想根据角色进一步控制权限，可以在这里做角色验证
            if (user.getRole() == null || user.getRole().isEmpty()) {
                return null;  // 角色为空，表示不合法，登录失败
            }
            // 重要信息置空，避免将密码返回前端
            user.setPassword("");
            return user;
        }
        return null;
    }

    @Override
    public User registService(User user) {
        //当新用户的用户名已存在时
        if(userDao.findByUname(user.getUname())!=null){
            // 无法注册
            return null;
        }else{
            // 如果角色为空，默认设置为 "user"
            if (user.getRole() == null || user.getRole().isEmpty()) {
                user.setRole("user");  // 默认普通用户
            }
            //返回创建好的用户对象(带uid)
            User newUser = userDao.save(user);
            newUser.setPassword("");
            return newUser;
        }
    }
}
