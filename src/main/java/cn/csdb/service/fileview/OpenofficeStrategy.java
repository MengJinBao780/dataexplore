package cn.csdb.service.fileview;

import cn.csdb.model.FileInfo;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.File;
import java.util.Properties;

@Service
public class OpenofficeStrategy extends Strategy {
    @Override
    public String handleView(FileInfo fileInfo, Model model){
        String filePath = fileInfo.getFilePath();
        //File file = new File("D:/"+filePath);
        File file = new File("//"+filePath);
        if (file.exists() && file.isFile()){
            String targetFilePath = filePath.substring(0,filePath.lastIndexOf("."))+".pdf";
            File targetFile = new File(targetFilePath);
            if (targetFile.exists()){
                targetFile.delete();
            }
            try {
                ClassPathResource classPathResource = new ClassPathResource("/cas_urls.properties");
                Properties properties = PropertiesLoaderUtils.loadProperties(classPathResource);
                DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
                configuration.setOfficeHome(properties.getProperty("OfficeHome"));
                configuration.setPortNumber(8100);
                configuration.setTaskExecutionTimeout(1000 * 60 * 1L);
                configuration.setTaskQueueTimeout(1000 * 60 * 60 * 24L);
                OfficeManager officeManager = configuration.buildOfficeManager();
                OfficeDocumentConverter officeDocumentConverter = new OfficeDocumentConverter(officeManager);
                officeManager.start();
                officeDocumentConverter.convert(file,targetFile);
                if (officeManager !=null){
                    officeManager.stop();
                }
            }catch (Exception e){
                System.out.println(e);
            }
            model.addAttribute("targetFilePath",targetFilePath);
        }
        return "office";
    }
}
