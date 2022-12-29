
package enterprise;

import org.imgscalr.Scalr;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class ImagesResizer implements Runnable {

    private List<File> files;
    private int newWidth;
    private String dstFolder;
    private long start;

    public ImagesResizer(List<File> files, int newWidth, String dstFolder, long start) {
        this.files = files;
        this.newWidth = newWidth;
        this.dstFolder = dstFolder;
        this.start = start;
    }

    private BufferedImage ImgscalrResize(BufferedImage image, int width, int height) {

        int imgWidth = image.getWidth();
        int imgHeight = image.getHeight();
        double destWidth = width;
        double destHeight = height;

        if ((imgWidth < imgHeight) && (imgHeight > height)) {

            destWidth = imgWidth * ((double) height / imgHeight);

        } else if ((imgWidth >= imgHeight) && (imgWidth > width)) {

            destHeight = imgHeight * ((float) width / imgWidth);

        } else {
            return image;
        }

        return Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC,
                (int) destWidth, (int) destHeight);
    }

    @Override
    public void run() {

        try {

            for (File file : files) {

                BufferedImage image = ImageIO.read(file);

                if (image == null) {
                    continue;
                }

                int newHeight = image.getHeight() / (image.getWidth() / newWidth);

                BufferedImage newImage = ImgscalrResize(image, newWidth, newHeight);

                File newFile = new File(dstFolder + "/" + file.getName());

                ImageIO.write(newImage, "jpg", newFile);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("Thread time of processing: " + (System.currentTimeMillis() - start) + " ms");
    }
}
