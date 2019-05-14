package com.java.mobile.phone.login.service.impl;

import org.junit.Test;

/**
 * @author xdd
 * @date 2019/5/14
 */
public class DemoTest {

    //try2
    //finally2
    //finally1
    @Test
    public void test() {
        test2(1);
    }

    public void test2(int i) {
        try {
            if (i == 1) {
                throw new Exception();
            }
            System.out.println("try" + i);
            return;
        } catch (Exception e) {
            test2(i + 1);
        } finally {
            System.out.println("finally" + i);
        }
    }
}
