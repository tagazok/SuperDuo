package it.jaschke.alexandria;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

public class ScannerActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    public static final String BARCODE = "barcode";

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
        formats.add(ZXingScannerView.ALL_FORMATS.get(2));
        mScannerView.setFormats(formats);
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.v("scanresult", rawResult.getText());
        Log.v("qrcode", rawResult.getBarcodeFormat().toString());
        Intent data = new Intent();
        data.putExtra(BARCODE, rawResult.getText());
        setResult(111, data);

        finish();
    }
}
