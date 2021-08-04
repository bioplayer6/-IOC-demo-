package ioc;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BeanDefiniton {
    private String beanName;
    private Class beanClass;
}
