package engine.stopCondition;

import engine.Engine;

import java.util.Timer;
import java.util.TimerTask;

public class TimeCondition implements StopCondition{

    private static boolean m_FirstExecute = true;
    private boolean m_ReturnValue = false;
    private int m_minutes;
    private Timer timer = new Timer();
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            m_ReturnValue = true;
            endTime = (int) (System.currentTimeMillis() / 60000);
            timeLeft = endTime-startTime;
        }
    };


    private int startTime;
    private int endTime;
    private int timeLeft;

    public TimeCondition(int second)
    {
        m_minutes = second;
    }

    public int getSecond(){return m_minutes;}

    public void setMinutes(int minutes){m_minutes=minutes;}

    @Override
    public boolean execute(Engine.DataEngine dataEngine) {

        if(m_FirstExecute)
        {
            startTime = (int) (System.currentTimeMillis() / 60000);
            timer.schedule(timerTask, m_minutes * 60 * 1000);
            m_FirstExecute = false;
        }

        if(m_ReturnValue)
        {
            m_FirstExecute = true;
        }

        return m_ReturnValue;
    }

    public int getTimeLeft(){return timeLeft;}
}
