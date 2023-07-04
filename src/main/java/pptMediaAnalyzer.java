import org.apache.poi.xslf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class pptMediaAnalyzer {

    public static void main(String[] args) {
        String pptFilePath = "D:\\testPPT\\daxueshiguang.pptx";  // 替换成你的PPT文件路径
        analyzePptMedia(pptFilePath);
    }

    private static void analyzePptMedia(String pptFilePath) {
        try (FileInputStream fis = new FileInputStream(new File(pptFilePath));
             XMLSlideShow ppt = new XMLSlideShow(fis)) {

            for (XSLFSlide slide : ppt.getSlides()) {
                System.out.println("Slide " + slide.getSlideNumber());
                int i = 0;
                for (XSLFShape shape : slide.getShapes()) {
                    if (shape instanceof XSLFPictureShape) {
                        XSLFPictureShape pictureShape = (XSLFPictureShape) shape;
                        //String mimeType = pictureShape.getPictureData().getMimeType();
                        String mimeType = pictureShape.getPictureData().getPackagePart().getContentType();
                        if (containsMediaKeywords(mimeType)) {
                            System.out.println("picture or video " + (++i));
                            System.out.println("  Picture Shape: " + mimeType);
                            // 执行相关操作
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean containsMediaKeywords(String mimeType) {
        // 根据需求，检查MIME类型是否为媒体/动画类型
        return mimeType.contains("video") || mimeType.contains("audio/mp3") || mimeType.contains("image");
    }
}
