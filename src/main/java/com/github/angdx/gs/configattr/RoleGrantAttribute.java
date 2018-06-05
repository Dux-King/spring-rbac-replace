package com.github.angdx.gs.configattr;

/**
 * 角色授权鉴权对象
 *
 * @author 王东旭
 */
public class RoleGrantAttribute implements GrantAttribute {
    public static final String PREFIX = "RO_";
    private String authority;

    public RoleGrantAttribute(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return PREFIX.concat(authority);
    }

    @Override
    public String getAttribute() {
        return PREFIX.concat(authority);
    }

    @Override
    public String toString() {
        return "RoleGrantAttribute{" +
                "authority='" + authority + '\'' +
                '}';
    }
}
