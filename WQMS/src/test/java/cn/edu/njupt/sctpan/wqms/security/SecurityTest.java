package cn.edu.njupt.sctpan.wqms.security;


import org.junit.Test;
import org.springframework.util.DigestUtils;

public class SecurityTest {
    @Test
    public void md5Test() {
        String password = "123";
        System.out.println(DigestUtils.md5DigestAsHex(password.getBytes()));
    }
}
