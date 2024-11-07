package com.example.vectorstore.config;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;


import java.io.File;
import java.util.List;

@Configuration
public class AppConfig {

    private final SimpleVectorStore vectorStore;

    @Value("classpath:/SPRi AI Brief_11월호_산업동향_F.pdf")
    private Resource pdfResource;

    public AppConfig(SimpleVectorStore vectorStore){
        this.vectorStore = vectorStore;
    }
    // 자동호출
    @PostConstruct
    public void init() {
        File file =new File("D:\\springai\\vectorstore\\src\\main\\resources\\SPRi.json");
       if(file.exists()){
           System.out.println("File exists");
           vectorStore.load(file);
       }else{
            System.out.println("Loading PDF~~");
           PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(pdfResource,
                    PdfDocumentReaderConfig.builder()
                            .withPageTopMargin(0)
                            .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                    .withNumberOfTopTextLinesToDelete(0)
                                    .build())
                            .withPagesPerDocument(1)
                            .build());

            List<Document> documents=pdfReader.get();
            TokenTextSplitter splitter = new TokenTextSplitter();
            List<Document> splitDocuments=splitter.apply(documents);
            vectorStore.add(splitDocuments);
            vectorStore.save(file);
            System.out.println("Application is ready to Serve the Requests");
        }
    }
}
