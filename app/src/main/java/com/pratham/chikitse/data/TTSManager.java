package com.pratham.chikitse.data;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.util.Locale;

public class TTSManager {

    private static final String TAG = "TTSManager";
    private static TTSManager instance;

    private TextToSpeech tts;
    private boolean isReady = false;
    private boolean isKannada = true;
    private OnTTSReadyListener readyListener;

    public interface OnTTSReadyListener {
        void onReady(boolean success);
    }

    private TTSManager() {}

    public static TTSManager getInstance() {
        if (instance == null) {
            instance = new TTSManager();
        }
        return instance;
    }

    public void init(Context context, OnTTSReadyListener listener) {
        this.readyListener = listener;
        tts = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                setLanguageKannada();
                isReady = true;
                if (readyListener != null) readyListener.onReady(true);
            } else {
                Log.e(TAG, "TTS initialization failed");
                isReady = false;
                if (readyListener != null) readyListener.onReady(false);
            }
        });
    }

    public void setLanguageKannada() {
        if (tts == null) return;
        Locale kannadaLocale = new Locale("kn", "IN");
        int result = tts.setLanguage(kannadaLocale);
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            // Fall back to English
            tts.setLanguage(Locale.ENGLISH);
            isKannada = false;
            Log.w(TAG, "Kannada TTS not supported on this device, falling back to English");
        } else {
            isKannada = true;
        }
    }

    public void setLanguageEnglish() {
        if (tts == null) return;
        tts.setLanguage(Locale.ENGLISH);
        isKannada = false;
    }

    public void speak(String text) {
        if (!isReady || tts == null || text == null || text.isEmpty()) return;
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "utterance_" + System.currentTimeMillis());
    }

    public void speakStep(String instructionKannada, String instructionEnglish) {
        String textToSpeak = isKannada ? instructionKannada : instructionEnglish;
        speak(textToSpeak);
    }

    public void stop() {
        if (tts != null && tts.isSpeaking()) {
            tts.stop();
        }
    }

    public boolean isSpeaking() {
        return tts != null && tts.isSpeaking();
    }

    public boolean isKannada() {
        return isKannada;
    }

    public void toggleLanguage() {
        if (isKannada) {
            setLanguageEnglish();
        } else {
            setLanguageKannada();
        }
    }

    public void shutdown() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
            isReady = false;
            instance = null;
        }
    }
}
