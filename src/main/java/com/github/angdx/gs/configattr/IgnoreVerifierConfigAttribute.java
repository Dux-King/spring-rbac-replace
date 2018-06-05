package com.github.angdx.gs.configattr;

import org.springframework.security.access.ConfigAttribute;

/**
 * 参与免校验选举的ConfigAttribute实现
 * @author 王东旭
 */
public class IgnoreVerifierConfigAttribute implements ConfigAttribute {


    private static final long serialVersionUID = 248610131416555266L;
    private IsIgnore isIgnore;
    public IgnoreVerifierConfigAttribute(IsIgnore isIgnore) {
        this.isIgnore = isIgnore;
    }

    @Override
    public String getAttribute() {
        return isIgnore.toString();
    }

    public IsIgnore isIgnore(){
        return this.isIgnore;
    }
    public enum IsIgnore{
        YES,NO,ABANDON
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IgnoreVerifierConfigAttribute that = (IgnoreVerifierConfigAttribute) o;

        return isIgnore == that.isIgnore;
    }

    @Override
    public int hashCode() {
        return isIgnore != null ? isIgnore.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "IgnoreVerifierConfigAttribute{" +
                "isIgnore=" + isIgnore +
                '}';
    }
}
