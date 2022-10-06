package com.JuTemp.Home.FragmentLogics;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.JuTemp.Home.Activities.ApplJuTemp;
import com.JuTemp.Home.R;
import com.JuTemp.Home.FragmentFramework.FragmentLogicJuTemp;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;
import java.util.Map;

public class QrJuTemp extends FragmentLogicJuTemp {

    ImageView qrImageView = null;

    @Override
    public void mainLogic(final Activity ThisActivity, final FragmentLogicJuTemp fragmentLogic, final View view) {
        super.mainLogic(ThisActivity, fragmentLogic, view);

        qrImageView = view.findViewById(R.id.qrImageView);

        view.findViewById(R.id.qrEncode).setOnClickListener(v -> {
            String qrContent = ((EditText) view.findViewById(R.id.qrText)).getText().toString();
            if (qrContent.isEmpty()) return;
            qrImageView = view.findViewById(R.id.qrImageView);
            qrImageView.setImageBitmap(generateBitmap(qrContent, qrImageView.getWidth(), qrImageView.getHeight()));
        });
        view.findViewById(R.id.qrScan).setOnClickListener(v -> {
            IntentIntegrator intentIntegrator = new IntentIntegrator(ThisActivity);
            intentIntegrator.initiateScan();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ApplJuTemp.viaShareString != null && !ApplJuTemp.viaShareString.isEmpty() && ApplJuTemp.shareFlag && ApplJuTemp.fragmentHaveChosen) {
            ApplJuTemp.shareFlag = false;
            String viaShareString = ApplJuTemp.viaShareString.substring(ApplJuTemp.viaShareString.indexOf("http")).trim();
            ApplJuTemp.viaShareString = null;
            ((EditText) view.findViewById(R.id.qrText)).setText(viaShareString);
            qrImageView.setImageBitmap(generateBitmap(viaShareString, 972, 972));//
        } else requestFocus(ThisActivity, view.findViewById(R.id.qrText));
    }

    @Override
    public void onActivityResult(Activity ThisActivity, int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(ThisActivity, requestCode, resultCode, intent);
        ((EditText) ThisActivity.findViewById(R.id.qrText)).setText(IntentIntegrator.parseActivityResult(requestCode, resultCode, intent).getContents());
    }

    private Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, "0");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0xff000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

}
