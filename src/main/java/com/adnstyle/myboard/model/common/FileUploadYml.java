package com.adnstyle.myboard.model.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

//@Data
//@Component
@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "spring.file-upload")
public class FileUploadYml {

    /**
     * 파일이 저장될 경로
     */
    private final String saveDir;

}