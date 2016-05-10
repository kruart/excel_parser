package ua.kruart.parser.excel;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by Arthur on 2/2/2016.
 */
public class ParserExcel {

    private final static Logger LOGGER = LoggerFactory.getLogger(ParserExcel.class);

    public MultiMap getImageLinkAndAttributesNames(String pathFromFileNameForParsing, int numberCellAttributeName, int numberCellImageLink){
        MultiMap mapImagesLinksAndAttributesNames = new MultiValueMap();  //создаем мапу <NameAttribute, ImageLink>

        File excelFile = new File(pathFromFileNameForParsing); //Переменная path содержит путь к документу в файловой системе

        try (POIFSFileSystem fileSystem = new POIFSFileSystem(excelFile);   //Открываем документ
             HSSFWorkbook workBook = new HSSFWorkbook(fileSystem)) {     // Получаем workbook

//            int numberOfSheets = workBook.getNumberOfSheets(); //узнаем кол-во страниц в файле
            HSSFSheet sheet = workBook.getSheetAt(0); // Проверяем только первую страницу
            Iterator<Row> rows = sheet.rowIterator(); // Перебираем все строки

            if (rows.hasNext()) // Пропускаем "шапку" таблицы
                rows.next();

            while (rows.hasNext()) { // Перебираем все строки начиная со второй до тех пор, пока документ не закончится
                HSSFRow row = (HSSFRow) rows.next();

                //Получаем ячейки из строки по номерам столбцов
                HSSFCell attributeNameCell = row.getCell(numberCellAttributeName);
                HSSFCell imageLinkCell = row.getCell(numberCellImageLink);

                if (attributeNameCell != null && imageLinkCell != null)
                    mapImagesLinksAndAttributesNames.put(attributeNameCell.getStringCellValue(), imageLinkCell.getStringCellValue());
            }
        }
        catch (IOException e){
            LOGGER.error("Something has gone wrong, {}", e.getMessage());
        }
        LOGGER.info("method getImageLinkAndAttributesNames worked successfully ...");
        return mapImagesLinksAndAttributesNames;
    }
}
