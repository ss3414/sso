package com.common.shiro;

import cn.hutool.crypto.SecureUtil;
import com.demo.dao.UserDao;
import com.demo.model.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.util.Optional;

public class RBACRealm extends AuthorizingRealm {

    @Autowired
    private UserDao userDao;

    /* 登录 */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String name = token.getUsername();
        String password = new String(token.getPassword());

        User select = new User();
        select.setUserName(name);
        select.setUserPassword(SecureUtil.md5(password));
        Example<User> userExample = Example.of(select);
        Optional<User> exist = userDao.findOne(userExample);
        if (!exist.isPresent()) {
            throw new AuthenticationException("用户不存在/密码错误");
        } else {
            User result = exist.get();
            return new SimpleAuthenticationInfo(result, result.getUserPassword(), getName());
        }
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return new SimpleAuthorizationInfo();
    }

}
