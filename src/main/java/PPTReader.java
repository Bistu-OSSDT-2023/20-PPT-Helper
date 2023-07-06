import org.apache.poi.xslf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class PPTReader {

    public static void main(String[] args) {
        try {
            readPPTFile("/Users/sunday/Downloads/1.pptx");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readPPTFile(String filePath) throws IOException {
        XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(new File(filePath)));
        List<XSLFSlide> slides = ppt.getSlides();
        String res = "";
        // 输出幻灯片的数量
        res+="Total slides: " + slides.size()+"\n";
        System.out.println("Total slides: " + slides.size());

        // 创建一个计数器
        int slideNumber = 1;
        for (XSLFSlide slide : slides) {
            // 输出当前处理的幻灯片的页数
            res+="【第" + slideNumber+"页】";
//            System.out.println("Slide Number: " + slideNumber);

            List<XSLFShape> shapes = slide.getShapes();
            for (XSLFShape shape : shapes) {
                if (shape instanceof XSLFTextShape) {
                    XSLFTextShape textShape = (XSLFTextShape) shape;
                    for (XSLFTextParagraph textParagraph : textShape.getTextParagraphs()) {
                        for (XSLFTextRun textRun : textParagraph.getTextRuns()) {
//                            System.out.println(textRun.getRawText().trim());
                            System.out.println(textRun.getRawText());
                            res+=textRun.getRawText();
                        }
                    }
                }
            }
            slideNumber++;
            // 在处理完一张幻灯片后，将计数器加一
//            res+=slideNumber + " done"+"\n";
//            System.out.println( slideNumber + " done");
        }
        System.out.println(res);
        return res;
    }
}
