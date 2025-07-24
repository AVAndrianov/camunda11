package com.example.camundaExchange.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "operate.client")
public class OperateClientProperties {

    private String profile;
    private Zeebe zeebe = new Zeebe();
    private Camunda7 camunda7 = new Camunda7();

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Zeebe getZeebe() {
        return zeebe;
    }

    public void setZeebe(Zeebe zeebe) {
        this.zeebe = zeebe;
    }

    public Camunda7 getCamunda7() {
        return camunda7;
    }

    public void setCamunda7(Camunda7 camunda7) {
        this.camunda7 = camunda7;
    }

    public static class Zeebe {
        private String brokerAddress;
        private boolean securityEnabled;
        private boolean plaintext;

        public String getBrokerAddress() {
            return brokerAddress;
        }

        public void setBrokerAddress(String brokerAddress) {
            this.brokerAddress = brokerAddress;
        }

        public boolean isSecurityEnabled() {
            return securityEnabled;
        }

        public void setSecurityEnabled(boolean securityEnabled) {
            this.securityEnabled = securityEnabled;
        }

        public boolean isPlaintext() {
            return plaintext;
        }

        public void setPlaintext(boolean plaintext) {
            this.plaintext = plaintext;
        }
    }

    public static class Camunda7 {
        private String url;
        private String username;
        private String password;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
