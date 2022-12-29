
package enterprise;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final int WIDTH = 300;

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        String srcFolder = "src/main/resources/source";
        String dstFolder = "src/main/resources/destination";

        int coresCounts = Runtime.getRuntime().availableProcessors();
        System.out.println("Number of cores: " + coresCounts);

        File srcDir = new File(srcFolder);
        File[] files = srcDir.listFiles();

        assert files != null;
        List<File> collection = new ArrayList<>(List.of(files));

        int partOfCollectionSize = collection.size() / coresCounts;
        int restOfCollectionSize = collection.size() % coresCounts;

        for (int i = 0; i < coresCounts; i++) {

            int startIndex = i * partOfCollectionSize;
            int endIndex = (i + 1) * partOfCollectionSize;

            if (i < restOfCollectionSize) {

                startIndex += i;
                endIndex += i + 1;

            } else {

                startIndex += restOfCollectionSize;
                endIndex += restOfCollectionSize;
            }

            List<File> collectionPart = new ArrayList<>(collection.subList(startIndex, endIndex));
            ImagesResizer resizer = new ImagesResizer(collectionPart, WIDTH, dstFolder, startTime);

            new Thread(resizer).start();
        }
    }
}
