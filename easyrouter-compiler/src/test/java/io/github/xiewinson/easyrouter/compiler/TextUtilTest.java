package io.github.xiewinson.easyrouter.compiler;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by winson on 2017/11/30.
 */
public class TextUtilTest {

    @Test
    public void className(){

        Assert.assertEquals("USer", TextUtil.path2ClassName("uSer"));
        Assert.assertEquals("User", TextUtil.path2ClassName("/user"));
        Assert.assertEquals("UserDetail", TextUtil.path2ClassName("/user/detail"));
        Assert.assertEquals("UserDetail", TextUtil.path2ClassName("user/detail"));
    }

    @Test
    public void methodName(){

        Assert.assertEquals("user", TextUtil.path2MethodName("user"));
        Assert.assertEquals("user", TextUtil.path2MethodName("/user"));
        Assert.assertEquals("userDETail", TextUtil.path2MethodName("/user/DETail"));
        Assert.assertEquals("userDetail", TextUtil.path2MethodName("user/detail"));
    }

}