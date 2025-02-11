package com.rasikhoons.cryptoclub.Utils;

import org.springframework.beans.BeanWrapperImpl;

import java.util.ArrayList;
import java.util.List;

public class CommonUtils {

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapperImpl beanWrapper = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = beanWrapper.getPropertyDescriptors();
        List<String> nullPropertyNames = new ArrayList<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object value = beanWrapper.getPropertyValue(pd.getName());
            if (value == null || (value instanceof String && ((String) value).isBlank())) {
                nullPropertyNames.add(pd.getName());
            }
        }
        return nullPropertyNames.toArray(new String[0]);
    }
}
