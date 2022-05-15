import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.imageio.plugins.jpeg.JPEGImageWriter;
import com.sun.imageio.plugins.jpeg.JPEGImageWriterSpi;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Main {

    public static final String root = ".\\image\\";
    public static final String keyAPI = "mbyJaX1HmPe6XMTdqhaxkhly8LmDrPhdw1ogbLU3";
    public static ObjectMapper mapper = new ObjectMapper();

    public static String downloadImageByURL(NASAResponse nasaResponse) throws IOException {

        if (nasaResponse.getMedia_type().equals("image")) {
            String fileName = root + nasaResponse.getHdurl()
                    .toString().split("/")[nasaResponse.getHdurl().toString().split("/").length - 1];
            BufferedImage img = ImageIO.read(nasaResponse.getHdurl().toURL());
            ImageWriter writer = new JPEGImageWriter(new JPEGImageWriterSpi());
            writer.setOutput(new FileImageOutputStream(new File(fileName)));
            writer.write(img);
            ((FileImageOutputStream) writer.getOutput()).close();
            return "картинка успешно сохранена в" + fileName;
        }
        return "файл не является картинкой";
    }

    public static void main(String[] args) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5_000)
                        .setSocketTimeout(30_000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request =
                new HttpGet("https://api.nasa.gov/planetary/apod?api_key=" + keyAPI);

        CloseableHttpResponse response = httpClient.execute(request);

        NASAResponse nasaResponse = mapper.readValue(response.getEntity().getContent(), new TypeReference<NASAResponse>() {
        });

        System.out.println(nasaResponse.getHdurl());
        System.out.println(nasaResponse.getUrl());
        request.setURI(nasaResponse.getHdurl());

        System.out.println(downloadImageByURL(nasaResponse));
    }


}
