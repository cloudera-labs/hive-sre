package com.cloudera.utils.common;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UtilTest {

    @Test
    public void test001() {
        List<String> testList = Arrays.asList("hello","from","ted");
        System.out.print(testList.stream().map(item -> item.toUpperCase()).collect(Collectors.joining(":")));
    }
}
