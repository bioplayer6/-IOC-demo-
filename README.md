本次手写Spring IOC源码,借鉴自https://www.bilibili.com/video/BV1AV411i7VH ,在手写过程中,对于基于注解的Spring IOC 容器实现过程有了一定的了解,同时对于Java的反射机制有了更深的体会,手写完后感觉
基础更加巩固,希望能对日后的Spring等框架的学习有更大的帮助

基于注解的IOC容器实现过程:

1.遍历包,得到包中的Class

2.遍历得到Class中的beanName和Class,封装到BeanDefinition(需要自定义),并把BeanDefition装入BeanDefinitons(set集合)中

3.遍历BeanDefinitions通过反射动态加载类对象,并把对象装入ioc对象(map)

4.如果类中有注解需要依赖注入,把注解中的值注入对象中。

![未命名文件 (1)](https://user-images.githubusercontent.com/72901123/128160931-66a848aa-4183-4a5a-bddf-1f34dc6ee3cd.png)
