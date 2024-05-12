package provider;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Server {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);


    }
}
