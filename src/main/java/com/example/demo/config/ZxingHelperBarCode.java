package com.example.demo.config;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Hashtable;
import java.util.UUID;

public class ZxingHelperBarCode {

    public static void saveBarcodeImage(UUID id, int width, int height) {
        try {
            String qrCodePath = "C:\\Users\\Lvh9x\\Documents\\GitHub\\SD74---Sneaker-Shop\\src\\main\\resources\\static\\images\\imgsBarcode\\";
            String qrCodeName = qrCodePath + id +  ".png";
            System.out.println(qrCodeName);
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            Writer writer = new Code128Writer();
            BitMatrix bitMatrix = writer.encode(String.valueOf(id), BarcodeFormat.CODE_128, width, height);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "png", byteArrayOutputStream);

            // Lưu ảnh vào đường dẫn qrCodeName
            File file = new File(qrCodeName);
            ImageIO.write(MatrixToImageWriter.toBufferedImage(bitMatrix), "png", file);
        } catch (Exception e) {
            System.out.printf("Lỗi " + e.getMessage());
        }
    }
}
