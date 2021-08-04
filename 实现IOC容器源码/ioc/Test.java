package ioc;

public class Test {
    public static void main(String[] args) {
        IOCContainer iocContainer=new IOCContainer("ioc.entity");
        System.out.println(iocContainer.getBeanDefinitionCount());
        String[] beanNames = iocContainer.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            System.out.println(beanName);
            System.out.println(iocContainer.getBean(beanName));
        }
    }
}
