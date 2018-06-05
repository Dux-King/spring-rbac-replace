package com.github.angdx.gs.voter;

import com.github.angdx.gs.configattr.PasswdDateGrantAttribute;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;

import java.time.LocalDate;
import java.util.Collection;

public class ExpireDateVoter implements RBACAccessDecisionVoter<FilterInvocation> {

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return ((attribute != null)&&
                attribute.getAttribute().startsWith(PasswdDateGrantAttribute.CA_PREFIX));
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, FilterInvocation object,
                    Collection<ConfigAttribute> attributes) {
        if (authentication == null) {
            return ACCESS_DENIED;
        }
        int result = ACCESS_ABSTAIN;
        Collection<? extends GrantedAuthority> authorities = extractAuthorities(authentication);

        for (ConfigAttribute attribute : attributes) {
            if (this.supports(attribute)) {
                result = ACCESS_DENIED;

                for (GrantedAuthority authority : authorities) {
                    if (compareAttrAuth(authority,attribute) ==ACCESS_GRANTED){
                        return ACCESS_GRANTED;
                    }
                }
            }
        }
        return result;
    }

    private Collection<? extends GrantedAuthority> extractAuthorities(
            Authentication authentication) {
        return authentication.getAuthorities();
    }
    
    private Long formatDate(String s){
        String[] split = s.split(PasswdDateGrantAttribute.CA_PREFIX);
        if (split.length != 2){
            return 0L;
        }
        try{
            return Long.valueOf(split[1]);
        }catch (Exception e){
            return 0L;
        }
    }
    private int compareAttrAuth(GrantedAuthority authority, ConfigAttribute attribute){
        if (!authority.getAuthority().startsWith(PasswdDateGrantAttribute.GA_PREFIX)) return ACCESS_ABSTAIN;
        LocalDate birthLine = PasswdDateGrantAttribute.convert(authority.getAuthority());
        if (birthLine == null) return ACCESS_ABSTAIN;
        LocalDate deadLine = birthLine.plusDays(formatDate(attribute.getAttribute()));
        if (deadLine.isAfter(LocalDate.now())) return ACCESS_GRANTED;
        return ACCESS_DENIED;
    }

    @Override
    public boolean passGreenChannel() {
        return false;
    }

    @Override
    public boolean denyGreenChannel() {
        return true;
    }
}
