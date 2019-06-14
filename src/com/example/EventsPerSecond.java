package com.example;

public class EventsPerSecond {
    long m_millisLast;
    long m_millisCurrent;
    int m_events = 0;
    float m_eps=0.0f;

    public EventsPerSecond() {
        m_millisLast = System.currentTimeMillis();
        m_millisCurrent = System.currentTimeMillis();
    }

    public void reStartTimer(){
        m_millisLast = System.currentTimeMillis();
        m_millisCurrent = System.currentTimeMillis();
        m_events=0;
        m_eps=0;
    }

    public float updateFrameCounterGetFPS(){
        m_events++;

        m_millisCurrent = System.currentTimeMillis();

        // Ogni 500 millisecondi calcolo il fps
        if ((m_millisCurrent - m_millisLast) > 500) {
            m_eps= ((float) m_events) / (m_millisCurrent - m_millisLast)*1000.0f;
            m_events=0;
            m_millisLast = m_millisCurrent;
        }
        return m_eps;
    }
}