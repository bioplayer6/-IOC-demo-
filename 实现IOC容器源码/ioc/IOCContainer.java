package ioc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class IOCContainer {
    private Map<String,Object> ioc= new HashMap<>();
    private List<String> beanNames =new ArrayList<>();

    public IOCContainer(String pack) {

        //1.遍历包，封装beanDefiniton,装入Set集合
        Set<BeanDefiniton> beanDefinitons= findBeanDefinitions(pack);
        //2.通过反射机制和原材料beanDefinitons创建对象
        createObject(beanDefinitons);
        //3.自动装配
        autowiredObject(beanDefinitons);
    }

    public Integer getBeanDefinitionCount(){
        return beanNames.size();
    }

    public String[] getBeanDefinitionNames(){
        return beanNames.toArray(new String[0]);
    }

    private void autowiredObject(Set<BeanDefiniton> beanDefinitons) {
        Iterator<BeanDefiniton> iterator = beanDefinitons.iterator();
        while (iterator.hasNext()) {
            BeanDefiniton beanDefiniton= iterator.next();
            Class<?> beanClass= beanDefiniton.getBeanClass();
            Field[] declaredFields = beanClass.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                Autowired autowired= declaredField.getAnnotation(Autowired.class);
                Qualifier qualifier=declaredField.getAnnotation(Qualifier.class);
                if(autowired!=null){
                    Object mObject =null;
                    Object object =null;
                    String fieldName =declaredField.getName();
                    String methodName = "set" + fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
                    Method method = null;
                    try {
                        method = beanClass.getMethod(methodName , declaredField.getType());
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    if(qualifier!=null){
                        //ByName
                        String beanName=qualifier.value();
                        mObject =getBean(beanDefiniton.getBeanName());
                        object =getBean(beanName);
                        try {
                            method.invoke(mObject,object);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }

                    }else {
                        //ByType
                        object = getBean(fieldName);
                        mObject =getBean(beanDefiniton.getBeanName());
                        try {
                            method.invoke(mObject,object);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public Object getBean(String beanName){
        return ioc.get(beanName);
    }
    private void createObject(Set<BeanDefiniton> beanDefinitons) {
        Iterator<BeanDefiniton> iterator = beanDefinitons.iterator();
        while (iterator.hasNext()) {
            BeanDefiniton beanDefiniton= iterator.next();
            Class<?> beanClass= beanDefiniton.getBeanClass();
            String beanName =beanDefiniton.getBeanName();
            try {
                Object object =beanClass.getConstructor().newInstance();
                Field[] declaredFields = beanClass.getDeclaredFields();
                for (Field declaredField : declaredFields) {
                    Value valueAnnotaion = declaredField.getAnnotation(Value.class);
                    if(valueAnnotaion!=null){
                        String value =valueAnnotaion.value();
                        String fieldName =declaredField.getName();
                        String methodName= "set"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
                        String typeName=declaredField.getType().getName();
                        Method method =beanClass.getMethod(methodName,declaredField.getType());
                        Object val=null;
                        switch (typeName){
                            case "java.lang.String":
                                val=value;
                                break;
                            case "java.lang.Integer":
                                val=Integer.parseInt(value);
                                break;
                            case "java.lang.Float":
                                val=Float.parseFloat(value);
                                break;
                        }
                        method.invoke(object,val);
                    }

                }
                ioc.put(beanName,object);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public Set<BeanDefiniton> findBeanDefinitions(String pack){
        Set<Class<?>> classes= MyTools.getClasses(pack);
        Set<BeanDefiniton> beanDefinitons = new HashSet<>();
        Iterator<Class<?>> iterator = classes.iterator();
        while (iterator.hasNext()) {
            Class<?> beanClass=iterator.next();
            Component component = beanClass.getAnnotation(Component.class);
            if(component!=null){
                String value=component.value();
                if("".equals(value)){
                    String replace =beanClass.getName().replace(beanClass.getPackage().getName()+".","");
                    value = replace.substring(0,1).toLowerCase()+replace.substring(1);
                }
                beanDefinitons.add(new BeanDefiniton(value,beanClass));
                beanNames.add(value);
            }
        }
        return beanDefinitons;

    }
}
