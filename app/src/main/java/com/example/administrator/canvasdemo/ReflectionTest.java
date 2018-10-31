package com.example.administrator.canvasdemo;

import android.view.animation.Animation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionTest {
    public static void main(String[]args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        PersonDao personDAO = new PersonDao();
        Person entity = new Person();
        //调用父类的save方法，同时也把Person这个“实参”传给了父类的T
        personDAO.save(entity);
        //这句的本意是要返回一个Person类型的对象
        Person result = personDAO.get(1);
    }
}
