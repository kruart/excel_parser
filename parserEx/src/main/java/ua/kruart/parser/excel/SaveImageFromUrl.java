package ua.kruart.parser.excel;

import org.apache.commons.collections.MultiMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.kruart.parser.util.Util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Arthur on 2/3/2016.
 */
public class SaveImageFromUrl {

    private final static Logger LOGGER = LoggerFactory.getLogger(SaveImageFromUrl.class);
    private static Map<String, Integer> countMatchFolderByName = new HashMap<>();
    private static Map<String, Integer> countMatchImageByName = new HashMap<>();
    private BufferedImage image;

    public void saveImage(MultiMap map, String destinationPath){

        List list;
        Iterator it = map.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry mapEntry = (Map.Entry) it.next();
                list = (List) map.get(mapEntry.getKey());
                try {
                for (int j = 0; j < list.size(); j++) {
//                System.out.println(mapEntry.getKey() + " ->  " + list.get(j));
                    String folderPath = createFolderFromAttributeName((String)mapEntry.getKey(), destinationPath);
                    String [] imagesLinks = splitStrImageUrl((String) list.get(j));

                    for (String imageLink : imagesLinks) {

                        String fileName = makeFileName(folderPath, imageLink);
                        String extension = getExtension(fileName);

                        URLConnection urlConnection = Util.getConnection(imageLink);

                        image = ImageIO.read(urlConnection.getInputStream());
                        ImageIO.write(image, extension, new File(folderPath + "/" + fileName));
                    }

                }
                LOGGER.info("I has worked by key: {}", mapEntry.getKey());
            }
                catch (IOException e){
                    LOGGER.error("Something has gone wrong by key {}, {}" , mapEntry.getKey(), e.getMessage());
                }
        }
//        finally {closeStream()}
    }
    public String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public String[] splitStrImageUrl(String url) {

        String[] links = url.split("[,; ]+");
//        String[] links = url.split("(http://|https://|(www.)|[,; ]+)");
        return links;
    }

    public String makeFileName(String folderPath, String link){

        String nameImage = link.substring(link.lastIndexOf("/") + 1);
        int index = nameImage.lastIndexOf("?");
        if (index != -1)
            nameImage = nameImage.substring(0, index);

        String fullName = folderPath + "/" + nameImage;
        if (!countMatchImageByName.containsKey(fullName)) countMatchImageByName.put(fullName, 0);
        else {
            countMatchImageByName.put(fullName, countMatchImageByName.get(fullName) + 1);
            nameImage = countMatchImageByName.get(fullName) + "_" + nameImage;
        }
        return nameImage;
    }

    public String createFolderFromAttributeName(String folder, String destinationPath) {

        String path = destinationPath + "/"  + folder;
        boolean isCreated = new File(path).mkdirs();

        if(isCreated || !countMatchFolderByName.containsKey(folder)) {
            countMatchFolderByName.put(folder, 0);
            return path;
        }
        else {
            countMatchFolderByName.put(folder, countMatchFolderByName.get(folder) + 1);
            String twinFolder = path + "__" + countMatchFolderByName.get(folder);
            boolean mkdirs = new File(twinFolder).mkdirs();
            return twinFolder;
        }
    }

}
