package engine.stopCondition;

import engine.Engine;

import java.util.Timer;
import java.util.TimerTask;

public class TimeCondition implements StopCondition{

    private static boolean m_FirstExecute = true;
    private boolean m_ReturnValue = false;
    private int m_minutes;
    private Timer timerMinute;
    private TimerTask timerTaskMinute;
    private TimerTask timerTaskUpdateSecond;
    private Timer timerUpdateSecond;
    private int m_Seconds;
    private boolean forHelper=false;
    private int helper=0;
    private int secondLeft;

    public TimeCondition(int minutes)
    {
        m_minutes = minutes;
        timerTaskMinute = null;
        timerTaskUpdateSecond = null;
    }

    public void setMinutes(int minutes){
        m_minutes=minutes;
        m_Seconds= m_minutes*60;
    }

    @Override
    public boolean execute(Engine.DataEngine dataEngine) {

        if(m_FirstExecute)
        {
            if(m_IsPause)
            {
                m_IsPause=false;
            }
            else
            {
                secondLeft=0;
            }
            m_IsStop=false;
            m_ReturnValue = false;
            timerMinute = new Timer();
            timerUpdateSecond = new Timer();
            timerTaskMinute = new TimerTask() {
                @Override
                public void run() {
                    m_ReturnValue = true;
                    timerUpdateSecond.cancel();
                    timerMinute.cancel();
                }
            };
            timerTaskUpdateSecond = new TimerTask()
            {
                @Override
                public void run()
                {
                    if(m_IsStop)
                    {
                        stop();
                        return;
                    }

                    if(forHelper)
                    {
                        secondLeft=helper;
                        forHelper=false;
                    }

                    secondLeft += 1;
                }
            };

            timerMinute.schedule(timerTaskMinute, (long)  m_Seconds * 1000);
            timerUpdateSecond.schedule(timerTaskUpdateSecond, 0, 1000);
            m_FirstExecute = false;
        }

        if(m_ReturnValue)
        {
            m_FirstExecute = true;
        }

        return m_ReturnValue;
    }

    public int getSecondLeft(){

            return secondLeft;
    }

    boolean m_IsStop=false;

    public void toFinish() {
        m_IsStop=true;

    }


    private void stop()
    {
        timerTaskUpdateSecond.cancel();
        timerTaskMinute.cancel();
        m_FirstExecute = true;
    }

    boolean m_IsPause=false;

    public void pause()
    {
        toFinish();
        m_IsPause=true;
    }

    public void resume()
    {
       m_Seconds = m_minutes * 60 - secondLeft;
       helper=secondLeft;
       forHelper=true;
       execute(null);
    }
}
