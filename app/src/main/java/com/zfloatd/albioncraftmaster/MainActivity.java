package com.zfloatd.albioncraftmaster;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String ONLINE_URL =
            "https://zfloatd.github.io/albion-craft-master/";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView webView = new WebView(this);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (url != null && url.startsWith(ONLINE_URL)) {
                    injectAcmBrand(view);
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description,
                                        String failingUrl) {
                if (failingUrl != null && failingUrl.startsWith(ONLINE_URL)) {
                    view.loadUrl("file:///android_asset/index.html");
                }
            }
        });

        // Garante foco e recebimento de toque no WebView em diferentes versões do Android/WebView.
        webView.setFocusable(true);
        webView.setFocusableInTouchMode(true);
        webView.setClickable(true);
        webView.setLongClickable(true);
        webView.requestFocusFromTouch();

        setContentView(webView);
        webView.loadUrl(ONLINE_URL + "?acm=" + System.currentTimeMillis());
    }

    private void injectAcmBrand(WebView webView) {
        String js =
                "(function(){"
                + "var svg=\"<svg viewBox='0 0 220 220' aria-label='ACM' style='width:100%;height:auto;display:block'>\""
                + "+\"<rect x='5' y='5' width='210' height='210' rx='42' fill='#070c12' stroke='#39d98a' stroke-width='6'/>\""
                + "+\"<circle cx='110' cy='110' r='78' fill='none' stroke='#39d98a' stroke-width='7'/>\""
                + "+\"<path d='M110 20l12 30 14 28-13 8 10 83-23 28-23-28 10-83-13-8 14-28z' fill='#f1f5f7' stroke='#0a1016' stroke-width='4'/>\""
                + "+\"<path d='M65 61l13-20 18 18 14-23 14 23 18-18 13 20-12 22H77z' fill='#39d98a' stroke='#0a1016' stroke-width='4'/>\""
                + "+\"<text x='110' y='143' text-anchor='middle' font-family='Arial,sans-serif' font-size='67' font-weight='900' letter-spacing='-7' fill='#fff' stroke='#0a1016' stroke-width='3' paint-order='stroke'>AC</text>\""
                + "+\"<text x='154' y='143' text-anchor='middle' font-family='Arial,sans-serif' font-size='67' font-weight='900' fill='#39d98a' stroke='#0a1016' stroke-width='3' paint-order='stroke'>M</text></svg>\";"
                + "var s=document.getElementById('acmAndroidBrandCss');"
                + "if(!s){s=document.createElement('style');s.id='acmAndroidBrandCss';s.textContent='.acmAndroidMark{width:148px;margin:0 auto 12px;filter:drop-shadow(0 0 18px #39d98a55)}.acmAndroidTitle{text-align:center;font-size:22px;font-weight:900}.acmAndroidTitle b{color:#39d98a}.acmAndroidSide{display:flex;align-items:center;gap:9px;padding:7px 8px 16px}.acmAndroidSide .mark{width:46px;flex:0 0 46px}.acmAndroidSide .txt{font-size:16px;font-weight:900;line-height:1.05}.acmAndroidSide .txt b{color:#39d98a}.acmAndroidSide small{display:block;color:#8794a2;font-size:9px;margin-top:4px;letter-spacing:.7px}';document.head.appendChild(s)}"
                + "var a=document.querySelector('#acmAuth .authLogo');"
                + "if(a && !a.dataset.acmBranded){a.innerHTML=\"<div class='acmAndroidMark'>\"+svg+\"</div><div class='acmAndroidTitle'>Albion Craft <b>Master</b></div>\";a.dataset.acmBranded='1'}"
                + "var l=document.querySelector('.side .logo');"
                + "if(l && !l.dataset.acmBranded){l.innerHTML=\"<div class='acmAndroidSide'><div class='mark'>\"+svg+\"</div><div><div class='txt'>Albion Craft <b>Master</b></div><small>zFloat</small></div></div>\";l.dataset.acmBranded='1'}"
                + "})();";
        webView.evaluateJavascript("javascript:" + js, null);
    }
}
