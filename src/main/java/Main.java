import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import com.sun.imageio.plugins.jpeg.*;
import javax.imageio.stream.*;
import java.io.*;


public class Main {

    public static final String root = ".\\";

    public static String downloadImageByURL(NASAResponse nasaResponse) throws IOException {

        String fileName = root + nasaResponse.getHdurl()
                .toString().split("/")[nasaResponse.getHdurl().toString().split("/").length-1];

        BufferedImage img = ImageIO.read(nasaResponse.getHdurl().toURL());

        //            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//            for (int i = 0; i < height; i++)
//                for (int j = 0; j < width; j++)
//                    bi.setRGB(j, i, pixels[i*width +j]);
//            return bi;
//        }



        // Запись изображения в jpeg-формате

            ImageWriter writer = new JPEGImageWriter(new JPEGImageWriterSpi());
//            saveToImageFile(writer, fileName);

//        private void saveToImageFile(ImageWriter iw, String fileName) throws IOException {

        writer.setOutput(new FileImageOutputStream(new File(fileName)));
        writer.write(img);
        ((FileImageOutputStream) writer.getOutput()).close();



//        // Формирование BufferedImage из массива pixels
//        private BufferedImage copyToBufferedImage()  {
//            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//            for (int i = 0; i < height; i++)
//                for (int j = 0; j < width; j++)
//                    bi.setRGB(j, i, pixels[i*width +j]);
//            return bi;
//        }




//        try (BufferedInputStream in =new BufferedInputStream(nasaResponse.getHdurl().toURL().openStream())){
//            FileOutputStream fos = new FileOutputStream(fileName){
//
//
//
//            }
//        }catch (){
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


//        try (BufferedInputStream in = new BufferedInputStream(nasaResponse.getHdurl().toURL().openStream());
//             FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
//            byte dataBuffer[] = new byte[4092];
//            int bytesRead;
//            while ((bytesRead = in.read(dataBuffer, 0, 4092)) != -1) {
//                fileOutputStream.write(dataBuffer, 0, bytesRead);
//            }
//            fileOutputStream.flush();
//        } catch (IOException e) {
//            // handle exception
//        }

        return null;
    }

    public static final String keyAPI = "mbyJaX1HmPe6XMTdqhaxkhly8LmDrPhdw1ogbLU3";
    public static ObjectMapper mapper = new ObjectMapper();

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
        response = httpClient.execute(request);
        downloadImageByURL(nasaResponse);

    }


}
