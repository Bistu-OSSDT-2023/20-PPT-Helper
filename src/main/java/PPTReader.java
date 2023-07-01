import org.apache.poi.xslf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class PPTReader {

    public static void main(String[] args) {
        try {
            readPPTFile("/Users/sunday/Downloads/my12-3.pptx");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readPPTFile(String filePath) throws IOException {
        XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(new File(filePath)));
        List<XSLFSlide> slides = ppt.getSlides();

        // 输出幻灯片的数量
        System.out.println("Total slides: " + slides.size());

        // 创建一个计数器
        int slideNumber = 1;
        for (XSLFSlide slide : slides) {
            // 输出当前处理的幻灯片的页数
            System.out.println("Slide Number: " + slideNumber);

            List<XSLFShape> shapes = slide.getShapes();
            for (XSLFShape shape : shapes) {
                if (shape instanceof XSLFTextShape) {
                    XSLFTextShape textShape = (XSLFTextShape) shape;
                    for (XSLFTextParagraph textParagraph : textShape.getTextParagraphs()) {
                        for (XSLFTextRun textRun : textParagraph.getTextRuns()) {
                            System.out.println(textRun.getRawText());
                        }
                    }
                }
            }

            // 在处理完一张幻灯片后，将计数器加一
            System.out.println(slideNumber++ + " done");
        }
    }
}
