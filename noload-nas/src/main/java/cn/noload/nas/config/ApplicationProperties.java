package cn.noload.nas.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Nas.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private SocksProxy socksProxy;

    public SocksProxy getSocksProxy() {
        return socksProxy;
    }

    public void setSocksProxy(SocksProxy socksProxy) {
        this.socksProxy = socksProxy;
    }

    public static class SocksProxy {
        private String host;
        private Integer port;
        private String password;
        private String encryption;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEncryption() {
            return encryption;
        }

        public void setEncryption(String encryption) {
            this.encryption = encryption;
        }
    }
}
