package com.ooka.test.hello;

import com.ooka.test.TestApplication;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

public class Hello {

    @Component
    @Qualifier("sayHelloService")
    public static class SayHelloService implements SaySomethingService {
        @Override
        public String saySomething() {
            return "Hello!";
        }
    }

    @Configuration
    public static class SaySomethingConfiguration {
        private final TestApplication testApplication;

        public SaySomethingConfiguration(TestApplication testApplication) {
            this.testApplication = testApplication;
        }

        @Bean
        @Primary
        public SaySomethingConfigurableService saySomethingConfigurableService() {
            SaySomethingConfigurableService saySomethingConfigurableService1 = new SaySomethingConfigurableService();
            saySomethingConfigurableService1.setWhatToSay("Bye");
            return saySomethingConfigurableService1;
        }
    }

    public static class SaySomethingConfigurableService implements SaySomethingService {

        private String whatToSay = "";

        @Override
        public String saySomething() {
            return whatToSay;
        }

        public String getWhatToSay() {
            return whatToSay;
        }

        public void setWhatToSay(String whatToSay) {
            this.whatToSay = whatToSay;
        }
    }
}
