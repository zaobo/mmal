package com.zab.mmal.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "ftp")
@Data
@Component
public class FtpConfiguration {
    @Value("${ftp.server.ip}")
    private String serverIp;
    private String username;
    private String password;
    @Value("${ftp.server.port}")
    private String serverPort;
    @Value("${ftp.server.http.prefix}")
    private String serverHttpPrefix;

}
