package cn.csdb.common;


import cn.csdb.model.User;
import cn.csdb.service.UserService;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasAuthenticationException;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.cas.CasToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.util.WebUtils;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;


public class NCasRealm extends CasRealm {

    @Resource
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//        String username = (String) principals.fromRealm(getName()).iterator().next();
        String loginid = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
//        String roles = HTTPAccess.getRoles(username);
        boolean isAdmin = userService.isAdmin(loginid);
        //String roles = isAdmin ? "Admin" : "普通用户";
        String roles = isAdmin ? "admin" : "普通用户";
//        Userlog userlog = new Userlog();
//        userlog.setUsername(username);
//        userlog.setOp("登录");
//        userlog.setOptime(new Date());
//        userlogService.insertUserlog(userlog);
        if (Strings.isNullOrEmpty(roles)) {
            return authorizationInfo;
        }
//        authorizationInfo.setRoles(Sets.newHashSet(JSON.parseArray(roles, String.class)));
        authorizationInfo.setRoles(Sets.newHashSet(roles));
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        logger.info("======用户登陆认证======");
        CasToken casToken = (CasToken) authenticationToken;
        if (authenticationToken == null)
            return null;
        String ticket = (String) casToken.getCredentials();
        TicketValidator ticketValidator = ensureTicketValidator();
        try {
            Assertion casAssertion = ticketValidator.validate(ticket, getCasService());
            AttributePrincipal casPrincipal = casAssertion.getPrincipal();
            String loginId = casPrincipal.getName();
            User user = userService.getUserById(loginId);
            if (user == null) {
                userService.addGuestUser(loginId,casPrincipal.getAttributes().get("UserName").toString());
                user = userService.getUserById(loginId);
            }
            Map attributes = casPrincipal.getAttributes();
            casToken.setUserId(loginId);
            List<Object> principals = CollectionUtils.asList(new Object[] {
                    loginId, attributes
            });
            PrincipalCollection principalCollection = new SimplePrincipalCollection(principals, getName());
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession();
            session.setAttribute("loginId",loginId);
            return new SimpleAuthenticationInfo(principalCollection, ticket);
//            AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user.getTrueName(), null, "peng");
//            return authenticationInfo;
//            return null;
        } catch (TicketValidationException e) {
            throw new CasAuthenticationException((new StringBuilder()).append("Unable to validate ticket [").append(ticket).append("]").toString(), e);
        }
    }

}

