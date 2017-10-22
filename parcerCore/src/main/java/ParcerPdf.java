import com.google.common.io.CharStreams;
import model.Article;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.sun.xml.internal.ws.commons.xmlutil.Converter.UTF_8;

public class ParcerPdf implements Parcer {

    private String filePath;
    private PDFParser parser;
    private PDDocument pdDoc;

    public ParcerPdf(String filePath) {
        this.filePath = filePath;
    }

    public Article parceMetadate(){

        return null;
    }

    public List<Article> parce() {
        File file = new File(filePath);
        try {
            parser = new PDFParser( new RandomAccessBuffer(new FileInputStream(file)));
            parser.parse();
            pdDoc = parser.getPDDocument();

            COSDocument cosDoc = parser.getDocument();

            parceTableContent();
            PDFTextStripper pdfStripper = pdfStripper = new PDFTextStripper();
            String parsedText = pdfStripper.getText(pdDoc);


            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        ;

        return null;
    }

    /**
     *  Ищем и формируем ц"Cодержаине"
     *
     * @return
     */
    public List<Article> parceTableContent() throws IOException {
        int contenPageNunber=0;
        PDFTextStripper pdfStripper;
        List<Article> articles = new ArrayList<>();
        for(int i = 0; i<pdDoc.getNumberOfPages(); i++){
            pdfStripper = pdfStripper = new PDFTextStripper();
            pdfStripper.setStartPage(i);
            pdfStripper.setEndPage(i);
            String text = pdfStripper.getText(pdDoc);
            if(text.contains("Содержание")){
                contenPageNunber = i;
                break;
            }
        }
        for (int i =contenPageNunber; i < pdDoc.getNumberOfPages(); i++){
            pdfStripper = new PDFTextStripper();
            pdfStripper.setStartPage(i);
            pdfStripper.setEndPage(i);
            String text = pdfStripper.getText(pdDoc);

            if(text.isEmpty()){
                break;
            }

            Integer lastNumber = null;
            for(String line : text.split(System.lineSeparator())){

                if(checkEndOfTableContent(line)){
                    return articles;
                }

                if(line.isEmpty()){
                    continue;
                }
                Integer currentPageNumber = StringUtils.getArticelPagesNumber(line);
                if(currentPageNumber==null){
                    continue;
                } else  {
                    if(lastNumber != null) {
                        articles.add(new Article(lastNumber, currentPageNumber-1));
                    }
                    lastNumber = currentPageNumber;
                }
            }
        }
        return articles;
    }

    private boolean checkEndOfTableContent(final String line) {
        String testString = line.replace(System.lineSeparator(), "");
        if(ParcerConst.STOP_WORDS.contains(testString)){
            return true;
        }
        return false;
    }


}
