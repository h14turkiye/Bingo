package com.h14turkiye.bingo.game.board.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.h14turkiye.bingo.Bingo;
import com.h14turkiye.bingo.game.BingoGame;
import com.h14turkiye.bingo.game.BingoPlayer;

public class BoardRenderer extends MapRenderer {

    private final Bingo plugin;
    private final BingoGame game;
    private final Image[] images;
    HashMap<BingoPlayer, Integer> renderedItems = new HashMap<>();
    private Image finished;

    public BoardRenderer(Bingo plugin, BingoGame game) {
        this.game = game;
        this.plugin = plugin;
        images = new Image[25];
    }

    @Override
    public void render(MapView map, MapCanvas canvas, Player player) {
    	BingoPlayer bingoPlayer = game.getBingoPlayer(player);
    	if(bingoPlayer == null)
    		return;
    	int foundItems = bingoPlayer.getBoard().getFoundItems();
    	if(renderedItems.getOrDefault(bingoPlayer, -1) == foundItems)
    		return;
        //make background transparent
        for (int i = 0; i < 128; i++) {
            for (int j = 0; j < 128; j++) {
                canvas.setPixelColor(i, j, Color.GRAY);
            }
        }

        drawGrid(canvas);
        drawImages(canvas, bingoPlayer);
        renderedItems.put(bingoPlayer, foundItems);
        drawTransparentImage(canvas, toBufferedImage(finished), 0, 0);
    }

    private void drawImages(MapCanvas canvas, BingoPlayer bingoPlayer) {
        int offset = 8;
        //the x and y coordniates of the positions on the grid
        int[][] imagePositions = new int[][]{
                //row 1
                {offset, offset},
                {24 + offset, offset},
                {48 + offset, offset},
                {72 + offset, offset},
                {96 + offset, offset},
                //row2
                {offset, offset + 24},
                {24 + offset, offset + 24},
                {48 + offset, offset+ 24},
                {72 + offset, offset + 24},
                {96 + offset, offset + 24},
                //row3
                {offset, offset + 48},
                {24 + offset, offset + 48},
                {48 + offset, offset+ 48},
                {72 + offset, offset + 48},
                {96 + offset, offset + 48},
                //row4
                {offset, offset + 72},
                {24 + offset, offset + 72},
                {48 + offset, offset+ 72},
                {72 + offset, offset + 72},
                {96 + offset, offset + 72},
                //row5
                {offset, offset + 96},
                {24 + offset, offset + 96},
                {48 + offset, offset+ 96},
                {72 + offset, offset + 96},
                {96 + offset, offset + 96},
        };

        for (int i = 0; i < images.length; i++) {
        	if (bingoPlayer.getBoard().getItems()[i].isFound()) {
                drawRect(canvas, new Color(MapPalette.matchColor(0, 162, 56)), imagePositions[i][0]-3, imagePositions[i][0]-3 + 22, imagePositions[i][1]-3, imagePositions[i][1]-3+22);
                
            }
            drawTransparentImage(canvas, toBufferedImage(images[i]), imagePositions[i][0], imagePositions[i][1]);
            
        }
    }
    
    private void drawTransparentImage(MapCanvas canvas, BufferedImage bufferedImage, int posX, int posY) {

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        int[] pixels = new int[width * height];
        if (pixels.length == 0) { return; }
        PixelGrabber pg = new PixelGrabber(bufferedImage, 0, 0, width, height, pixels, 0, width);

        try {

            pg.grabPixels();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                	
                	Color cOrg = new Color(bufferedImage.getRGB(x, y), true);
                    Color cPalette = new Color(MapPalette.matchColor(cOrg.getRed(), cOrg.getGreen(), cOrg.getBlue()));

                    if (cOrg.getAlpha() == 0){ continue; };

                    int cRed = (cOrg.getRed() * cOrg.getAlpha() + cPalette.getRed() * (255 - cOrg.getAlpha())) / 255;
                    int cGreen = (cOrg.getGreen() * cOrg.getAlpha() + cPalette.getGreen() * (255 - cOrg.getAlpha())) / 255;
                    int cBlue = (cOrg.getBlue() * cOrg.getAlpha() + cPalette.getBlue() * (255 - cOrg.getAlpha())) / 255;

                    final Color cFinal = new Color(MapPalette.matchColor(cRed, cGreen, cBlue));
                   
                    canvas.setPixelColor(posX + x, posY + y, cFinal);

                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    
    /**
     * Converts a given Image into a BufferedImage
     *
     * @param img The Image to be converted
     * @return The converted BufferedImage
     */
    private BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    private void drawGrid(MapCanvas canvas) {
        int width = 2;
        Color color = Color.WHITE;
        
        //outer line not tested
        drawRect(canvas, color, 0, 0 + 5, 0, 128); // left
        drawRect(canvas, color, 128 - 5, 128, 0, 128); // right
        drawRect(canvas, color, 0, 128, 0, 0 + 5); // up
        drawRect(canvas, color, 0, 128, 128 - 5, 128); // down

        //vertical lines
        drawRect(canvas, color, 27, 27 + width, 0, 128);
        drawRect(canvas, color, 51, 51 + width, 0, 128);
        drawRect(canvas, color, 75, 75 + width, 0, 128);
        drawRect(canvas, color, 99, 99 + width, 0, 128);

        //horizontal lines
        drawRect(canvas, color, 0, 128, 27, 27 + width);
        drawRect(canvas, color, 0, 128, 51, 51 + width);
        drawRect(canvas, color, 0, 128, 75, 75 + width);
        drawRect(canvas, color, 0, 128, 99, 99 + width);
    }

    private void drawRect(MapCanvas canvas, Color color, int fromX, int toX, int fromY, int toY) {
        for (int x = fromX; x < toX; x++) {
            for (int y = fromY; y < toY; y++) {
                canvas.setPixelColor(x, y, color);
            }
        }
    }


    public void updateImages() {
        try {
            for (int i = 0; i < game.getItems().length; i++) {
            	File file = new File(plugin.getDataFolder(), "assets/textures/" + game.getItems()[i].toString() + ".png");
                InputStream stream = new FileInputStream(file);
                images[i] = ImageIO.read(stream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
