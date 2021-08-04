package ioc.entity;

import ioc.Autowired;
import ioc.Component;
import ioc.Qualifier;
import ioc.Value;
import lombok.Data;

@Data
@Component
public class Account {
    @Value("123")
    private String name;
    @Value("11")
    private Integer id;
    @Value("45.2")
    private Float price;
    @Autowired
    private User tiga;
}
