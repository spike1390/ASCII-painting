
import java.awt.Color;
import java.awt.image.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;

public class BufferImage {
	private String outputFilePath;
	private int[][] result = null;
	private int height=0;
	private int width=0;
	private int type=0;

	public BufferImage(String inputImagePath,String outputfilepath) throws Exception{
		this.outputFilePath = outputfilepath;
		result=this.getImageGRB(inputImagePath);
		this.rgbtogray();
		this.work();
	}

	public int[][] getImageGRB(String filePath) throws Exception {
		File file = new File(filePath);
		int[][] result = null;
		if (!file.exists()) {

			throw new Exception("NOT FOUND");
		}
		try {
			BufferedImage bufImg = ImageIO.read(file);
			height = bufImg.getHeight();
			width = bufImg.getWidth();
			type=bufImg.getType();
			result = new int[width][height];
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					result[i][j] = bufImg.getRGB(i, j);// & 0xFFFFFF
					//System.out.println(bufImg.getRGB(i, j));// & 0xFFFFFF)

				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("end");
		return result;
	}


	private static int colorToRGB(int alpha, int red, int green, int blue) {

		int newPixel = 0;
		newPixel += alpha;
		newPixel = newPixel << 8;
		newPixel += red;
		newPixel = newPixel << 8;
		newPixel += green;
		newPixel = newPixel << 8;
		newPixel += blue;

		return newPixel;

	}

	// change rgb to gray value
	public void rgbtogray() {
		BufferedImage grayImage =
				new BufferedImage(width,height,type);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				final int color = result[i][j];
				final int r = (color >> 16) & 0xff;
				final int g = (color >> 8) & 0xff;
				final int b = color & 0xff;
				int gray =(int)( 0.299f*r + 0.587f*g + 0.114f*b);
				int newPixel = colorToRGB(255, gray, gray, gray);
				grayImage.setRGB(i, j, newPixel);
				result[i][j]=gray;
			}
		}
		/*
		 * output gray value image
		 * */
//		File newFile = new File("path");
//		try {
//			ImageIO.write(grayImage, "jpg", newFile);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public char convert(int g){
	    if (g <= 30) {
            return '#';
        } else if (g > 30 && g <= 60) {
            return '&';
        } else if (g > 60 && g <= 120) {
            return '$';
        }  else if (g > 120 && g <= 150) {
            return '*';
        } else if (g > 150 && g <= 180) {
            return 'o';
        } else if (g > 180 && g <= 210) {
            return '!';
        } else if (g > 210 && g <= 240) {
            return ';';
        }  else {
            return ' ';
        }

	}

	public void work() throws IOException{
		// creates a new, empty file named by this abstract pathname if the file not exists yet
		File f = new File(this.outputFilePath);
		if(!f.exists()){
			f.createNewFile();
		}

		PrintWriter writer = new PrintWriter(this.outputFilePath, "UTF-8");
	    for (int h = 0; h < height; h += 12) {
			// p means one line of the painting
	    	String p="";
            for (int w = 0; w < width; w += 6) {
                p +=this.convert( result[w][h]);
            }
            //write in the output file
            writer.println(p);
            System.out.println(p);
        }

	    writer.close();

	}


}
