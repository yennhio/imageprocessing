import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;

public class MedianFilter {

    int imageWidth, imageHeight;
    int maskSize;
    int[][] imagePixelValues = new int[imageHeight][imageWidth];

    public MedianFilter(int[][] imagePixelValues, int imageWidth, int imageHeight, int maskSize) {
        this.maskSize = maskSize;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.imagePixelValues = imagePixelValues;
    }
    
    public void outputImageFile(int[][] processedImagePixelValues) {
        BufferedImage outputImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_BYTE_GRAY);

        //set the pixel values in the BufferedImage
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                int pixelValue = processedImagePixelValues[y][x];
                int grayColor = (pixelValue << 16) | (pixelValue << 8) | pixelValue;
                outputImage.setRGB(x, y, grayColor);
            }
        }

        //save the BufferedImage as a JPG file
        File outputFile = new File("output.jpg");
        try {
            ImageIO.write(outputImage, "jpg", outputFile);
            System.out.println("JPEG image created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void applyMedianFilter(int[][] originalArr) {

        int[][] medianFilteredValues = new int[imageHeight][imageWidth];
        int topRow, bottomRow, leftMostColumn, rightMostColumn;
        int[] numbersArray;
        int median=0;

        for (int y=0; y<originalArr.length; y++) {
            for (int x=0; x<originalArr[0].length; x++) {

                topRow = y-(maskSize/2);
                bottomRow = y+(maskSize/2);
                leftMostColumn = x-(maskSize/2);
                rightMostColumn = x+(maskSize/2);
                int numberOfPixels=0, currentIndex=0;

                for (int j = topRow; j <= bottomRow; j++) {
                    for (int i = leftMostColumn; i <= rightMostColumn; i++) {
                        if (j >= 0 && j < imageHeight && i >= 0 && i < imageWidth) {
                            numberOfPixels++;
                        }
                    }
                }

                numberOfPixels = (bottomRow-topRow+1)*(rightMostColumn-leftMostColumn+1);
                
                numbersArray = new int[numberOfPixels];
                currentIndex=0;

                //store neighborhood pixel values in array to sort
                for (int j = topRow; j <= bottomRow; j++) {
                    for (int i = leftMostColumn; i <= rightMostColumn; i++) {
                        if (j >= 0 && j < imageHeight && i >= 0 && i < imageWidth) {
                            numbersArray[currentIndex] = imagePixelValues[j][i];
                            currentIndex++;
                        }
                    }
                }

                //sort values in array in increasing order
                Arrays.sort(numbersArray);
                if (numbersArray.length%2 == 0)
                    median = numbersArray[numbersArray.length/2] + numbersArray[numbersArray.length/2-1] /2;
                else if (numbersArray.length%2 != 0)
                    median = numbersArray[numbersArray.length / 2];

                medianFilteredValues[y][x] = median;
            }
        }
        outputImageFile(medianFilteredValues);
    }
}
