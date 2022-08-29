package sit.int221.projectintegrate;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ApplicationConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    @Bean
    public listMapper listMapper() {
        return listMapper.getInstance();
    }
//    @Bean
//    public PasswordEncoder passwordEncoder() {return passwordEncoder();}
}