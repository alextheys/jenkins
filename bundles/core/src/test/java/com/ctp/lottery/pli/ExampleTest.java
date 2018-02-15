package com.ctp.lottery.pli;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ExampleTest {

    @Before
    public void setUp(){

    }

    @Test
    public void testExample() {
        Example example = new Example();
        example.setExample("test");
        System.out.println("in develop");
        assertThat(example.getExample(), is("test"));
    }
}
