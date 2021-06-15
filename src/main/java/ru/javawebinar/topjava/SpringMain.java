package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));

            //MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            //System.out.println("\nGet all meals for current user:");
            //mealRestController.getAllForUser().forEach(System.out::println);
            //System.out.println("\nTrying to delete wrong meal:");
            //mealRestController.delete(1);
            //System.out.println("\nGet current user meal having id=1:\n" + mealRestController.get(1));
            //System.out.println("\nGet all meals for current user filtered by date and time:");
            //mealRestController.getAllForUserFiltered(LocalDate.of(2020, Month.JANUARY, 30), LocalTime.MIN,
            // LocalDate.of(2020, Month.JANUARY, 30), LocalTime.MAX).forEach(System.out::println);
        }
    }
}
