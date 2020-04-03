package cn.csg.ucs.bi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

@EnableEurekaClient
@SpringBootApplication
@MapperScan({"cn.csg.ucs.bi.business.**.mapper", "cn.csg.ucs.bi.common.**.mapper", "cn.csg.ucs.bi.core.**.mapper"})
@EnableTransactionManagement
public class AuthorityApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AuthorityApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(AuthorityApplication.class, args);
    }
}
