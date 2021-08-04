package ioc.entity;

import ioc.Component;
import ioc.Value;
import lombok.Data;

@Data
@Component("tiga")
public class User {
    @Value("lxy")
    private String userName;
    @Value("a3823783")
    private String password;
    @Value("22")
    private Integer age;
    @Value("1899.9")
    private Float purchasePrice;
}
