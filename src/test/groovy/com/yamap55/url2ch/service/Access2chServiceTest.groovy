package com.yamap55.url2ch.service

import org.junit.Test

/**
 * Created with IntelliJ IDEA.
 * User: yui
 * Date: 14/11/03
 * Time: 19:52
 * To change this template use File | Settings | File Templates.
 */
class Access2chServiceTest {

    @Test
    void testGetBbsList() {
        def target = new Access2chService()
        System.out.println target.getBbsList()
    }

    void testGetThreadList() {

    }
}
