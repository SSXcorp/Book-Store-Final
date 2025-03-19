package mate.academy.bookstoreprod.config;

//
//import java.util.Properties;
//import javax.sql.DataSource;
//import org.apache.commons.dbcp2.BasicDataSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.core.env.Environment;
//import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
//
//@Configuration
//@PropertySource("classpath:application.properties")
//@ComponentScan(basePackages = "mate.academy.bookstore")
//public class AppConfig {
//
//    @Autowired
//    private Environment env;
//
//    @Bean
//    public DataSource getDataSource() {
//        BasicDataSource dataSource = new BasicDataSource();
//        dataSource.setDriverClassName(env.getProperty("db.driver"));
//        dataSource.setUrl(env.getProperty("db.url"));
//        dataSource.setUsername(env.getProperty("db.username"));
//        dataSource.setPassword(env.getProperty("db.password"));
//        return dataSource;
//    }
//
//    @Bean
//    public LocalSessionFactoryBean getSessionFactory() {
//        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
//        sessionFactory.setDataSource(getDataSource());
//
//        Properties properties = new Properties();
//        properties.put("show_sql", env.getProperty("hibernate.show_sql"));
//        properties.put("hibernate.hbm2dll.auto", env.getProperty("hibernate.hbm2dll.auto"));
//        sessionFactory.setHibernateProperties(properties);
//
//        sessionFactory.setPackagesToScan("mate.academy.bookstore");
//
//        return sessionFactory;
//    }
//}
