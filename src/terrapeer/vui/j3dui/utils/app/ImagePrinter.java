package terrapeer.vui.j3dui.utils.app;

import java.awt.*;
import java.awt.geom.*;
import java.awt.print.*;

import terrapeer.vui.j3dui.utils.*;
import terrapeer.vui.j3dui.utils.app.*;

/**
 Provides image printing support.  The image is printed when
 this object is constructed.  The image will be sized to fit
 the print page and centered.
 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */
public class ImagePrinter implements Printable
{

  // public interface =========================================

  /**
    Initiates a print job that prints the image <image>.
   */
  public ImagePrinter(Image image)
  {
    Debug.println("ImagePrinter",
                  "PRINT:ImagePrinter: begin");

    _printImage = image;

    // initiate print job
    PrinterJob job = PrinterJob.getPrinterJob();
//		PageFormat pageFormat = job.pageDialog(job.defaultPage());

    _printRequest = true;
//		job.setPrintable(ImagePrinter.this, pageFormat);
    job.setPrintable(ImagePrinter.this);
    if (job.printDialog())
    {
      try
      {
        job.print();
      }
      catch (Exception ex)
      {
        System.out.println("ImagePrinter: " +
                           "Can't start print job.");
        ex.printStackTrace();
      }
    }
  }

  // Printable implementation

  /**
    Called by print handler.  Do not use this method directly.
   */
  public int print(Graphics graphics, PageFormat pageFormat,
                   int pageIndex)
  {

    Debug.println("ImagePrinter",
                  "PRINT:ImagePrinter:print: pageIndex=" + pageIndex);

    // if no more pages, quit
    if (pageIndex > 0)
    {
      return Printable.NO_SUCH_PAGE;
    }

    // build print data (once per page)
    if (_printRequest)
    {

      Debug.println("ImagePrinter",
                    "PRINT:ImagePrinter:print: begin print build");

      _printTransform = buildPrintTransform(_printImage, pageFormat);
      _printRequest = false;
    }

    // draw print data (one or more times per page)
    ( (Graphics2D) graphics).drawImage(_printImage,
                                       _printTransform, null);

    return Printable.PAGE_EXISTS;
  }

  // personal body ============================================

  /** True if a print has been requested. */
  private boolean _printRequest;

  /** Image to be printed. */
  private Image _printImage = null;

  /** Transform to fit image on page. */
  private AffineTransform _printTransform = null;

  /**
    Builds the print image transform such that the specified
    image fits the page and is centered.
    @param printImage Image to be printed on the page.
    @param pageFormat Page format (i.e. page size and position).
    @return New transform for printing the image.
   */
  protected AffineTransform buildPrintTransform(
      Image printImage, PageFormat pageFormat)
  {
    // build rects for convenience
    Rectangle2D.Double pageRect = new Rectangle2D.Double(
        pageFormat.getImageableX(), pageFormat.getImageableY(),
        pageFormat.getImageableWidth(),
        pageFormat.getImageableHeight());
    Rectangle2D.Double imageRect = new Rectangle2D.Double(0, 0,
        printImage.getWidth(null), printImage.getHeight(null));

    // resolve scale so largest dimension fits page
    double scaleV = 1.0, scaleH = 1.0;
    if (imageRect.getWidth() > pageRect.getWidth())
      scaleH = pageRect.getWidth() / imageRect.getWidth();
    if (imageRect.getHeight() > pageRect.getHeight())
      scaleV = pageRect.getHeight() / imageRect.getHeight();

    double scale;
    if (scaleV < scaleH)
      scale = scaleV;
    else
      scale = scaleH;

      // build transform to scale and then center image
    AffineTransform printTransform = new AffineTransform();

    printTransform.translate(
        -imageRect.getX(), -imageRect.getY());
    printTransform.scale(scale, scale);
    printTransform.translate(
        (pageRect.getX() + (pageRect.getWidth() -
                            scale * imageRect.getWidth()) / 2.0) / scale,
        (pageRect.getY() + (pageRect.getHeight() -
                            scale * imageRect.getHeight()) / 2.0) / scale);

    Debug.println("ImagePrinter",
                  "PRINT:ImagePrinter:buildPrintTransform:" +
                  " printTransform=" + printTransform);

    return printTransform;
  }

}