/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s): Alexandre Iline.
 *
 * The Original Software is the Jemmy library.
 * The Initial Developer of the Original Software is Alexandre Iline.
 * All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 *
 *
 *
 * $Id$ $Revision$ $Date$
 *
 */

package org.netbeans.jemmy.util;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

/** This class allows to encode BufferedImage into B/W, greyscale or true color PNG
 * image format with maximum compression.<br>
 * It also provides complete functionality for capturing full screen, part of
 * screen or single component, encoding and saving captured image info PNG file.
 * @author Adam Sotona
 * @version 1.0 */
public class PNGEncoder extends Object {

    /** black and white image mode. */    
    public static final byte BW_MODE = 0;
    /** grey scale image mode. */    
    public static final byte GREYSCALE_MODE = 1;
    /** full color image mode. */    
    public static final byte COLOR_MODE = 2;
    
    OutputStream out;
    CRC32 crc;
    byte mode;

    /** public constructor of PNGEncoder class with greyscale mode by default.
     * @param out output stream for PNG image format to write into
     */    
    public PNGEncoder(OutputStream out) {
        this(out, GREYSCALE_MODE);
    }

    /** public constructor of PNGEncoder class.
     * @param out output stream for PNG image format to write into
     * @param mode BW_MODE, GREYSCALE_MODE or COLOR_MODE
     */    
    public PNGEncoder(OutputStream out, byte mode) {
        crc=new CRC32();
        this.out = out;
        if (mode<0 || mode>2)
            throw new IllegalArgumentException("Unknown color mode");
        this.mode = mode;
    }

    void write(int i) throws IOException {
        byte b[]={(byte)((i>>24)&0xff),(byte)((i>>16)&0xff),(byte)((i>>8)&0xff),(byte)(i&0xff)};
        write(b);
    }

    void write(byte b[]) throws IOException {
        out.write(b);
        crc.update(b);
    }
    
    /** main encoding method (stays blocked till encoding is finished).
     * @param image BufferedImage to encode
     * @throws IOException IOException
     */    
    public void encode(BufferedImage image) throws IOException {
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        final byte id[] = {-119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13};
        write(id);
        crc.reset();
        write("IHDR".getBytes());
        write(width);
        write(height);
        byte head[]=null;
        switch (mode) {
            case BW_MODE: head=new byte[]{1, 0, 0, 0, 0}; break;
            case GREYSCALE_MODE: head=new byte[]{8, 0, 0, 0, 0}; break;
            case COLOR_MODE: head=new byte[]{8, 2, 0, 0, 0}; break;
        }                 
        write(head);
        write((int) crc.getValue());
        ByteArrayOutputStream compressed = new ByteArrayOutputStream(65536);
        BufferedOutputStream bos = new BufferedOutputStream( new DeflaterOutputStream(compressed, new Deflater(9)));
        int pixel;
        int color;
        int colorset;
        switch (mode) {
            case BW_MODE: 
                int rest=width%8;
                int bytes=width/8;
                for (int y=0;y<height;y++) {
                    bos.write(0);
                    for (int x=0;x<bytes;x++) {
                        colorset=0;
                        for (int sh=0; sh<8; sh++) {
                            pixel=image.getRGB(x*8+sh,y);
                            color=((pixel >> 16) & 0xff);
                            color+=((pixel >> 8) & 0xff);
                            color+=(pixel & 0xff);
                            colorset<<=1;
                            if (color>=3*128)
                                colorset|=1;
                        }
                        bos.write((byte)colorset);
                    }
                    if (rest>0) {
                        colorset=0;
                        for (int sh=0; sh<width%8; sh++) {
                            pixel=image.getRGB(bytes*8+sh,y);
                            color=((pixel >> 16) & 0xff);
                            color+=((pixel >> 8) & 0xff);
                            color+=(pixel & 0xff);
                            colorset<<=1;
                            if (color>=3*128)
                                colorset|=1;
                        }
                        colorset<<=8-rest;
                        bos.write((byte)colorset);
                    }
                }
                break;
            case GREYSCALE_MODE: 
                for (int y=0;y<height;y++) {
                    bos.write(0);
                    for (int x=0;x<width;x++) {
                        pixel=image.getRGB(x,y);
                        color=((pixel >> 16) & 0xff);
                        color+=((pixel >> 8) & 0xff);
                        color+=(pixel & 0xff);
                        bos.write((byte)(color/3));
                    }
                }
                break;
             case COLOR_MODE:
                for (int y=0;y<height;y++) {
                    bos.write(0);
                    for (int x=0;x<width;x++) {
                        pixel=image.getRGB(x,y);
                        bos.write((byte)((pixel >> 16) & 0xff));
                        bos.write((byte)((pixel >> 8) & 0xff));
                        bos.write((byte)(pixel & 0xff));
                    }
                }
                break;
        }
        bos.close();
        write(compressed.size());
        crc.reset();
        write("IDAT".getBytes());
        write(compressed.toByteArray());
        write((int) crc.getValue()); 
        write(0);
        crc.reset();
        write("IEND".getBytes());
        write((int) crc.getValue()); 
        out.close();
    }

    /** Static method performing screen capture into PNG image format file with given fileName.
     * @param rect Rectangle of screen to be captured
     * @param fileName file name for screen capture PNG image file */    
    public static void captureScreen(Rectangle rect, String fileName) {
        captureScreen(rect, fileName, GREYSCALE_MODE);
    }

    /** Static method performing screen capture into PNG image format file with given fileName.
     * @param rect Rectangle of screen to be captured
     * @param mode image color mode
     * @param fileName file name for screen capture PNG image file */    
    public static void captureScreen(Rectangle rect, String fileName, byte mode) {
        try {
            BufferedImage capture=new Robot().createScreenCapture(rect);
            BufferedOutputStream file=new BufferedOutputStream(new FileOutputStream(fileName));
            PNGEncoder encoder=new PNGEncoder(file, mode);
            encoder.encode(capture);
        } catch (AWTException awte) {
            awte.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

     /** Static method performing one component screen capture into PNG image format file with given fileName.
      * @param comp Component to be captured
      * @param fileName String image target filename */    
    public static void captureScreen(Component comp, String fileName) {
        captureScreen(comp, fileName, GREYSCALE_MODE);
    }
    
    /** Static method performing one component screen capture into PNG image format file with given fileName.
     * @param comp Component to be captured
     * @param fileName String image target filename
     * @param mode image color mode */    
    public static void captureScreen(Component comp, String fileName, byte mode) {
	captureScreen(new Rectangle(comp.getLocationOnScreen(),
				    comp.getSize()),
		      fileName, mode);
    }

    
    /** Static method performing whole screen capture into PNG image format file with given fileName.
     * @param fileName String image target filename */    
    public static void captureScreen(String fileName) {
        captureScreen(fileName, GREYSCALE_MODE);
    }
    
    /** Static method performing whole screen capture into PNG image format file with given fileName.
     * @param fileName String image target filename
     * @param mode image color mode */    
    public static void captureScreen(String fileName, byte mode) {
	captureScreen(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()), fileName, mode);
    }
}
