package com.example.administrator.canvasdemo;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

public class DAO<T> {
    private T entity;
    private Class<T>clazz;
    T get(Integer id){
        return entity;
    }
    //保存一个对象
    void save(T entity){
        this.entity=entity;
    }

    public DAO(){
        System.out.println("dao is constrctor");
        System.out.println(this);
        System.out.println(this.getClass());
        Class clazz1=this.getClass().getSuperclass();
        System.out.println(clazz1);
        Type type=this.getClass().getGenericSuperclass();
        System.out.println(type);
        if (type instanceof ParameterizedType){
            ParameterizedType parameterizedType= (ParameterizedType) type;
            Type[]args=parameterizedType.getActualTypeArguments();
            System.out.println(Arrays.asList(args));
            if (args!=null&&args.length>0){
                Type arg=args[0];
                System.out.println(arg);
                if (arg instanceof Class){
                    clazz= (Class<T>) arg;
                }
            }
        }

    }
}
