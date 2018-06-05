package com.github.angdx.gs.configattr;

import org.springframework.security.access.ConfigAttribute;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * 用于校验密码有效期的授权鉴权{@link ConfigAttribute}
 *
 * @author 王东旭
 */
public class PasswdDateGrantAttribute implements GrantAttribute {

    public static final String GA_PREFIX = "PD_";
    public static final String CA_PREFIX = "ED_";
    private static final long serialVersionUID = 8494523443532836297L;
    private transient LocalDate localDate;
    private int expireDate;

    /**
     * 设置密码的最后更改时间
     * @param localDate 最后更改时间
     */
    public PasswdDateGrantAttribute(LocalDate localDate) {
        this.localDate = localDate;
    }

    /**
     * 设置有效期
     * @param expireDate 有效期
     */
    public PasswdDateGrantAttribute(int expireDate) {
        this.expireDate = expireDate;
    }

    @Override
    public String getAuthority() {
        return GA_PREFIX.concat(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }

    public static LocalDate convert(String dateStr) {
        if (!dateStr.startsWith(GA_PREFIX)){
            return null;
        }
        try {
            return LocalDate.parse(dateStr.split(GA_PREFIX)[1]);
        }catch (DateTimeParseException e){
            return null;
        }
    }

    @Override
    public String getAttribute() {
        return CA_PREFIX.concat(String.valueOf(expireDate));
    }

    @Override
    public String toString() {
        return "PasswdDateGrantAttribute{" +
                "localDate=" + localDate +
                ", expireDate=" + expireDate +
                '}';
    }
}
