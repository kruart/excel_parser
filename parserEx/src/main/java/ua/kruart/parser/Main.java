package ua.kruart.parser;

import org.apache.commons.collections.MultiMap;
import ua.kruart.parser.excel.ParserExcel;
import ua.kruart.parser.excel.SaveImageFromUrl;


/**
 * Created by Arthur on 2/2/2016.
 */
public class Main {
    public static void main(String[] args) {

        MultiMap map = new ParserExcel().getImageLinkAndAttributesNames("testFile/111.xls", 1, 18);
        new SaveImageFromUrl().saveImage(map, "G:/rozetka");
    }
}
