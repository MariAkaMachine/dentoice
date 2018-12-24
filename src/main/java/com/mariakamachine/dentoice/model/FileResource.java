package com.mariakamachine.dentoice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileResource {

    private Resource resource;
    private String fileName;

}
